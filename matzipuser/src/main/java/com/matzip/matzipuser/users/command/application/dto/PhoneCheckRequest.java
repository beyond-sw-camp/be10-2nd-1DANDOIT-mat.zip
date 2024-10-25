package com.matzip.matzipuser.users.command.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PhoneCheckRequest {
    @NotBlank(message = "휴대폰 번호는 필수 입력 사항입니다.")
    @Pattern(regexp = "^[0-9]{10,11}$", message = "휴대폰 번호는 10자리 또는 11자리 숫자여야 합니다.")
    public String userPhone;
}
