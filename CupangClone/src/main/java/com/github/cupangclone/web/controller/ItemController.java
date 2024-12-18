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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/items")
@Slf4j
public class ItemController {
    private final ItemService itemService;
    private final AuthService authService;

    @GetMapping("/all")
    public ResponseEntity<Message> requestAllItems(HttpServletRequest request, HttpServletResponse response) {


        try {

            String email = authService.blockAccessWithOnlyToken(request);

            List<ItemsResponse> allItems = itemService.getAllItems();
            return new SuccessResponse().getMessageResponseEntity(allItems);

        } catch (Exception e){
            return new NotAcceptResponse().sendMessage("잘못된 접근입니다.");
        }

    }

    @GetMapping("/search")
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
