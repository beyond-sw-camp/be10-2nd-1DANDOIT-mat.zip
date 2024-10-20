package com.matzip.matzipback.report.command.domain.aggregate;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "reasons")
@Getter
@NoArgsConstructor (access = AccessLevel.PROTECTED)
public class Reasons {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reasonSeq;

    @Column(nullable = false)
    private String reasonName;

    @Column(nullable = false)
    private int reasonOrder;

    public void updateReasonOrder(int reasonOrder) {
        this.reasonOrder = reasonOrder;
    }

    public void updateReasonName(String reasonName) {
        this.reasonName = reasonName;
    }

}