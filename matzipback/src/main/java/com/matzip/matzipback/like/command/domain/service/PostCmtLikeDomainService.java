package com.matzip.matzipback.like.command.domain.service;

import com.matzip.matzipback.like.command.domain.aggregate.Like;
import com.matzip.matzipback.like.command.domain.repository.PostCmtLikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostCmtLikeDomainService {

    private final PostCmtLikeRepository postCmtLikeRepository;

    // 해당 게시글 댓글에 대해 좋아요를 했는지 조회
    public Optional<Like> findLikeByUserSeqAndPostCommentSeq(long likeUserSeq, Long postCommentSeq) {
        return postCmtLikeRepository.findByLikeUserSeqAndPostCommentSeq(likeUserSeq, postCommentSeq);
    }

    // 좋아요 취소
    public void delete(Like foundPostCmtLike) {
        postCmtLikeRepository.delete(foundPostCmtLike);
    }

    // 좋아요 저장
    public Like save(Like newPostCmtLike) {
        return postCmtLikeRepository.save(newPostCmtLike);
    }
}
