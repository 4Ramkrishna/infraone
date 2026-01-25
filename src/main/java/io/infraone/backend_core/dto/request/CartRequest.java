package io.infraone.backend_core.dto.request;

import lombok.Data;

@Data
public class CartRequest {
    private Long productId;
    private Integer quantity;
}