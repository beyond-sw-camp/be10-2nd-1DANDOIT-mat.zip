package com.matzip.matzipuser.users.command.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class UserPwTokenDTO {

    private Long userSeq;
    private String pwResetToken;
    private LocalDateTime pwTokenDueTime;
}
