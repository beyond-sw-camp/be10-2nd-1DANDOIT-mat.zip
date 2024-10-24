package com.matzip.matzipuser.users.command.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UpdateUserRequest {

    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[!@#$%^&*.,?])[A-Za-z\\d!@#$%^&*.,?]{8,}$",
            message = "비밀번호는 영문, 숫자, 특수문자를 포함한 8자 이상이어야 합니다.")
    private String userPassword;
    @Pattern(regexp = "^[0-9]{10,11}$", message = "휴대폰 번호는 10자리 또는 11자리 숫자여야 합니다.")
    private String userPhone;
    @Size(min = 2, max = 16, message = "닉네임은 2자 이상, 16자 이하로 입력해주세요.")
    private String userNickname;

}
