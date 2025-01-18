package com.github.cupangclone.web.dto.items;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.github.cupangclone.web.dto.options.OptionRequest;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
//@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ItemsRequest {

    private String itemName;
    private String itemExplain;
    private Long itemPrice;
    private Long itemStock;
    private List<OptionRequest> option;

}
