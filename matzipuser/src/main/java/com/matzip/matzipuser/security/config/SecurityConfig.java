package com.matzip.matzipuser.security.config;

import com.matzip.matzipuser.security.filter.CustomAuthenticationFilter;
import com.matzip.matzipuser.security.filter.JwtFilter;
import com.matzip.matzipuser.security.handler.JwtAccessDeniedHandler;
import com.matzip.matzipuser.security.handler.JwtAuthenticationEntryPoint;
import com.matzip.matzipuser.security.handler.LoginFailureHandler;
import com.matzip.matzipuser.security.handler.LoginSuccessHandler;
import com.matzip.matzipuser.security.util.CustomUserDetailService;
import com.matzip.matzipuser.security.util.JwtUtil;
import com.matzip.matzipuser.users.query.service.UsersInfoService;
import jakarta.servlet.Filter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final BCryptPasswordEncoder passwordEncoder;
    private final CustomUserDetailService userDetailService;
    private final UsersInfoService usersInfoService;
    private final Environment env;
    private final JwtUtil jwtUtil;

    @Bean
    protected SecurityFilterChain configure(HttpSecurity http) throws Exception {
        /* CSRF 토큰 발행 시 Client 에서 매번 해당 토큰도 함께 넘겨주어야 하므로 기능 비 활성화 */
        http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auths -> {
                    // 회원가입 Post 는 인증 필요 없음
                    auths.requestMatchers(
                            new AntPathRequestMatcher("/user/api/v1/auth/register"),
//                            new AntPathRequestMatcher("/user/api/v1/**"),
//                            new AntPathRequestMatcher("/**"),
                            new AntPathRequestMatcher("/swagger-ui/index.html"),
                            new AntPathRequestMatcher("/swagger-ui/**"),
                            new AntPathRequestMatcher("/webjars/swagger-ui/**"),
                            new AntPathRequestMatcher("/v3/api-docs/**"),
                            new AntPathRequestMatcher("/user/v3/api-docs")
                            ).permitAll();

                    allAuthConnection(auths);
                    userAuthConnection(auths);
                    adminAuthConnection(auths);

                    auths.anyRequest().authenticated();

                })
                /* session 로그인 방식을 사용하지 않음 (JWT Token 방식을 사용할 예정) */
                .sessionManagement(
                        session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                );



        /* 커스텀 로그인 필터 이전에 JWT 토큰 확인 필터를 설정 */
        http.addFilterBefore(new JwtFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class);

        /* 커스텀 로그인 필터 추가 */
        http.addFilterBefore(getAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        /* 인증, 인가 실패 핸들러 설정 */
        http.exceptionHandling(
                exceptionHandling -> {
                    exceptionHandling.accessDeniedHandler(new JwtAccessDeniedHandler());
                    exceptionHandling.authenticationEntryPoint(new JwtAuthenticationEntryPoint());
                }
        );



        return http.build();

    }


    // 관리자 접근 url
    private void adminAuthConnection(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry auths) {
        auths.requestMatchers(
                new AntPathRequestMatcher("/user/api/v1/active-level", "POST"),
                new AntPathRequestMatcher("/user/api/v1/active-level", "PUT"),
                new AntPathRequestMatcher("/user/api/v1/active-level/{activeLevelSeq}", "DELETE"),
                new AntPathRequestMatcher("/user/api/v1/active-level", "GET"),
                new AntPathRequestMatcher("/user/api/v1/users/activity", "GET"),
                new AntPathRequestMatcher("/user/api/v1/users/list", "GET"),
                new AntPathRequestMatcher("/user/api/v1/users", "GET"),
                new AntPathRequestMatcher("/user/api/v1/user/{userSeq}", "GET"),
                new AntPathRequestMatcher("/user/api/v1/user/email", "GET"),
                new AntPathRequestMatcher("/user/api/v1/user/userseq", "GET"),
                new AntPathRequestMatcher("/user/api/v1/active-level-count", "GET")
        ).hasAuthority("admin");
    }

    // 회원 접근 url
    private void userAuthConnection(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry auths) {
        auths.requestMatchers(
                new AntPathRequestMatcher("/user/api/v1/user-activity/point", "PUT"),
                new AntPathRequestMatcher("/user/api/v1/auth/logout", "POST"),
                new AntPathRequestMatcher("/user/api/v1/follow", "POST"),
                new AntPathRequestMatcher("/user/api/v1/user/{userSeq}", "PUT"),
                new AntPathRequestMatcher("/user/api/v1/user/{userSeq}", "DELETE"),
                new AntPathRequestMatcher("/user/api/v1/user/userStatus/{userSeq}", "PUT"),
                new AntPathRequestMatcher("/user/api/v1/follow", "GET"),
                new AntPathRequestMatcher("/user/api/v1/follower", "GET" ),
                new AntPathRequestMatcher("/user/api/v1/users", "GET"),
                new AntPathRequestMatcher("/user/api/v1/user/{userSeq}", "GET"),
                new AntPathRequestMatcher("/user/api/v1/user/email", "GET"),
                new AntPathRequestMatcher("/user/api/v1/user/userseq", "GET")
        ).hasAuthority("user");
    }

    // 모든 접근 url
    private void allAuthConnection(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry auths) {
        auths.requestMatchers(
                new AntPathRequestMatcher("/user/api/v1/auth/mai-verification", "POST"),
                new AntPathRequestMatcher("/user/api/v1/auth/chkEmailCode", "POST"),
                new AntPathRequestMatcher("/user/api/v1/auth/register", "POST"),
                new AntPathRequestMatcher("/user/api/v1/auth/find-email", "POST"),
                new AntPathRequestMatcher("/user/api/v1/auth/send-pw-reset-url", "POST"),
                new AntPathRequestMatcher("/user/api/v1/auth/reset-password", "POST")
        ).permitAll();
    }

    private Filter getAuthenticationFilter() {
        CustomAuthenticationFilter customAuthenticationFilter = new CustomAuthenticationFilter();
        customAuthenticationFilter.setAuthenticationManager(getAuthenticationManager());
        customAuthenticationFilter.setAuthenticationSuccessHandler(new LoginSuccessHandler(env, usersInfoService));
        customAuthenticationFilter.setAuthenticationFailureHandler(new LoginFailureHandler());

        return customAuthenticationFilter;

    }

    private AuthenticationManager getAuthenticationManager() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder);
        provider.setUserDetailsService(userDetailService);
        return new ProviderManager(provider);
    }

    @Bean
    public CorsFilter corsFilter() {
        return new CorsFilter(corsConfigurationSource());
    }

    @Bean
    public UrlBasedCorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("http://localhost:5173"); // 허용할 도메인
        config.addAllowedOrigin("http://localhost:8000");
        config.addAllowedHeader("*"); // 모든 헤더 허용
        config.addAllowedMethod("*"); // 모든 HTTP 메소드 허용
        config.addExposedHeader("token"); // 서버측에서 보내는 헤더에 대한 허용 설정

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
