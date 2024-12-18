package com.github.cupangclone.service;

import com.github.cupangclone.repository.items.Items;
import com.github.cupangclone.repository.items.ItemsRepository;
import com.github.cupangclone.repository.userPrincipal.UserPrincipal;
import com.github.cupangclone.repository.userPrincipal.UserPrincipalRepository;
import com.github.cupangclone.repository.userPrincipalRoles.UserPrincipalRoles;
import com.github.cupangclone.repository.userPrincipalRoles.UserPrincipalRolesRepository;
import com.github.cupangclone.web.dto.items.ItemsRequest;
import com.github.cupangclone.web.dto.items.ItemsResponse;
import com.github.cupangclone.web.exceptions.NotFoundException;
import com.github.cupangclone.web.exceptions.NotFoundResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SellItemService {

    private final UserPrincipalRepository userPrincipalRepository;
    private final UserPrincipalRolesRepository userPrincipalRolesRepository;
    private final ItemsRepository itemsRepository;


    @Transactional(transactionManager = "tmJpa1")
    public String registerItems(String email, ItemsRequest itemsRequest) {

        UserPrincipal user = userPrincipalRepository.findByEmail(email)
                .orElseThrow( () -> new NotFoundException("유저를 찾을 수 없습니다.") );
        UserPrincipalRoles userRoles = userPrincipalRolesRepository.findById(user.getUserPrincipalId())
                .orElseThrow( () -> new NotFoundException("유저 정보에 오류가 있습니다.") );

        Items items = Items.builder()
                .userPrincipal(user)
                .itemName(itemsRequest.getItemName())
                .itemExplain(itemsRequest.getItemExplain())
                .itemPrice(itemsRequest.getItemPrice())
                .itemStock(itemsRequest.getItemStock())
                .build();

        itemsRepository.save(items);

        return "성공적으로 판매 물품을 등록하였습니다.";
    }

    public List<ItemsResponse> getAllSellItems(String email) {

        UserPrincipal user = userPrincipalRepository.findByEmail(email)
                .orElseThrow( () -> new NotFoundException("유저를 찾을 수 없습니다.") );
        UserPrincipalRoles userRoles = userPrincipalRolesRepository.findById(user.getUserPrincipalId())
                .orElseThrow( () -> new NotFoundException("유저 정보에 오류가 있습니다.") );

        List<Items> foundItemsById = itemsRepository.findByUserPrincipalId(user.getUserPrincipalId());

        return foundItemsById
                .stream()
                .map( item -> ItemsResponse.fromItem(item, user))
                .toList();


    }

    public boolean changeItemStock(String email, Long itemId, Long stock) {

        UserPrincipal user = userPrincipalRepository.findByEmail(email)
                .orElseThrow( () -> new NotFoundException("유저를 찾을 수 없습니다.") );
        UserPrincipalRoles userRoles = userPrincipalRolesRepository.findById(user.getUserPrincipalId())
                .orElseThrow( () -> new NotFoundException("유저 정보에 오류가 있습니다.") );

        Optional<Items> item = itemsRepository.findById(itemId);
        if ( item.isPresent() ) {
            Items itemEntity = item.get();
            itemEntity.setItemStock(stock);
            itemsRepository.save(itemEntity);
            return true;
        } else {
            return false;
        }

    }
}
