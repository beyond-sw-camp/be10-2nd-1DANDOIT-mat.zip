package com.matzip.matzipback.report.command.application.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateReasonNameDTO {

    @NotBlank(message = "사유 이름은 필수 항목입니다.")
    private String reasonName;

}
