package com.matzip.matzipuser.users.command.application.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class FindPasswordRequest {

    @NotBlank(message = "이메일은 필수 입력 항목입니다.")
    private String userEmail;

    @NotBlank(message = "휴대폰 번호는 필수 입력 항목입니다.")
    private String userPhone;

}
