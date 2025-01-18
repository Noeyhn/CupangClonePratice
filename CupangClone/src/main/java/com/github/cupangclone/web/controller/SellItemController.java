package com.github.cupangclone.web.controller;

import com.github.cupangclone.service.AuthService;
import com.github.cupangclone.service.SellItemService;
import com.github.cupangclone.web.dto.items.ItemsRequest;
import com.github.cupangclone.web.dto.items.ItemsResponse;
import com.github.cupangclone.web.exceptions.NotAcceptException;
import com.github.cupangclone.web.exceptions.NotAcceptResponse;
import com.github.cupangclone.web.exceptions.SuccessResponse;
import com.github.cupangclone.web.exceptions.responMessage.Message;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/*
    TODO LIST : 판매 아이템 로직 수정

 */

@RestController
@RequestMapping("/api/sell_items")
@RequiredArgsConstructor
public class SellItemController {

    private final SellItemService sellItemService;
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<Message> registerItemsForSale(@RequestBody ItemsRequest itemsRequest, HttpServletRequest request, HttpServletResponse response) {
        String email = authService.blockAccessWithOnlyToken(request);

        if ( email != null) {
            if ( sellItemService.registerItems(email, itemsRequest) ) {
                return new SuccessResponse().getMessageResponseEntity("성공적으로 판매물품을 등록하였습니다.");
            } else {
                return new NotAcceptResponse().sendMessage("물픔 등록 권한이 없습니다.");
            }
        } else {
            return new NotAcceptResponse().sendMessage("잘못된 접근입니다.");
        }
    }

    @GetMapping("/all")
    public List<ItemsResponse> getAllItemsBySeller(HttpServletRequest request, HttpServletResponse response) {
        String email = authService.blockAccessWithOnlyToken(request);

        if ( email != null) {
            return sellItemService.getAllSellItems(email);
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            throw new NotAcceptException("잘못된 접근입니다.");
        }
    }

    @PutMapping("/stock_change")
    public String updateItemStock(@RequestParam("item_id") Long itemId, @RequestParam("stock") Long stock, HttpServletRequest request, HttpServletResponse response){
        String email = authService.blockAccessWithOnlyToken(request);

        if ( email != null) {
            boolean isSuccessChange = sellItemService.changeItemStock(email, itemId, stock);
            if ( isSuccessChange ) {
                return "성공적으로 수정이 완료되었습니다.";
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                return "잘못된 접근이거나 아이템을 찾을 수 없습니다.";
            }
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            throw new NotAcceptException("잘못된 접근입니다.");
        }

    }

}
