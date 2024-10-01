package com.matzip.matzipback.users.query.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class MessageDetailResMessageDTO {

    private int code;
    private String message;
    private List<MessageDetailResDTO> messages;
}
