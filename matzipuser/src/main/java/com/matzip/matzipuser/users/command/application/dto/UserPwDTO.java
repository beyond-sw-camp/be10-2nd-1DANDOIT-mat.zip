package com.matzip.matzipuser.users.command.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserPwDTO {

    private Long userSeq;
    private String userPassword;
    private String pwResetToken;
    private String pwTokenDueTime;
}
