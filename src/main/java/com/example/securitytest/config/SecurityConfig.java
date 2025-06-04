package com.example.securitytest.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {

        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .authorizeHttpRequests((auth) -> auth
                        // 누구나 접근 허용: 루트(/)와 로그인 페이지(/login)
                        .requestMatchers("/", "/login").permitAll()

                        // "/admin" 경로는 "ADMIN" 역할만 접근 가능
                        .requestMatchers("/admin").hasRole("ADMIN")

                        // "/my/**" 하위 경로는 "ADMIN" 또는 "USER" 역할만 접근 가능
                        .requestMatchers("/my/**").hasAnyRole("ADMIN", "USER")

                        // 위에 명시되지 않은 모든 요청은 인증된 사용자만 접근 가능
                        .anyRequest().authenticated()
                );

        http
                .formLogin((auth) -> auth
                        // 사용자 정의 로그인 페이지 경로 설정 (GET /login 요청을 처리할 HTML 페이지 필요)
                        .loginPage("/login")

                        // 로그인 요청을 처리할 URL (POST /loginProc 요청이 들어오면 Spring Security가 자동 처리)
                        .loginProcessingUrl("/loginProc")

                        // 로그인 관련 리소스는 모두 인증 없이 접근 가능하도록 허용
                        .permitAll()
                );

        http
                .csrf((auth) ->
                        // CSRF 보호 기능 비활성화
                        // 보통 REST API, 간단한 테스트용 프로젝트에서는 disable() 처리함
                        auth.disable()
                );



        return http.build(); // 최종 SecurityFilterChain 객체 생성


    }
}
