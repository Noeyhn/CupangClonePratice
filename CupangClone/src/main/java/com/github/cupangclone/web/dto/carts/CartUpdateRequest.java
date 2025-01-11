package com.github.cupangclone.web.dto.carts;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CartUpdateRequest {

    private Long cartId;
    private Long itemId;
    private Long itemOptionId;
    private Long itemQuantity;
    private Long totalPrice;

}
