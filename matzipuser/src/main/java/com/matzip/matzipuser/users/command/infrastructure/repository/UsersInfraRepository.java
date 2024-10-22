package com.matzip.matzipuser.users.command.infrastructure.repository;

import com.matzip.matzipuser.users.command.domain.aggregate.Users;
import com.matzip.matzipuser.users.command.domain.repository.UsersDomainRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

public interface UsersInfraRepository extends JpaRepository<Users, Long>, UsersDomainRepository {

    @Modifying
    @Transactional
    @Query("UPDATE Users u SET u.pwResetToken = null, u.pwTokenDueTime = null WHERE u.pwTokenDueTime < :currentTime")
    void clearExpiredTokens(LocalDateTime currentTime);
}
