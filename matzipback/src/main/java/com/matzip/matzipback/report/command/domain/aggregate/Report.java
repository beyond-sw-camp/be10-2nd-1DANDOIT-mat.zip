package com.matzip.matzipback.report.command.domain.aggregate;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "report")
@Getter
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reportSeq;

    @Column(nullable = false)
    private Long reporterUserSeq;
    @Column(nullable = false)
    private Long reportedUserSeq;
    private Long penaltySeq;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime reportTime;
    private String reportContent;
    private LocalDateTime reportFinishedTime;
    @Column(nullable = false)
    @ColumnDefault("wait") // 기본값 지정
    private String reportStatus = "wait"; // wait, none, penalty

    private Long postSeq;
    private Long postCommentSeq;
    private Long listSeq;
    private Long listCommentSeq;
    private Long messageSeq;
    private Long reviewSeq;

    public void updateReportDetails(Long penaltySeq) {
        this.penaltySeq = penaltySeq;
        this.reportFinishedTime = LocalDateTime.now();
        this.reportStatus = "penalty";
    }

}
