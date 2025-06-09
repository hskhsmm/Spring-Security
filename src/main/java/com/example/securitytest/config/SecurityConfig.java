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

    // Spring Security는 사용자 인증 시 입력한 비밀번호를 DB에 저장된 암호화된 비밀번호와 비교함.
    // 이때 동일한 해시 알고리즘으로 암호화해야 비교가 가능하므로 비밀번호를 BCrypt 방식으로 암호화하고 검증하기 위해 이 Bean을 등록함.
    // 회원 가입 시: raw password → encode() → 암호화된 문자열 저장
    // 로그인 시: 입력된 비밀번호(raw) vs 저장된 비밀번호(encoded) → matches()로 비교

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .authorizeHttpRequests((auth) -> auth
                        // 누구나 접근 허용: 루트(/)와 로그인 페이지(/login)
                        .requestMatchers("/", "/login", "/loginProc", "/join", "/joinProc").permitAll()
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

                        // 로그인 성공 시 리다이렉트할 URL 설정
                        .defaultSuccessUrl("/", true)

                        // 로그인 실패 시 리다이렉트할 URL 설정
                        .failureUrl("/login?error=true")

                        // 로그인 관련 리소스는 모두 인증 없이 접근 가능하도록 허용
                        .permitAll()
                );

        http
                .sessionManagement((auth) -> auth
                        .maximumSessions(1) // 하나의 아이디에 대해 다중 로그인 허용 개수
                        .maxSessionsPreventsLogin(true) // 초과할 경우 새로운 로그인 차단
                );

        http
                .sessionManagement((auth) -> auth
                        .sessionFixation().changeSessionId()
                );

        http
                .logout((auth) -> auth
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/")
                        .invalidateHttpSession(true) // 세션 무효화
                        .deleteCookies("JSESSIONID") // 쿠키 삭제
                );

        // CSRF 보호 활성화 (보안 강화)
        http.csrf(csrf -> csrf
                .ignoringRequestMatchers("/h2-console/**") // H2 콘솔 사용 시에만 추가
        );

        return http.build(); // 최종 SecurityFilterChain 객체 생성
    }
}