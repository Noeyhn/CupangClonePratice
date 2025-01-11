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
import com.github.cupangclone.web.dto.carts.CartUpdateRequest;
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

    /** TODO : 로그인이 아닌 다른경로로 접근시 본인확인 로직 필요
     * AuthService에 작성? <- 생각해 볼것(하나의 메소드로 작성하여 유지보수 용이하도록 할 것)
     */

    // 고객 정보 확인 로직
    public UserPrincipal findUser(String email) {
        return userPrincipalRepository.findByEmail(email)
                .orElseThrow( () -> new NotFoundException("고객정보를 찾을 수 없습니다."));
    }

    // 장바구니에 아이템 추가
    @Transactional(transactionManager = "tmJpa1")
    public String addItem(String email, CartRequest cartRequest) {

        // 사용자 정보 확인
        UserPrincipal user = findUser(email);

        // 제품 유무 확인
        Items item = itemsRepository.findById(cartRequest.getItemId())
                .orElseThrow( () -> new NotFoundException("상품정보를 찾을 수 없습니다."));

        // 장바구니에 해당 아이템이 존재하는지 확인
        // TODO : 아이템 옵션에 따라 각기 다른 상품정보로 분류 및 확인 요함
        Optional<Carts> existingCart = cartsRepository.findByUserPrincipal_UserPrincipalIdAndItems_itemIdAndItemOption_ItemOptionId(user.getUserPrincipalId(), item.getItemId(), cartRequest.getItemOptionId());

        /** 장바구니에 해당 아이템이 존재하면 수량만 증가
         *  해당 아이템이 없을 경우 장바구니에 추가
         */
        if ( existingCart.isPresent() ) {

            Carts cart = existingCart.get();
            Long itemQuantity = cart.getItemQuantity();
            Long itemPrice = cart.getTotalPrice();
            cart.setItemQuantity(itemQuantity + cartRequest.getItemQuantity());
            cart.setTotalPrice(itemPrice + cartRequest.getTotalPrice());
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

    // 장바구니에 아이템 삭제
    @Transactional(transactionManager = "tmJpa1")
    public String removeItemFromCart(String email, Long cartId) {

        // 고객 확인 로직
        UserPrincipal user = findUser(email);

        // 장바구니 아이템 확인
        Carts cartItem = cartsRepository.findByCartIdAndUserPrincipal_UserPrincipalId(cartId, user.getUserPrincipalId()).orElseThrow( () -> new NotFoundException("장바구니에서 해당 아이템을 찾을 수 없습니다."));

        // 아이템 삭제
        cartsRepository.deleteById(cartId);

        return "아이템이 장바구니에서 삭제되었습니다.";
    }

    // 장바구니 아이템 수량 변경
    @Transactional(transactionManager = "tmJpa1")
    public String updateItemQuantity(String email, CartUpdateRequest cartUpdateRequest) {

        /**
         * 옵션 변경에 따른 로직 필요(수량변경과 합칠지 따로 만들지 고민해보기)
         */

        // 고객 확인 로직
        UserPrincipal user = findUser(email);

        // 장바구니 아이템 확인
        Carts cartItem = cartsRepository.findByCartIdAndUserPrincipal_UserPrincipalId(cartUpdateRequest.getCartId(), user.getUserPrincipalId()).orElseThrow( () -> new NotFoundException("장바구니에서 해당 아이템을 찾을 수 없습니다."));

        // 수량이 0 이하일 경우, 장바구니에서 삭제
        if (cartUpdateRequest.getItemQuantity() <= 0) {
            cartsRepository.deleteById(cartItem.getCartId());
            return "아이템이 장바구니에서 삭제되었습니다.";
        }

        cartItem.setItemQuantity(cartUpdateRequest.getItemQuantity());
        cartItem.setTotalPrice(cartUpdateRequest.getTotalPrice());
        cartsRepository.save(cartItem);

        return "아이템 수량이 변경되었습니다.";
    }

}
