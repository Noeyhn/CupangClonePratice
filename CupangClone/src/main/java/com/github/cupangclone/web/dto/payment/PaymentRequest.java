package com.github.cupangclone.web.dto.payment;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class PaymentRequest {

    private Long orderId;
    private Long totalPrice;
    private String paymentStatus;

}
