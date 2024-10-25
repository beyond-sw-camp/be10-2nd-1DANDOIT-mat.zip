package com.matzip.matzipuser.users.command.application.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmailSendRequest {
    @NotBlank(message = "이메일은 필수 입력사항입니다.")
    @Email(message = "유효한 이메일 주소를 입력해주세요.")
    private String userEmail;
    @NotBlank(message = "이름은 필수 입력 사항입니다.")
    private String userName;
}
