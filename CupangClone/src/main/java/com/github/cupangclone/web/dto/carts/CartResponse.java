package com.github.cupangclone.web.dto.carts;

import com.github.cupangclone.repository.carts.Carts;
import com.github.cupangclone.repository.items.Items;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class CartResponse {

    private Long cartId;
    private String itemName;
//    private String optionNames;
    private Long itemQuantity;
    private Long totalPrice;

    public static CartResponse formCarts(Carts carts, Items items) {
        return CartResponse
                .builder()
                .cartId(carts.getCartId())
                .itemName(items.getItemName())
                .itemQuantity(carts.getItemQuantity())
                .totalPrice(carts.getTotalPrice())
                .build();
    }

}
