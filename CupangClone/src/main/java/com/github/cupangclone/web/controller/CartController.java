package com.github.cupangclone.web.controller;

import com.github.cupangclone.service.AuthService;
import com.github.cupangclone.service.CartService;
import com.github.cupangclone.web.dto.carts.CartRequest;
import com.github.cupangclone.web.dto.carts.CartResponse;
import com.github.cupangclone.web.dto.carts.CartUpdateRequest;
import com.github.cupangclone.web.exceptions.NotAcceptResponse;
import com.github.cupangclone.web.exceptions.SuccessResponse;
import com.github.cupangclone.web.exceptions.responMessage.Message;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/*
    TODO LIST : 수량변경(옵션에 따른 변경사항 같이 둘지 따로 관리할지 고민해보기), 그에따른 가격변경, 옵션 추가 삭제 변경 기능 구현
                장바구니에 추가한 시간순으로 정렬(제일 최근에 담은 아이템이 맨 위에 보이도록)
 */

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
@Slf4j
public class CartController {

    private final CartService cartService;
    private final AuthService authService;

    /*
        장바구니에 아이템 추가
     */
    @PostMapping("/add")
    public ResponseEntity<Message> addItemToCart(@RequestBody CartRequest cartRequest, HttpServletRequest request) {
        String email = authService.blockAccessWithOnlyToken(request);
        if (email != null) {
            String result = cartService.addItem(email, cartRequest);
            return new SuccessResponse().getMessageResponseEntity(result);
        } else {
            return new NotAcceptResponse().sendMessage("잘못된 접근입니다.");
        }
    }

    /*
        장바구니에 아이템 삭제
     */
    @DeleteMapping("/remove")
    public ResponseEntity<Message> removeItemFromCart(
            @RequestParam("cart_id") Long cartId, HttpServletRequest request) {
        String email = authService.blockAccessWithOnlyToken(request);

        if (email != null) {
            String result = cartService.removeItemFromCart(email, cartId);
            return new SuccessResponse().getMessageResponseEntity(result);
        } else {
            return new NotAcceptResponse().sendMessage("잘못된 접근입니다.");
        }
    }

    /*
        장바구니 모든 아이템 조회
     */
    @GetMapping("/find_all")
    public ResponseEntity<Message> findAllCarts(HttpServletRequest request) {
        String email = authService.blockAccessWithOnlyToken(request);

        if (email != null) {
            List<CartResponse> results = cartService.getAllCartItems(email);
            return new SuccessResponse().getMessageResponseEntity(results);
        } else {
            return new NotAcceptResponse().sendMessage("잘못된 접근입니다.");
        }
    }

    /*
        장바구니 아이템 수량 변경
     */
    @PutMapping("/update_cart_quantity")
    public ResponseEntity<Message> updateCartQuantity(
            @RequestBody CartUpdateRequest cartUpdateRequest, HttpServletRequest request) {
        String email = authService.blockAccessWithOnlyToken(request);

        if (email != null) {
            String result = cartService.updateItemQuantity(email, cartUpdateRequest);
            return new SuccessResponse().getMessageResponseEntity(result);
        } else {
            return new NotAcceptResponse().sendMessage("잘못된 접근입니다.");
        }
    }

}
