package com.matzip.matzipuser.users.command.application.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DeleteUserRequest {

    private long userSeq;
    private String userPassword;
}
