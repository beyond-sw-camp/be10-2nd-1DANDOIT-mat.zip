package com.matzip.matzipuser.users.query.dto.userInfo;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class UserStatusDTO {
    private String userStatus;
    private LocalDateTime userDeleteDate;
}
