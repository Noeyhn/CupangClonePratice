package com.github.cupangclone.service;

import com.github.cupangclone.repository.items.Items;
import com.github.cupangclone.repository.items.ItemsRepository;
import com.github.cupangclone.repository.userPrincipal.UserPrincipal;
import com.github.cupangclone.repository.userPrincipal.UserPrincipalRepository;
import com.github.cupangclone.web.dto.items.ItemsResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemService {

    private final ItemsRepository itemsRepository;
    private final UserPrincipalRepository userPrincipalRepository;

    public Page<ItemsResponse> getAllItems(int page, int size) {

        PageRequest pageRequest = PageRequest.of(page, size);

        Page<Items> pageItems = itemsRepository.findAll(pageRequest);

        return pageItems
                .map(items
                        -> ItemsResponse.fromItem(items, items.getUserPrincipal()));

    }

    public ItemsResponse getItemsByName(Long itemId) {

        Optional<Items> itemById = itemsRepository.findById(itemId);

        if (itemById.isPresent()) {

            UserPrincipal user = itemById.get().getUserPrincipal();

            return ItemsResponse.fromItem(itemById.get(), user);

        } else {
            return null;
        }
    }

}
