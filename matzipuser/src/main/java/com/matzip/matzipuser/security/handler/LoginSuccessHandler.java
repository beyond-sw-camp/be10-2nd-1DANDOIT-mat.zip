package com.matzip.matzipuser.security.handler;

import com.matzip.matzipuser.users.query.dto.userInfo.UserStatusDTO;
import com.matzip.matzipuser.users.query.service.UsersInfoService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final Environment env;
    private final UsersInfoService usersInfoService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        UserStatusDTO userStatus = usersInfoService.getUserStatus(Long.parseLong(authentication.getName()));
        if (userStatus.getUserDeleteDate() != null) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(
                    "{\"status\": 403, " +
                    "\"error\": \"Forbidden\", " +
                    "\"message\": \"이미 탈퇴한 계정입니다.\"}");
        } else if (userStatus.getUserStatus().equals("inactive")) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(
                    "{\"status\": 403, " +
                    "\"error\": \"Forbidden\", " +
                    "\"message\": \"정지된 계정입니다.\"}");
        } else {
            /* 권한을 꺼내 List<String> 으로 변환 */
            List<String> authorities = authentication.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());

            Claims claims = Jwts.claims().setSubject(authentication.getName());
            claims.put("auth", authorities);

            String token = Jwts.builder()
                    .setClaims(claims) // 본체 넣는 값
                    .setExpiration(new Date((System.currentTimeMillis()) + Long.parseLong(env.getProperty("token.expiration_time")))) // 만료시간
                    .signWith(
                            SignatureAlgorithm.HS512, env.getProperty("token.secret")
                    ) // 서명
                    .compact();
            log.info("token: {}", token);

            response.setHeader("token", token);
        }
    }
}
