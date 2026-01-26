package com.austin.opaque_token.mapper;

import org.mapstruct.Mapper;

import com.austin.opaque_token.dto.item.ItemResponse;
import com.austin.opaque_token.entity.ItemEntity;

@Mapper(componentModel = "spring")
public interface ItemMapper {

    ItemResponse toResponse(ItemEntity itemEntity);
}
