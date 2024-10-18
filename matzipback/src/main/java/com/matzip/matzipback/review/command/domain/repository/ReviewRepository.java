package com.matzip.matzipback.review.command.domain.repository;

import com.matzip.matzipback.review.command.domain.aggregate.Review;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ReviewRepository {
    Review save(Review review);

    Optional<Review> findById(Long reviewSeq);

    void deleteById(Long reviewSeq);

}
