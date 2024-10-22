package com.matzip.matzipuser.users.command.application.service;

import com.matzip.matzipuser.users.command.domain.repository.UsersDomainRepository;
import com.matzip.matzipuser.users.command.infrastructure.repository.UsersInfraRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class CronService {

    private final UsersInfraRepository usersInfraRepository;

    @Scheduled(cron = "0 0 * * * ?") // 매시간 정각에 실행
    public void cleanUpExpiredTokens() {
        usersInfraRepository.clearExpiredTokens(LocalDateTime.now());
        log.info("만료된 토큰을 정리했습니다.");
    }

}
