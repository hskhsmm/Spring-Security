package com.example.securitytest.config;

import com.example.securitytest.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Spring Security 설정 클래스
 * - 사용자 인증/인가 처리
 * - 비밀번호 암호화 설정
 * - 세션 관리
 * - CSRF 보호 설정
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity // Spring Security 6.x에서 권장 (컨트롤러 메서드에 @PreAuthorize 등 사용 가능)
@RequiredArgsConstructor // CustomUserDetailsService 자동 주입을 위한 Lombok 어노테이션
public class SecurityConfig {

    // 커스텀 사용자 인증 서비스 (DB에서 사용자 정보 조회)
    private final CustomUserDetailsService customUserDetailsService;


    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 사용자 인증을 처리하는 AuthenticationProvider 설정
     *
     * DaoAuthenticationProvider:
     * - UserDetailsService를 통해 사용자 정보 조회
     * - PasswordEncoder를 통해 비밀번호 검증
     * - 인증 성공 시 Authentication 객체 생성
     *
     * 왜 명시적으로 설정하나?
     * - Spring이 자동으로 해주긴 하지만, 명확한 설정으로 가독성 향상
     * - 커스터마이징이 필요한 경우 쉽게 수정 가능
     * - 여러 UserDetailsService가 있을 때 명확히 지정
     */
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(customUserDetailsService); // 사용자 조회 서비스 설정
        authProvider.setPasswordEncoder(bCryptPasswordEncoder());     // 비밀번호 인코더 설정
        return authProvider;
    }

    /**
     * AuthenticationManager Bean 등록
     *
     * AuthenticationManager의 역할:
     * - 인증 요청을 받아서 AuthenticationProvider들에게 위임
     * - 인증 성공/실패 결과를 반환
     * - 프로그래매틱한 인증이 필요할 때 사용 (예: 자동 로그인)
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    /**
     * Spring Security 메인 설정
     * HTTP 요청에 대한 보안 규칙 정의
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        // 커스텀 AuthenticationProvider 등록
        http.authenticationProvider(authenticationProvider());

        // === URL별 접근 권한 설정 ===
        http.authorizeHttpRequests(auth -> auth
                // 공개 접근 허용
                .requestMatchers("/", "/login", "/loginProc", "/join", "/joinProc").permitAll()

                // 정적 리소스
                .requestMatchers("/css/**", "/js/**", "/images/**", "/favicon.ico").permitAll()

                // 관리자 전용 페이지
                .requestMatchers("/admin/**").hasRole("ADMIN")

                // 👤 로그인한 사용자 전용 (ROLE_ADMIN, ROLE_USER 모두 접근 가능)
                .requestMatchers("/my/**").hasAnyRole("ADMIN", "USER")

                // 🔒 그 외 모든 요청은 인증 필요
                .anyRequest().authenticated()
        );

        // === 로그인 설정 ===
        http.formLogin(form -> form
                .loginPage("/login")              // 커스텀 로그인 페이지
                .loginProcessingUrl("/loginProc") // 로그인 폼 제출 URL (Spring Security가 자동 처리)
                .defaultSuccessUrl("/", true)    // 로그인 성공 시 이동 페이지
                .failureUrl("/login?error=true")  // 로그인 실패 시 이동 페이지
                .permitAll()                      // 로그인 관련 URL은 모두 허용
        );

        // === 로그아웃 설정 ===
        http.logout(logout -> logout
                .logoutUrl("/logout")           // 로그아웃 URL
                .logoutSuccessUrl("/")          // 로그아웃 성공 시 이동 페이지
                .invalidateHttpSession(true)   // 세션 완전 무효화
                .deleteCookies("JSESSIONID")   // 세션 쿠키 삭제
                .permitAll()                   // 로그아웃 URL 접근 허용
        );

        // === 세션 관리 설정 ===
        http.sessionManagement(session -> session
                // 세션 고정 공격 방지 (먼저 설정)
                .sessionFixation().changeSessionId() // 로그인 시 세션 ID 변경
                // 동시 세션 제어
                .maximumSessions(1)                  // 한 사용자당 최대 1개 세션만 허용
                .maxSessionsPreventsLogin(true)      // 초과 시 새 로그인 차단 (false면 기존 세션 만료)
        );

        // === CSRF(Cross-Site Request Forgery) 보호 설정 ===
        http.csrf(csrf -> csrf
                        // H2 콘솔 사용 시 CSRF 비활성화 (개발용)
                        .ignoringRequestMatchers("/h2-console/**")
                // 운영 환경에서는 이 부분 제거하고 모든 요청에 CSRF 토큰 적용 권장
        );

        return http.build();
    }
}