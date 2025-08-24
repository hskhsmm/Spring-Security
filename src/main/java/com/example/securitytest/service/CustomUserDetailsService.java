package com.example.securitytest.service;

import com.example.securitytest.dto.CustomUserDetails;
import com.example.securitytest.entity.UserEntity;
import com.example.securitytest.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;


    //loadUserByUsername는 로그인 요청 시 해당 사용자 정보를 데이터베이스에서 찾아 반환
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.debug("사용자 인증 시도: {}", username);

        return userRepository.findByUsername(username)
                .map(userData -> {
                    log.debug("사용자 인증 성공: {}, 권한: {}", username, userData.getRole());
                    return new CustomUserDetails(userData);
                })
                .orElseThrow(() -> {
                    log.warn("사용자 인증 실패: {} (사용자를 찾을 수 없음)", username);
                    return new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + username);
                });
    }
}
