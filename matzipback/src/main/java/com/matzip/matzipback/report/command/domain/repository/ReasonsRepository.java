package com.matzip.matzipback.report.command.domain.repository;

import com.matzip.matzipback.report.command.domain.aggregate.Reasons;

import java.util.List;
import java.util.Optional;

public interface ReasonsRepository {

    Reasons save(Reasons reasons);

    Optional<Reasons> findById(Long reasonSeq);

    void deleteAllByReasonSeqIn(List<Long> reasonSeqs);
}
