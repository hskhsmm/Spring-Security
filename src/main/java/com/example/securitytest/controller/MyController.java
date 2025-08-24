package com.example.securitytest.controller;

import com.example.securitytest.entity.UserEntity;
import com.example.securitytest.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@Controller
@RequestMapping("/my")
@RequiredArgsConstructor
public class MyController {

    private final UserRepository userRepository;

    @GetMapping("/profile")
    public String profilePage(Model model) {

        // 현재 로그인한 사용자 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        //  Optional 사용으로 null 안전성 확보
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> {
                    log.error("인증된 사용자를 DB에서 찾을 수 없음: {}", username);
                    return new IllegalStateException("사용자 정보를 찾을 수 없습니다");
                });

        //  Stream API로 더 안전한 권한 처리
        String role = authentication.getAuthorities().stream()
                .findFirst()
                .map(grantedAuthority -> grantedAuthority.getAuthority())
                .orElse("ROLE_USER");

        // 현재 시간 (마지막 접속 시간으로 사용)
        String currentTime = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        // 모델에 사용자 정보 추가
        model.addAttribute("user", user);
        model.addAttribute("role", role);
        model.addAttribute("lastAccess", currentTime);
        model.addAttribute("isAdmin", "ROLE_ADMIN".equals(role));

        log.debug("프로필 페이지 접근: {}, 권한: {}", username, role);

        return "my/profile";
    }
}