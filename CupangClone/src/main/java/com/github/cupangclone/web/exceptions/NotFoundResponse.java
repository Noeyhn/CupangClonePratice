package com.github.cupangclone.web.exceptions;

import com.github.cupangclone.web.exceptions.responMessage.Message;
import com.github.cupangclone.web.exceptions.responMessage.StatusEnum;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.nio.charset.StandardCharsets;

public class NotFoundResponse {
    public ResponseEntity<Message> sendMessage(Object data) {

        Message message = new Message();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));

        message.setStatus(StatusEnum.NOT_FOUND);
        message.setMessage("NotFoundException");
        message.setData(data);

        return new ResponseEntity<>(message, headers, HttpStatus.NOT_FOUND);

    }
}
