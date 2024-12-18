package com.github.cupangclone.web.exceptions.responMessage;

import lombok.Data;
import lombok.Setter;
import lombok.ToString;

@Data
@Setter
@ToString
public class Message {

    private StatusEnum status;
    private String message;
    private Object data;

}
