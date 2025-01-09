package com.github.cupangclone.service;

import com.github.cupangclone.repository.carts.Carts;
import com.github.cupangclone.repository.carts.CartsRepository;
import com.github.cupangclone.repository.itemOption.ItemOption;
import com.github.cupangclone.repository.itemOption.ItemOptionRepository;
import com.github.cupangclone.repository.items.Items;
import com.github.cupangclone.repository.items.ItemsRepository;
import com.github.cupangclone.repository.userPrincipal.UserPrincipal;
import com.github.cupangclone.repository.userPrincipal.UserPrincipalRepository;
import com.github.cupangclone.web.dto.carts.CartRequest;
import com.github.cupangclone.web.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CartService {

    private final UserPrincipalRepository userPrincipalRepository;
    private final ItemsRepository itemsRepository;
    private final CartsRepository cartsRepository;
    private final ItemOptionRepository itemOptionRepository;

    @Transactional(transactionManager = "tmJpa1")
    public String addItem(String email, CartRequest cartRequest) {

        UserPrincipal user = userPrincipalRepository.findByEmail(email)
                .orElseThrow( () -> new NotFoundException("고객정보를 찾을 수 없습니다."));

        Items item = itemsRepository.findById(cartRequest.getItemId())
                .orElseThrow( () -> new NotFoundException("상품정보를 찾을 수 없습니다."));

        Optional<Carts> existingCart = cartsRepository.findByUserPrincipal_UserPrincipalIdAndItems_itemIdAndItemOption_ItemOptionId(user.getUserPrincipalId(), item.getItemId(), cartRequest.getItemOptionId());

        if ( existingCart.isPresent() ) {

            Carts cart = existingCart.get();
            Long itemQuantity = cart.getItemQuantity();
            cart.setItemQuantity(itemQuantity + cartRequest.getItemQuantity());
            cartsRepository.save(cart);

        } else {

            ItemOption getItemOption;

            if ( cartRequest.getItemOptionId() != null) {
                getItemOption = itemOptionRepository.findById(cartRequest.getItemOptionId())
                        .orElseThrow( () -> new NotFoundException("상품옵션 정보를 찾을 수 없습니다."));
            } else {
                getItemOption = null;
            }

            Carts cart = Carts.builder()
                    .items(item)
                    .itemOption(getItemOption)
                    .userPrincipal(user)
                    .itemQuantity(cartRequest.getItemQuantity())
                    .totalPrice(cartRequest.getTotalPrice())
                    .build();
            cartsRepository.save(cart);

        }

        return "아이템이 장바구니에 추가되었습니다.";
    }

}
