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

        //로그인한 사용자의 username(ID) 를 가져옴
        String id = SecurityContextHolder.getContext().getAuthentication().getName();

        //현재 로그인한 사용자의 인증 정보를 SecurityContextHolder로부터 가져옴
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();


        //authentication.getAuthorities()는 현재 사용자의 권한 목록(Role)을 Collection 형태로 반환
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iter = authorities.iterator();
        GrantedAuthority auth = iter.next();
        String role = auth.getAuthority();

        model.addAttribute("id", 10); //id 하드코딩
        model.addAttribute("role", role);

        return "main";
    }
}
