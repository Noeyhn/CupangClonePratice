package com.github.cupangclone.web.dto.orders;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class OrderRequest {

    private Long itemId;
    private Long itemOptionId;
    private String orderNumber;
    private Long totalPrice;

    private String receiverName;
    private String receiverAddress;
    private Long receiverPostcode;
    private String receiverPhoneNumber;

}
