package com.matzip.matzipuser.users.command.application.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateSocialRequest {

    private String userEmail;
    private String userName;
    private String userPhone;

    private String userSocialYn = "Y";  // 소셜회원
    private String userSocialSite;  // google, kakao, naver
    private String socialToken;
}
