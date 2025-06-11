package com.example.securitytest.service;

import com.example.securitytest.dto.CustomUserDetails;
import com.example.securitytest.entity.UserEntity;
import com.example.securitytest.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;


    //loadUserByUsername는 로그인 요청 시 해당 사용자 정보를 데이터베이스에서 찾아 반환
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        UserEntity userData = userRepository.findByUsername(username);

        if(userData != null) {
            return new CustomUserDetails(userData);
        }

        return null;
    }
}
