# 🔐 Spring Security 인증/인가 시스템

Spring Boot와 Spring Security를 활용한 완전한 사용자 인증 및 권한 관리 시스템입니다.

## 📋 목차

- [프로젝트 개요](#-프로젝트-개요)
- [주요 기능](#-주요-기능)
- [기술 스택](#-기술-스택)
- [프로젝트 구조](#-프로젝트-구조)
- [사용법](#-사용법)
- [보안 설정](#-보안-설정)
- [API 엔드포인트](#-api-엔드포인트)

## 🚀 프로젝트 개요

이 프로젝트는 Spring Security를 사용하여 사용자 인증, 권한 관리, 세션 관리를 구현한 웹 애플리케이션입니다. 일반 사용자와 관리자 권한을 구분하며, 특별한 관리자 키를 통해 관리자 권한 부여가 가능합니다.

**핵심 특징:**
- 🔑 특별 키를 통한 관리자 권한 자동 부여
- 🎨 모던하고 반응형 UI 디자인
- 🛡️ 강력한 보안 설정 (CSRF, 세션 관리)
- 👤 개인화된 사용자 프로필 페이지

## ✨ 주요 기능

### 🔐 인증/인가 시스템
- **회원가입**: 일반 사용자 + 관리자 키 입력시 관리자 권한
- **로그인/로그아웃**: Spring Security 기반 폼 인증
- **비밀번호 암호화**: BCrypt 해싱 알고리즘
- **역할 기반 접근 제어**: ROLE_USER, ROLE_ADMIN
- **세션 관리**: 동시 로그인 제한, 세션 고정 공격 방지

### 👤 사용자 관리
- **내 정보 페이지**: 개인 정보 및 계정 통계 확인
- **프로필 관리**: 사용자 상세 정보 표시
- **권한별 UI**: 관리자/일반 사용자 구분된 인터페이스

### 👑 관리자 시스템
- **관리자 대시보드**: 시스템 통계 및 관리 기능
- **사용자 관리**: 전체 사용자 현황 (예정)
- **시스템 설정**: 보안, 백업 등 관리 기능

### 🎨 모던 UI/UX
- **반응형 디자인**: 모바일/데스크톱 최적화
- **카드 기반 레이아웃**: 깔끔한 정보 구성
- **호버 효과**: 부드러운 인터랙션
- **역할별 시각화**: 권한에 따른 아이콘/색상 구분

## 🛠 기술 스택

### Backend
- **Spring Boot** 3.5.0
- **Spring Security** 6
- **Spring Data JPA** (Hibernate)
- **Java** 21

### Database
- **MySQL** 8.0
- **JPA/Hibernate** ORM

### Frontend
- **Thymeleaf** Template Engine
- **HTML5/CSS3**
- **Modern CSS** (Grid, Flexbox, Gradients)

### Build & Tools
- **Gradle** 8.x
- **Lombok** (코드 간소화)

## 📁 프로젝트 구조

```
src/main/java/com/example/securitytest/
├── 📁 config/
│   └── SecurityConfig.java              # Spring Security 설정
├── 📁 controller/
│   ├── AdminController.java             # 관리자 페이지
│   ├── JoinController.java              # 회원가입 처리
│   ├── LoginController.java             # 로그인 페이지
│   ├── LogoutController.java            # 로그아웃 처리
│   ├── MainController.java              # 메인 페이지
│   └── MyController.java                # 내 정보 관리
├── 📁 dto/
│   ├── CustomUserDetails.java           # UserDetails 구현
│   └── JoinDTO.java                     # 회원가입 DTO
├── 📁 entity/
│   └── UserEntity.java                 # 사용자 엔티티
├── 📁 repository/
│   └── UserRepository.java             # 사용자 데이터 접근
└── 📁 service/
    ├── CustomUserDetailsService.java   # 사용자 인증 서비스
    └── JoinService.java                # 회원가입 비즈니스 로직

src/main/resources/templates/
├── login.html                          # 로그인 페이지
├── join.html                           # 회원가입 페이지
├── main.html                           # 메인 대시보드
├── admin.html                          # 관리자 패널
└── 📁 my/
    └── profile.html                    # 내 정보 페이지
```



## 📖 사용법

### 👤 일반 사용자 가입
1. **회원가입 페이지** 접속
2. **사용자명, 비밀번호** 입력
3. **관리자 키는 비워둠** ⭐
4. 가입 완료 → `ROLE_USER` 권한

### 👑 관리자 가입
1. **회원가입 페이지** 접속
2. **사용자명, 비밀번호** 입력
3. **관리자 키에 `hellosungmin!!` 입력** ⭐
4. 가입 완료 → `ROLE_ADMIN` 권한

### 🔍 주요 페이지 기능

| 페이지 | 경로 | 설명 | 권한 |
|--------|------|------|------|
| 메인 | `/` | 대시보드, 사용자별 맞춤 UI | 모든 사용자 |
| 내 정보 | `/my/profile` | 개인 정보 및 계정 통계 | 인증된 사용자 |
| 관리자 | `/admin` | 시스템 관리 패널 | 관리자만 |
| 로그인 | `/login` | 로그인 폼 | 비인증 사용자 |
| 회원가입 | `/join` | 회원가입 폼 | 비인증 사용자 |

## 🔒 보안 설정

### 🔐 인증 보안
```java
// BCrypt 비밀번호 해싱
BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
String hashedPassword = encoder.encode(rawPassword);
```


### 🚪 URL 기반 접근 제어
```
java
.requestMatchers("/", "/login", "/join").permitAll()           // 모든 사용자
.requestMatchers("/admin").hasRole("ADMIN")                    // 관리자만
.requestMatchers("/my/**").hasAnyRole("ADMIN", "USER")         // 인증된 사용자
.anyRequest().authenticated()                                  // 나머지는 인증 필요
```



## 🔗 API 엔드포인트

### 인증 관련
```http
GET  /login              # 로그인 페이지
POST /loginProc          # 로그인 처리 (Spring Security)
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
GET  /admin              # 관리자 패널 (ADMIN 권한 필요)
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

