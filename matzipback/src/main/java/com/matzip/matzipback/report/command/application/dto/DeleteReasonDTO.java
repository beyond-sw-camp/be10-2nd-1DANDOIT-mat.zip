package com.matzip.matzipback.report.command.application.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class DeleteReasonDTO {

    private List<Long> reasonSeqs;

}
