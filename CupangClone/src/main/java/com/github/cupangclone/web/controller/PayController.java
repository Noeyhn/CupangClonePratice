package com.github.cupangclone.web.controller;

import com.github.cupangclone.service.AuthService;
import com.github.cupangclone.service.OrderPaymentService;
import com.github.cupangclone.web.dto.orders.OrderRequest;
import com.github.cupangclone.web.dto.payment.PaymentRequest;
import com.github.cupangclone.web.exceptions.NotAcceptResponse;
import com.github.cupangclone.web.exceptions.SuccessResponse;
import com.github.cupangclone.web.exceptions.responMessage.Message;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
public class PayController {
    private final AuthService authService;
    private final OrderPaymentService orderPaymentService;

    /*
        TODO : 외부 API 연동하기 (내부 구조 및 로직에 대해 조금 더 알아보기)
               orderNumber 생성 로직 구현하기 (ex, ddmm + 구분코드 2자리 + 랜덤숫자및 문자 3자리 )
     */

    /*
        주문 정보 저장
     */
    @PostMapping("/add_order")
    public ResponseEntity<Message> addOrder(@RequestBody OrderRequest orderRequest, HttpServletRequest request) {
        String email = authService.blockAccessWithOnlyToken(request);

        if (email != null) {
            String result = orderPaymentService.registerOrder(email, orderRequest);
            return new SuccessResponse().getMessageResponseEntity(result);
        } else {
            return new NotAcceptResponse().sendMessage("잘못된 접근입니다.");
        }
    }

    /*
        결제 정보 저장
        TODO : 결제 상태에 관한 내용 ENUM으로 관리
               (결제 전, 결제 완료, 환불 및 할부결제에 대한 상태)
     */
    @PostMapping("/add_payment")
    public ResponseEntity<Message> addPayment(@RequestBody PaymentRequest paymentRequest, HttpServletRequest request) {
        String email = authService.blockAccessWithOnlyToken(request);

        if (email != null) {
            String result = orderPaymentService.registerPayment(email, paymentRequest);
            return new SuccessResponse().getMessageResponseEntity(result);
        } else {
            return new NotAcceptResponse().sendMessage("잘못된 접근입니다.");
        }
    }

}
