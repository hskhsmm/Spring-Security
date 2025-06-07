package com.example.securitytest.controller;

import com.example.securitytest.dto.JoinDTO;
import com.example.securitytest.service.JoinService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class JoinController {

    private final JoinService joinService;

    @GetMapping("/join")
    public String joinP() {
        return "join";
    }

    @PostMapping("/joinProc")
    public String joinProcess(JoinDTO joinDTO, Model model) {

        try {
            System.out.println("회원가입 시도: " + joinDTO.getUsername());

            joinService.joinProcess(joinDTO);

            System.out.println("회원가입 성공: " + joinDTO.getUsername());
            return "redirect:/login?success=true";

        } catch (IllegalArgumentException e) {
            System.out.println("회원가입 실패: " + e.getMessage());
            model.addAttribute("error", e.getMessage());
            return "join";
        } catch (Exception e) {
            System.out.println("회원가입 중 오류 발생: " + e.getMessage());
            model.addAttribute("error", "회원가입 중 오류가 발생했습니다.");
            return "join";
        }
    }
}