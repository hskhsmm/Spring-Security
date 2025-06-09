package com.example.securitytest.controller;

import com.example.securitytest.entity.UserEntity;
import com.example.securitytest.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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

        // 데이터베이스에서 사용자 정보 조회
        UserEntity user = userRepository.findByUsername(username);

        if (user == null) {
            // 사용자를 찾을 수 없는 경우 로그인 페이지로 리다이렉트
            return "redirect:/login";
        }

        // 권한 정보 가져오기
        String role = authentication.getAuthorities().iterator().next().getAuthority();

        // 현재 시간 (마지막 접속 시간으로 사용)
        String currentTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        // 모델에 사용자 정보 추가
        model.addAttribute("user", user);
        model.addAttribute("role", role);
        model.addAttribute("lastAccess", currentTime);
        model.addAttribute("isAdmin", "ROLE_ADMIN".equals(role));

        return "my/profile";
    }
}