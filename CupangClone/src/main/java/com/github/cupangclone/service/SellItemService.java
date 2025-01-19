package com.github.cupangclone.service;

import com.github.cupangclone.repository.itemOption.ItemOption;
import com.github.cupangclone.repository.itemOption.ItemOptionRepository;
import com.github.cupangclone.repository.items.Items;
import com.github.cupangclone.repository.items.ItemsRepository;
import com.github.cupangclone.repository.optionType.OptionType;
import com.github.cupangclone.repository.optionType.OptionTypeRepository;
import com.github.cupangclone.repository.options.Options;
import com.github.cupangclone.repository.options.OptionsRepository;
import com.github.cupangclone.repository.userPrincipal.UserPrincipal;
import com.github.cupangclone.repository.userPrincipal.UserPrincipalRepository;
import com.github.cupangclone.repository.userPrincipalRoles.UserPrincipalRoles;
import com.github.cupangclone.repository.userPrincipalRoles.UserPrincipalRolesRepository;
import com.github.cupangclone.web.dto.items.ItemsRequest;
import com.github.cupangclone.web.dto.items.ItemsResponse;
import com.github.cupangclone.web.dto.options.OptionRequest;
import com.github.cupangclone.web.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class SellItemService {

    private final UserPrincipalRepository userPrincipalRepository;
    private final UserPrincipalRolesRepository userPrincipalRolesRepository;
    private final ItemsRepository itemsRepository;
    private final OptionTypeRepository optionTypeRepository;
    private final OptionsRepository optionsRepository;
    private final ItemOptionRepository itemOptionRepository;


    @Transactional(transactionManager = "tmJpa1")
    public boolean registerItems(String email, ItemsRequest itemsRequest) {

        UserPrincipal user = userPrincipalRepository.findByEmail(email)
                .orElseThrow( () -> new NotFoundException("유저를 찾을 수 없습니다.") );
        UserPrincipalRoles userRoles = userPrincipalRolesRepository.findById(user.getUserPrincipalId())
                .orElseThrow( () -> new NotFoundException("유저 정보에 오류가 있습니다.") );

        if (Objects.equals(userRoles.getRoles().getRoleName(), "ROLE_SELLER")) {

            Items items = Items.builder()
                    .userPrincipal(user)
                    .itemName(itemsRequest.getItemName())
                    .itemExplain(itemsRequest.getItemExplain())
                    .itemPrice(itemsRequest.getItemPrice())
                    .itemStock(itemsRequest.getItemStock())
                    .build();

            itemsRepository.save(items);

            /*
                아이템 옵션값이 존재할경우 옵션 DB에 저장

                TODO : 옵션값 DB에 저장시 SQL문을 최소로 불러올 수 있도록 로직 수정방향 생각해보기
                        단계별 옵션 및 옵션추가 확장성에 용이하도록 수정 요함  
             */
            if (itemsRequest.getOption() != null) {

                List<OptionRequest> optionRequests = itemsRequest.getOption().stream().toList();

                List<Options> options = new ArrayList<>();
                List<OptionType> optionTypes = new ArrayList<>();

                for ( OptionRequest optionRequest : optionRequests ) {
                    List<String> optionsNames = optionRequest.getOptionName();
                    List<Long> optionsPrices = optionRequest.getOptionPrice();
                    List<Long> optionsStocks = optionRequest.getOptionStock();

                    OptionType optionType = OptionType.builder()
                            .optionTypeName(optionRequest.getOptionTypeName())
                            .build();

                    optionTypeRepository.save(optionType);

                    OptionType findOptionType = optionTypeRepository.findByOptionTypeName(optionRequest.getOptionTypeName());

                    optionTypes.add(findOptionType);

                    ItemOption itemOption = ItemOption.builder()
                            .optionType(findOptionType)
                            .items(items)
                            .build();

                    itemOptionRepository.save(itemOption);

                    for ( int i = 0; i < optionsNames.size(); i++ ) {

                        Options option = Options.builder()
                                .optionName(optionsNames.get(i))
                                .optionPrice(optionsPrices.get(i))
                                .optionStock(optionsStocks.get(i))
                                .optionType(findOptionType)
                                .build();

                        options.add(option);
                    }

                    optionsRepository.saveAll(options);

                }

            }


            return true;
        } else {
            return false;
        }
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

        // 아이템에 등록된 유저 정보와 토큰으로 받아온 유저 정보 확인
        if ( item.isPresent() && Objects.equals(item.get().getUserPrincipal().getEmail(), email)) {

            Items itemEntity = item.get();
            itemEntity.setItemStock(stock);
            itemsRepository.save(itemEntity);
            return true;

        } else {

            return false;

        }

    }
}
