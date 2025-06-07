package com.example.securitytest.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Collection;
import java.util.Iterator;

@Controller
public class MainController {

    @GetMapping("/")
    public String mainP(Model model) {

        // 현재 로그인한 사용자의 인증 정보를 SecurityContextHolder로부터 가져옴
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // 인증되지 않은 사용자(익명 사용자)인지 확인
        if (authentication == null || !authentication.isAuthenticated() ||
                "anonymousUser".equals(authentication.getName())) {
            model.addAttribute("id", "Guest");
            model.addAttribute("role", "ANONYMOUS");
            return "main";
        }

        // 로그인한 사용자의 username(ID)를 가져옴
        String id = authentication.getName();

        // authentication.getAuthorities()는 현재 사용자의 권한 목록(Role)을 Collection 형태로 반환
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        String role = "USER"; // 기본값 설정

        if (!authorities.isEmpty()) {
            Iterator<? extends GrantedAuthority> iter = authorities.iterator();
            GrantedAuthority auth = iter.next();
            role = auth.getAuthority();
        }

        model.addAttribute("id", id); // 실제 사용자 ID 사용 (하드코딩 제거)
        model.addAttribute("role", role);

        return "main";
    }
}