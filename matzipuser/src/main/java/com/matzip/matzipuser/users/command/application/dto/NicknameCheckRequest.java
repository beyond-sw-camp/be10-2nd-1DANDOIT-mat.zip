package com.matzip.matzipuser.users.command.application.dto;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class NicknameCheckRequest {
    @Size(min = 2, max = 16, message = "닉네임은 2자 이상, 16자 이하로 입력해주세요.")
    public String userNickname;
}
