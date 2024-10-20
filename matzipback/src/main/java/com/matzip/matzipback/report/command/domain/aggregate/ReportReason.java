package com.matzip.matzipback.report.command.domain.aggregate;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity @Getter
@Table(name = "report_reason")
@NoArgsConstructor/*(access = AccessLevel.PROTECTED)*/
public class ReportReason {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long  reportReasonSeq;

    private Long  reportSeq;
    private Long  reasonSeq;


}
