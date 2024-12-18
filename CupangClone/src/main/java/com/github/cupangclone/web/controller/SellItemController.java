package com.github.cupangclone.web.controller;

import com.github.cupangclone.service.AuthService;
import com.github.cupangclone.service.SellItemService;
import com.github.cupangclone.web.dto.items.ItemsRequest;
import com.github.cupangclone.web.dto.items.ItemsResponse;
import com.github.cupangclone.web.exceptions.NotAcceptException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sell_items")
@RequiredArgsConstructor
public class SellItemController {

    private final SellItemService sellItemService;
    private final AuthService authService;

    @PostMapping("/register")
    public String registerItemsForSale(@RequestBody ItemsRequest itemsRequest, HttpServletRequest request, HttpServletResponse response) {
        String email = authService.blockAccessWithOnlyToken(request);

        if ( email != null) {
            return sellItemService.registerItems(email, itemsRequest);
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            throw new NotAcceptException("잘못된 접근입니다.");
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

    @PutMapping("/change")
    public String updateItemStock(@RequestParam("item_id") Long itemId, @RequestParam("stock") Long stock, HttpServletRequest request, HttpServletResponse response){
        String email = authService.blockAccessWithOnlyToken(request);

        if ( email != null) {
            boolean isSuccessChange = sellItemService.changeItemStock(email, itemId, stock);
            if ( isSuccessChange ) {
                return "성공적으로 수정이 완료되었습니다.";
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                return "아이템을 찾을 수 없습니다.";
            }
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            throw new NotAcceptException("잘못된 접근입니다.");
        }

    }

}
