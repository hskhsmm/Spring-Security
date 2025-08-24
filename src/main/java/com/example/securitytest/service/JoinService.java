package com.example.securitytest.service;

import com.example.securitytest.dto.JoinDTO;
import com.example.securitytest.entity.UserEntity;
import com.example.securitytest.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Slf4j
@Service
@RequiredArgsConstructor
public class JoinService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    // application.yml에서 관리자 키를 주입받음
    @Value("${app.admin.secret-key:defaultSecretKey}")
    private String adminSecretKey;

    @Transactional
    public void joinProcess(JoinDTO joinDTO) {
        // 입력값 검증 로직 분리
        validateJoinInput(joinDTO);

        // 중복 사용자 체크
        if (userRepository.existsByUsername(joinDTO.getUsername())) {
            throw new IllegalArgumentException("이미 존재하는 사용자명입니다: " + joinDTO.getUsername());
        }

        // 사용자 엔티티 생성
        UserEntity userEntity = createUserEntity(joinDTO);

        // 저장
        userRepository.save(userEntity);

        log.info("회원가입 완료: {}, 권한: {}", userEntity.getUsername(), userEntity.getRole());
    }

    private void validateJoinInput(JoinDTO joinDTO) {
        if (!StringUtils.hasText(joinDTO.getUsername())) {
            throw new IllegalArgumentException("사용자명을 입력해주세요");
        }

        if (!StringUtils.hasText(joinDTO.getPassword())) {
            throw new IllegalArgumentException("비밀번호를 입력해주세요");
        }

        if (joinDTO.getPassword().length() < 4) {
            throw new IllegalArgumentException("비밀번호는 최소 4자리 이상이어야 합니다");
        }
    }

    private UserEntity createUserEntity(JoinDTO joinDTO) {
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(joinDTO.getUsername().trim());
        userEntity.setPassword(bCryptPasswordEncoder.encode(joinDTO.getPassword()));

        // 관리자 권한 검증 (보안 강화 버전)
        if (isValidAdminKey(joinDTO.getAdminKey())) {
            userEntity.setRole("ROLE_ADMIN");
            log.warn("🔐 관리자 권한으로 가입: {}", joinDTO.getUsername());
        } else {
            userEntity.setRole("ROLE_USER");
            log.info("👤 일반 사용자 권한으로 가입: {}", joinDTO.getUsername());
        }

        return userEntity;
    }

    private boolean isValidAdminKey(String inputAdminKey) {
        if (!StringUtils.hasText(inputAdminKey)) {
            return false;
        }

        // 보안 강화: 시간 기반 공격 방지를 위한 constant-time 비교
        byte[] expected = adminSecretKey.getBytes();
        byte[] actual = inputAdminKey.trim().getBytes();

        return java.security.MessageDigest.isEqual(expected, actual);
    }
}