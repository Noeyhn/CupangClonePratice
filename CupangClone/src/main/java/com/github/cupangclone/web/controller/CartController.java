package com.github.cupangclone.web.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
}
