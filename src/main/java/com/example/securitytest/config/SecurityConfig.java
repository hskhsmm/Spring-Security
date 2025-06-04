package com.example.securitytest.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

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

        return http.build(); // 최종 SecurityFilterChain 객체 생성


    }
}
