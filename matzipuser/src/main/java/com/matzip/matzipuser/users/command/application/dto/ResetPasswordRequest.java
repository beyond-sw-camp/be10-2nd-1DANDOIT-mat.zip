package com.matzip.matzipuser.users.command.application.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ResetPasswordRequest {

    private String userPassword;
}
