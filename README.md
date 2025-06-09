# 🔐 Spring Security 인증/인가 시스템

Spring Boot와 Spring Security를 활용한 간단한 사용자 인증 및 권한 관리 시스템입니다.

## 📋 목차
- [프로젝트 개요](#-프로젝트-개요)
- [주요 기능](#-주요-기능)
- [기술 스택](#-기술-스택)
- [프로젝트 구조](#-프로젝트-구조)
- [사용법](#-사용법)
- [API 엔드포인트](#-api-엔드포인트)

## 🚀 프로젝트 개요

Spring Security를 사용한 기본적인 웹 인증 시스템입니다. 일반 사용자와 관리자 권한을 구분하며 특별한 관리자 키로 관리자 권한을 부여할 수 있습니다.

**핵심 특징:**
- 🔑관리자 키를 통한 자동 권한 부여
- 🎨 반응형 UI
- 🛡️ 기본 보안 설정
- 👤 사용자 정보 페이지

## ✨ 주요 기능

### 🔐 인증/인가
- **회원가입**: 일반 사용자 + 관리자 키 입력시 관리자 권한
- **로그인/로그아웃**: Spring Security 폼 인증
- **비밀번호 암호화**: BCrypt 해싱
- **역할 기반 접근 제어**: ROLE_USER, ROLE_ADMIN

### 👤 사용자 관리
- **내 정보 페이지**: 개인 정보 및 계정 상태 확인
- **권한별 UI**: 관리자/일반 사용자 구분

### 👑 관리자 시스템
- **관리자 패널**: 관리자 전용 페이지

## 🛠 기술 스택

- **Spring Boot** 3.5.0
- **Spring Security** 6
- **Spring Data JPA**
- **MySQL** 8.0
- **Thymeleaf**
- **Java** 21
- **Gradle**

## 📁 프로젝트 구조

```
src/main/java/com/example/securitytest/
├── config/
│   └── SecurityConfig.java              # Spring Security 설정
├── controller/
│   ├── AdminController.java             # 관리자 페이지
│   ├── JoinController.java              # 회원가입
│   ├── LoginController.java             # 로그인
│   ├── MainController.java              # 메인 페이지
│   └── MyController.java                # 내 정보
├── dto/
│   ├── CustomUserDetails.java           # UserDetails 구현
│   └── JoinDTO.java                     # 회원가입 DTO
├── entity/
│   └── UserEntity.java                 # 사용자 엔티티
├── repository/
│   └── UserRepository.java             # 데이터 접근
└── service/
    ├── CustomUserDetailsService.java   # 인증 서비스
    └── JoinService.java                # 회원가입 서비스

src/main/resources/templates/
├── login.html                          # 로그인 페이지
├── join.html                           # 회원가입 페이지
├── main.html                           # 메인 페이지
├── admin.html                          # 관리자 페이지
└── my/profile.html                     # 내 정보 페이지
```

## 사용법

### 일반 사용자 가입
1. 회원가입 페이지 접속
2. 사용자명, 비밀번호 입력
3. **관리자 키는 비워둠** 
4. `ROLE_USER` 권한으로 가입

### 관리자 가입
1. 회원가입 페이지 접속
2. 사용자명, 비밀번호 입력
3. **관리자 키에 `hellosungmin!!` 입력** 
4. `ROLE_ADMIN` 권한으로 가입

### 페이지별 기능

| 페이지 | 경로 | 설명 | 권한 |
|--------|------|------|------|
| 메인 | `/` | 사용자별 대시보드 | 모든 사용자 |
| 내 정보 | `/my/profile` | 개인 정보 확인 | 인증된 사용자 |
| 관리자 | `/admin` | 관리자 패널 | 관리자만 |
| 로그인 | `/login` | 로그인 폼 | 비인증 사용자 |
| 회원가입 | `/join` | 회원가입 폼 | 비인증 사용자 |


## API 엔드포인트

### 인증 관련
```http
GET  /login              # 로그인 페이지
POST /loginProc          # 로그인 처리
GET  /join               # 회원가입 페이지  
POST /joinProc           # 회원가입 처리
GET  /logout             # 로그아웃 처리
```

### 사용자 페이지
```http
GET  /                   # 메인 대시보드
GET  /my/profile         # 내 정보 페이지
```

### 관리자 페이지
```http
GET  /admin              # 관리자 패널
```



## 화면
<p align="center">
  <img src="https://github.com/user-attachments/assets/56fb724a-a49e-4a24-a0ec-abe0410e193e" width="400"/><br/>
  <img src="https://github.com/user-attachments/assets/d222c31b-1efb-4018-9db5-054fb80dbd5e" width="400"/><br/>
  <img src="https://github.com/user-attachments/assets/d99283ec-2d1f-48d5-9614-90f884d08d46" width="400"/><br/>
  <img src="https://github.com/user-attachments/assets/84fc116f-1a2c-4f24-8580-53a3a5d8b0d3" width="400"/><br/>
  <img src="https://github.com/user-attachments/assets/22dd6cc2-995d-416d-b592-e4816d29a231" width="400"/><br/>
  <img src="https://github.com/user-attachments/assets/c0dcfd6c-ed61-4f4c-afcf-79ab2635ba9b" width="400"/><br/>
  <img src="https://github.com/user-attachments/assets/6fc51958-2b45-4bf1-b9e1-3963c46222c6" width="400"/>
</p>

