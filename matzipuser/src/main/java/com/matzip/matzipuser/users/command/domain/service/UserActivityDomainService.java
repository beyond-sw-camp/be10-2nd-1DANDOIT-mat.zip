package com.matzip.matzipuser.users.command.domain.service;

import com.matzip.matzipuser.users.command.application.dto.UpdateUserActivityPointDTO;
import com.matzip.matzipuser.users.command.domain.aggregate.ActiveLevel;
import com.matzip.matzipuser.users.command.domain.aggregate.UserActivity;
import com.matzip.matzipuser.users.command.domain.repository.UserActivityRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
public class UserActivityDomainService {

    private final UserActivityRepository userActivityRepository;
    private final ActiveLevelDomainService activeLevelDomainService;
    private final ModelMapper modelMapper;

    // 유저 활동 포인트를 업데이트 하는 메서드
    @Transactional
    public void updateUserActivityPoint(UpdateUserActivityPointDTO updateUserActivityPointDTO) {

        UserActivity foundUserActivity = userActivityRepository
                .findById(updateUserActivityPointDTO.getActivityUserSeq())
                .orElse(null);

        if (foundUserActivity == null) {
            foundUserActivity = saveUserActivity(updateUserActivityPointDTO);
            foundUserActivity.resetActivityPoint();
        }

        foundUserActivity.changePoint(updateUserActivityPointDTO.getActivityPoint());

        updateUserActivityLevel(foundUserActivity);

    }

    // 유저 활동 정보 만들기
    public UserActivity saveUserActivity(UpdateUserActivityPointDTO updateUserActivityPointDTO) {
        UserActivity userActivity = modelMapper.map(updateUserActivityPointDTO, UserActivity.class);
        return userActivityRepository.save(userActivity);
    }


    // 유저 회원 등급 조정
    public void updateUserActivityLevel(UserActivity userActivity) {

        long activeLevelSeq = calculateLevel(userActivity.getActivityPoint());
        userActivity.changeLevel(activeLevelSeq);
    }

    // 유저 회원 등급 조정
    private long calculateLevel(int point) {
        List<ActiveLevel> activeLevelList = activeLevelDomainService.getAllActiveLevel();
        // activeLevelStandard 기준으로 내림차순 정렬
        activeLevelList.sort((level1, level2) -> level2.getActiveLevelStandard() - level1.getActiveLevelStandard());

        for (ActiveLevel level : activeLevelList) {
            if (point >= level.getActiveLevelStandard()) {
                return level.getActiveLevelSeq();
            }
        }

        return 1;
    }
}
