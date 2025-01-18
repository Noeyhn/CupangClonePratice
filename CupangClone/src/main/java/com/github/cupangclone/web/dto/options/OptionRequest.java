package com.github.cupangclone.web.dto.options;

import com.github.cupangclone.repository.options.Options;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class OptionRequest {

    private String optionTypeName;
    private List<String> optionName;
    private List<Long> optionPrice;
    private List<Long> optionStock;

}
