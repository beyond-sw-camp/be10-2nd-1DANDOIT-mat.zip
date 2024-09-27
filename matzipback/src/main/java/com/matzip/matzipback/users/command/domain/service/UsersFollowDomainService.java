package com.matzip.matzipback.users.command.domain.service;

import com.matzip.matzipback.users.command.domain.aggregate.Follow;
import com.matzip.matzipback.users.command.domain.aggregate.FollowId;
import com.matzip.matzipback.users.command.domain.repository.UsersFollowRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsersFollowDomainService {

    private final UsersFollowRepository usersFollowRepository;

    // 해당 팔로우 관계가 존재하는지 체크함
    public boolean isExistFollow(Follow newFollow) {

        FollowId followId = new FollowId(newFollow.getFollowingUserSeq(), newFollow.getFollowedUserSeq());
        return usersFollowRepository.existsById(followId);
    }

    // 팔로우 취소
    public void deleteFollow(Follow newFollow) {

        FollowId followId = new FollowId(newFollow.getFollowingUserSeq(), newFollow.getFollowedUserSeq());
        usersFollowRepository.deleteById(followId);
    }

    // 팔로우 등록
    public Follow save(Follow newFollow) {

        return usersFollowRepository.save(newFollow);
    }
}