package com.github.cupangclone.web.controller;

import com.github.cupangclone.service.AuthService;
import com.github.cupangclone.service.ItemService;
import com.github.cupangclone.web.dto.items.ItemsResponse;
import com.github.cupangclone.web.exceptions.NotAcceptException;
import com.github.cupangclone.web.exceptions.NotAcceptResponse;
import com.github.cupangclone.web.exceptions.NotFoundResponse;
import com.github.cupangclone.web.exceptions.SuccessResponse;
import com.github.cupangclone.web.exceptions.responMessage.Message;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/*
    TODO LIST : item 판매량, 등록일자, 가격순 등등 정렬기능 구현
                item option 기능 구현[추가 옵션(선택 자유) 및 메인 옵션(무조건 1택), 옵션 없을 시 단일 품목]
                item 찜 기능 구현
                item 카테고리에 따른 DB 구현[ ex) 대 카테고리 - 중 카테고리 - 소 카테고리 ]
                item 검색 페이지 및 상세페이지는 회원이 아니라도 접근이 가능하도록 api 접근권한 수정
                item 전체 페이지에 관한 데이터도 넘겨주기
*/



@RestController
@RequiredArgsConstructor
@RequestMapping("/api/items")
@Slf4j
public class ItemController {
    private final ItemService itemService;
    private final AuthService authService;

    @GetMapping("/all")
    public ResponseEntity<Message> requestAllItems(
            @RequestParam(defaultValue = "0", name = "page") int page,
            @RequestParam(defaultValue = "5", name = "size")int size,
            HttpServletRequest request) {

        String email = authService.blockAccessWithOnlyToken(request);

        if ( email != null ) {

            Page<ItemsResponse> result = itemService.getAllItems(page, size);
            List<ItemsResponse> allItems = result.getContent();

            return new SuccessResponse().getMessageResponseEntity(allItems);

        } else {
            return new NotAcceptResponse().sendMessage("잘못된 접근입니다.");
        }

    }

    @GetMapping("/found_items")
    public ResponseEntity<Message> requestFoundItems(@RequestParam("item_id") Long itemId, HttpServletRequest request, HttpServletResponse response) {

        String email = authService.blockAccessWithOnlyToken(request);

        if ( email != null) {
            ItemsResponse ir = itemService.getItemsByName(itemId);
            if ( ir != null ) {
                return new SuccessResponse().getMessageResponseEntity(ir);
            } else {
                return new NotFoundResponse().sendMessage("아이템을 찾을 수 없습니다.");
            }
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            throw new NotAcceptException("잘못된 접근입니다.");
        }

    }

}
