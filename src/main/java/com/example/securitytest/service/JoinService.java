package com.example.securitytest.service;

import com.example.securitytest.dto.JoinDTO;
import com.example.securitytest.entity.UserEntity;
import com.example.securitytest.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JoinService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    // application.yml에서 관리자 키를 주입받음
    @Value("${app.admin.secret-key:defaultSecretKey}")
    private String adminSecretKey;

    public void joinProcess(JoinDTO joinDTO) {

        // 입력값 검증
        if (joinDTO.getUsername() == null || joinDTO.getUsername().trim().isEmpty()) {
            throw new IllegalArgumentException("회원이름을 입력하세요");
        }

        if (joinDTO.getPassword() == null || joinDTO.getPassword().trim().isEmpty()) {
            throw new IllegalArgumentException("비밀번호를 입력하세요");
        }

        // db에 이미 동일한 username을 가진 회원이 존재하는지 확인
        boolean isUser = userRepository.existsByUsername(joinDTO.getUsername());
        if (isUser) {
            throw new IllegalArgumentException("동일한 이름의 사용자가 있습니다.");
        }

        UserEntity data = new UserEntity();
        data.setUsername(joinDTO.getUsername());
        data.setPassword(bCryptPasswordEncoder.encode(joinDTO.getPassword()));

        // 🔥 핵심 로직: 관리자 키 검증
        if (joinDTO.getAdminKey() != null &&
                !joinDTO.getAdminKey().trim().isEmpty() &&
                adminSecretKey.equals(joinDTO.getAdminKey().trim())) {

            data.setRole("ROLE_ADMIN");
            System.out.println("🔐 관리자 권한으로 가입 완료: " + joinDTO.getUsername());

        } else {
            data.setRole("ROLE_USER");
            System.out.println("👤 일반 사용자 권한으로 가입 완료: " + joinDTO.getUsername());
        }

        userRepository.save(data);
    }
}