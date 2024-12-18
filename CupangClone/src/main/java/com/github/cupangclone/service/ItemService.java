package com.github.cupangclone.service;

import com.github.cupangclone.repository.items.Items;
import com.github.cupangclone.repository.items.ItemsRepository;
import com.github.cupangclone.repository.userPrincipal.UserPrincipal;
import com.github.cupangclone.repository.userPrincipal.UserPrincipalRepository;
import com.github.cupangclone.web.dto.items.ItemsResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemService {

    private final ItemsRepository itemsRepository;
    private final UserPrincipalRepository userPrincipalRepository;

    public List<ItemsResponse> getAllItems() {

        List<Items> foundedAllItems = itemsRepository.findAll();

        return foundedAllItems
                .stream()
                .map(items
                        -> ItemsResponse.fromItem(items, items.getUserPrincipal()))
                .toList();

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
