package com.austin.opaque_token.dto.item;

import com.austin.opaque_token.common.ItemState;

public record ItemResponse(String id, String data, String userId, ItemState itemState ) {

}
