package com.example.securitytest.dto;

import com.example.securitytest.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

//로그인한 사용자의 정보를 담기 위한 UserDetails 구현체
@RequiredArgsConstructor
public class CustomUserDetails implements UserDetails {

    private final UserEntity userEntity;


    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public String getUsername() {
        return userEntity.getUsername();
    }

    @Override
    public String getPassword() {
        return userEntity.getPassword();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // SimpleGrantedAuthority 사용으로 코드 간소화
        return List.of(new SimpleGrantedAuthority(userEntity.getRole()));
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    // 편의 메서드: UserEntity 직접 접근
    public UserEntity getUserEntity() {
        return userEntity;
    }

    // 편의 메서드: ADMIN 권한 체크
    public boolean isAdmin() {
        return "ROLE_ADMIN".equals(userEntity.getRole());
    }
}
