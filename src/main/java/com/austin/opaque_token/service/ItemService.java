package com.austin.opaque_token.service;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.austin.opaque_token.common.ItemState;
import com.austin.opaque_token.common.Role;
import com.austin.opaque_token.dto.item.ItemRequest;
import com.austin.opaque_token.dto.item.ItemResponse;
import com.austin.opaque_token.entity.ItemEntity;
import com.austin.opaque_token.exception.NoAcessException;
import com.austin.opaque_token.exception.NotFoundException;
import com.austin.opaque_token.mapper.ItemMapper;
import com.austin.opaque_token.repository.ItemRepository;
import com.austin.opaque_token.security.user.AuthUser;
@Service
public class ItemService {

    private final ItemMapper itemMapper;

    private final ItemRepository itemRepository;

    public ItemService(
      ItemMapper itemMapper,
      ItemRepository itemRepository) {
        this.itemMapper = itemMapper;
        this.itemRepository = itemRepository;
      }

      public ItemResponse createItem(ItemRequest itemRequest, AuthUser authUser){
        ItemEntity itemEntity = new ItemEntity();
        itemEntity.setData(itemRequest.data());
        itemEntity.setId(authUser.userId());
        itemEntity.setItemState(ItemState.CREATED);

        ItemEntity savedItem = itemRepository.save(itemEntity);
        return itemMapper.toResponse(savedItem);
      }

      public ItemResponse getItem(String itemId, AuthUser authUser) {

    ItemEntity itemEntity = getItemEntity(itemId);
    checkAccessToItem(authUser, itemEntity);

    return itemMapper.toResponse(itemEntity);
  }

  public Page<ItemResponse> listAllItems(Pageable pageable) {

    return itemRepository.findAll(pageable).map(itemMapper::toResponse);
  }

  public Page<ItemResponse> listUserItems(String userId, Pageable pageable) {

    return itemRepository.findByUserId(userId, pageable).map(itemMapper::toResponse);
  }

  public ItemResponse updateItem(String itemId, ItemRequest itemRequest, AuthUser authUser) {

    ItemEntity itemEntity = getItemEntity(itemId);
    checkIsOwner(authUser, itemEntity);

    itemEntity.setData(itemRequest.data());
    itemEntity.setItemState(ItemState.CHANGED);

    ItemEntity updatedEntity = itemRepository.save(itemEntity);

    return itemMapper.toResponse(updatedEntity);
  }

  public void deleteItem(String itemId, AuthUser authUser) {

    ItemEntity itemEntity = getItemEntity(itemId);
    checkAccessToItem(authUser, itemEntity);
    itemRepository.deleteById(itemEntity.getId());
  }

  public ItemResponse approveItem(String itemId) {

    return setItemState(itemId, ItemState.APPROVED);
  }

  public ItemResponse rejectItem(String itemId) {

    return setItemState(itemId, ItemState.REJECTED);
  }

  private ItemResponse setItemState(String itemId, ItemState approved) {
    ItemEntity itemEntity = getItemEntity(itemId);
    itemEntity.setItemState(approved);

    ItemEntity updatedEntity = itemRepository.save(itemEntity);

    return itemMapper.toResponse(updatedEntity);
  }

  private ItemEntity getItemEntity(String itemId) {

    return itemRepository.findById(itemId).orElseThrow(NotFoundException::new);
  }

  // due to these methods security responsibilities is scattered between controller and service
  private void checkAccessToItem(AuthUser authUser, ItemEntity itemEntity) {

    if (!authUser.roles().contains(Role.ROLE_ADMIN) && !isOwner(authUser, itemEntity)) {
      throw new NoAcessException();
    }
  }

  private void checkIsOwner(AuthUser authUser, ItemEntity itemEntity) {
    if (!isOwner(authUser, itemEntity)) {
      throw new NoAcessException();
    }
  }

  private boolean isOwner(AuthUser authUser, ItemEntity itemEntity) {
    return StringUtils.equals(itemEntity.getId(), authUser.userId());
  }

}
