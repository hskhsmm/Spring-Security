package com.example.securitytest.repository;

import com.example.securitytest.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Integer> {

    boolean existsByUsername(String username);

    // 추가: 사용자명으로 사용자 조회
    UserEntity findByUsername(String username);

}