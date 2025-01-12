package com.github.cupangclone.service;

import com.github.cupangclone.repository.itemOption.ItemOption;
import com.github.cupangclone.repository.itemOption.ItemOptionRepository;
import com.github.cupangclone.repository.items.Items;
import com.github.cupangclone.repository.items.ItemsRepository;
import com.github.cupangclone.repository.orders.Orders;
import com.github.cupangclone.repository.orders.OrdersRepository;
import com.github.cupangclone.repository.payment.Payment;
import com.github.cupangclone.repository.payment.PaymentRepository;
import com.github.cupangclone.repository.userPrincipal.UserPrincipal;
import com.github.cupangclone.repository.userPrincipal.UserPrincipalRepository;
import com.github.cupangclone.web.dto.orders.OrderRequest;
import com.github.cupangclone.web.dto.payment.PaymentRequest;
import com.github.cupangclone.web.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderPaymentService {

    private final UserPrincipalRepository userPrincipalRepository;
    private final ItemsRepository itemsRepository;
    private final ItemOptionRepository itemOptionRepository;
    private final OrdersRepository ordersRepository;
    private final PaymentRepository paymentRepository;

    public UserPrincipal findUser(String email) {
        return userPrincipalRepository.findByEmail(email).orElseThrow(
                () -> new NotFoundException("고객 정보를 찾을 수 없습니다."));
    }

    /*
        TODO : 주문 실패 경우의 수 고민해보기 및 그에 관한 대응 로직 구현
               주문 취소에 대한 상태 Column 추가하기
     */
    @Transactional(transactionManager = "tmJpa1")
    public String registerOrder(String email, OrderRequest orderRequest) {

        // 고객 정보 확인
        UserPrincipal user = findUser(email);

        ItemOption itemOption;
        Items items = itemsRepository.findById(orderRequest.getItemId()).orElseThrow(
                () -> new NotFoundException("상품 정보를 찾을 수 없습니다."));


        if ( orderRequest.getItemOptionId() != null) {
            itemOption = itemOptionRepository.findById(orderRequest.getItemOptionId()).orElseThrow( () -> new NotFoundException("아이템 옵션 정보를 찾을 수 없습니다."));
        } else {
            itemOption = null;
        }


        Orders orders = Orders.builder()
                .items(items)
                .itemOption(itemOption)
                .orderNumber(orderRequest.getOrderNumber())
                .totalPrice(orderRequest.getTotalPrice())
                .userPrincipal(user)
                .receiverName(orderRequest.getReceiverName())
                .receiverAdress(orderRequest.getReceiverAddress())
                .receiverPostCode(orderRequest.getReceiverPostcode())
                .receiverPhoneNumber(orderRequest.getReceiverPhoneNumber())
                .paymentStatus(true)
                .build();

        ordersRepository.save(orders);

        return "주문정보가 성공적으로 저장되었습니다.";
    }

    /*
        TODO : 결제상태 관리 로직 추가
     */

    @Transactional(transactionManager = "tmJpa1")
    public String registerPayment(String email, PaymentRequest paymentRequest) {

        UserPrincipal user = findUser(email);

        Orders orders = ordersRepository.findById(paymentRequest.getOrderId()).orElseThrow(
                () -> new NotFoundException("주문 정보를 찾을 수 없습니다."));

        Payment payment = Payment.builder()
                .orders(orders)
                .totalPrice(paymentRequest.getTotalPrice())
                .paymentStatus(paymentRequest.getPaymentStatus())
                .build();

        paymentRepository.save(payment);

        return "결제정보가 성공적으로 주문되었습니다.";

    }
}
