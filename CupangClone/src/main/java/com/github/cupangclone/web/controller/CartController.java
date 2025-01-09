package com.github.cupangclone.web.controller;

import com.github.cupangclone.service.AuthService;
import com.github.cupangclone.service.CartService;
import com.github.cupangclone.web.dto.carts.CartRequest;
import com.github.cupangclone.web.exceptions.NotAcceptException;
import com.github.cupangclone.web.exceptions.NotAcceptResponse;
import com.github.cupangclone.web.exceptions.SuccessResponse;
import com.github.cupangclone.web.exceptions.responMessage.Message;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/*
    TODO LIST : 장바구니 기본 추가 및 삭제, 수량변경, 그에따른 가격변경, 옵션 추가 삭제 변경 기능 구현
                장바구니에 추가한 시간순으로 정렬(제일 최근에 담은 아이템이 맨 위에 보이도록)
 */

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
@Slf4j
public class CartController {

    private final CartService cartService;
    private final AuthService authService;

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

}
