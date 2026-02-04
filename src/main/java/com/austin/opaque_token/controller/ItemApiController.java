package com.austin.opaque_token.controller;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.austin.opaque_token.common.OpenApiConstants;
import com.austin.opaque_token.dto.item.ItemRequest;
import com.austin.opaque_token.dto.item.ItemResponse;
import com.austin.opaque_token.security.user.AuthUser;
import com.austin.opaque_token.service.ItemService;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/api/items")
@SecurityRequirement(name = OpenApiConstants.TOKEN_SECURITY_REQUIREMENT)
public class ItemApiController {

  private final ItemService itemService;

  public ItemApiController(ItemService itemService) {
    this.itemService = itemService;
  }

  @PreAuthorize("isAuthenticated()")
  @PostMapping
  public ItemResponse createItem(
      @RequestBody ItemRequest itemRequest, @AuthenticationPrincipal AuthUser authUser) {
    return itemService.createItem(itemRequest, authUser);
  }

  @PreAuthorize("isAuthenticated()")
  @GetMapping("/{id}")
  public ItemResponse getItem(
      @PathVariable("id") String itemId, @AuthenticationPrincipal AuthUser authUser) {
    return itemService.getItem(itemId, authUser);
  }

  @PreAuthorize("hasRole('ROLE_ADMIN')")
  @GetMapping
  public Page<ItemResponse> listAllItems(@PageableDefault @ParameterObject Pageable pageable) {
    return itemService.listAllItems(pageable);
  }

  // check isAuthenticated(), because authentication.principal is String for anonymous
  // authentication
  @PreAuthorize(
      "isAuthenticated() && (hasAuthority('ROLE_ADMIN') || (#userId == authentication.principal.userId))")
  @GetMapping(params = "userId")
  public Page<ItemResponse> listUserItems(
      @Parameter(allowEmptyValue = true) @RequestParam("userId") String userId,
      @PageableDefault @ParameterObject Pageable pageable) {
    return itemService.listUserItems(userId, pageable);
  }

  @PreAuthorize("isAuthenticated()")
  @PutMapping("/{id}")
  public ItemResponse updateItem(
      @PathVariable("id") String itemId,
      @RequestBody ItemRequest itemRequest,
      @AuthenticationPrincipal AuthUser authUser) {
    return itemService.updateItem(itemId, itemRequest, authUser);
  }

  @PreAuthorize("isAuthenticated()")
  @DeleteMapping("/{id}")
  public void deleteItem(
      @PathVariable("id") String itemId, @AuthenticationPrincipal AuthUser authUser) {
    itemService.deleteItem(itemId, authUser);
  }

  @PreAuthorize("hasRole('ROLE_ADMIN')")
  @PostMapping("/{id}/approve")
  public ItemResponse approveItem(@PathVariable("id") String itemId) {
    return itemService.approveItem(itemId);
  }

  @PreAuthorize("hasRole('ROLE_ADMIN')")
  @PostMapping("/{id}/reject")
  public ItemResponse rejectItem(@PathVariable("id") String itemId) {
    return itemService.rejectItem(itemId);
  }
}
