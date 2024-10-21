package com.matzip.matzipuser.users.command.application.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class FindEmailRequest {

    @NotBlank(message = "이름은 필수 입력 항목입니다.")
    private String userName;

    @NotBlank(message = "휴대폰 번호는 필수 입력 항목입니다.")
    private String userPhone;

}
