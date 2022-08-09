# 🙋‍♂️ Today-Assembel - 오늘의 모임 백엔드 서버

<br>

## 💡 Introduction

- 간단한 모임 웹 사이트 백엔드 서버 입니다.
- 기본적으로 계정 관련 CRUD API, 모임 관련 CRUD API 를 제공하고 있습니다.

<br>

## 🔖 Stack
- Back: `Spring Boot`, `Spring Security`, `JPA`, `Lombok`, `Spring Boot Starter Mail`(이메일 인증 기능), `JsonWebToken` (JWT 기반 인증 기능), `Swagger` 
- Test: `JUnit5`
- Build: `Maven`
- DB: `Postgresql`

<br>

## 🔖 ERD
![](./today_assemble_erd.png)


## 🔖 DB Table 구성

- **accounts** (계정)
    - `id` (계정 고유값, PK)
    - `created_at` (데이터 생성 시간)
    - `updated_at` (데이터 수정 시간)
    - `email` (로그인할 때 사용할 email)
    - `password` (비밀번호)
    - `name` (이름 혹은 별칭)
    - `age` (나이)
    - `birth` (생년월일)
    - `gender` (성별)
    - `accountType` (계정 유형 ex. 관리자 or 일반회원)
    - `emailVerified` (이메일 인증된 계정인지 체크)
    - `emailCheckToken` (이메일 인증용 토큰)
    - `emailCheckTokenGeneratedAt` (이메일 인증 토큰 발급 시간)
    - `emailLoginVerified` (이메일로 로그인할 때 체크)
    - `emailLoginToken` (이메일 로그인용 토큰)
    - `emailLoginTokenGeneratedAt` (이메일 로그인용 토큰 발급 시간)
    - `accounts_images_id` (계정 이미지 id, FK)
    
  
- **accounts_images** (계정 이미지)
    - `id` (계정 이미지 고유값, PK)
    - `created_at` (데이터 생성 시간)
    - `updated_at` (데이터 수정 시간)
    - `image` (이미지 데이터)
    - `image_type` (이미지 타입, ex. 메인 or 서브)


- **email** (이메일)
  - `id` (이메일 고유값, PK)
  - `created_at` (데이터 생성 시간)
  - `updated_at` (데이터 수정 시간)
  - `email_type` (이메일 타입, ex. 인증용 이메일 or 로그인용 이메일)
  - `accounts_id` (이메일 대상 계정 id, FK)


- **accounts_mapper_events** (계정-모임 mapper 테이블)
  - `id` (계정-모임 고유값, PK)
  - `created_at` (데이터 생성 시간)
  - `updated_at` (데이터 수정 시간)
  - `events_id` (모임 id, FK)
  - `accounts_id` (계정 id, FK)


- **accounts_like_events** ('좋아요' 누른 모임 테이블)
  - `id` ('좋아요' 고유값, PK)
  - `created_at` (데이터 생성 시간)
  - `updated_at` (데이터 수정 시간)
  - `events_id` (모임 id, FK)
  - `accounts_id` (계정 id, FK)



- **Events** (모임)
  - `id` (모임 고유값, PK)
  - `created_at` (데이터 생성 시간)
  - `updated_at` (데이터 수정 시간)
  - `address`(모임 주소)
  - `description` (모임에 대한 설명)
  - `events_time` (모임 시간)
  - `events_type` (모임 타입, ex. OFFLINE)
  - `latitude` (모임 장소 위도)
  - `longitude` (모임 장소 경도)
  - `max_members` (모임 최대 인원)
  - `name` (모임 이름)
  - `take_time` (모임 소요 시간)
  - `host_id` (모임 주최자 계정 id, FK)


- **events_images** (모임 이미지)
  - `id` (모임 이미지 고유값, PK)
  - `created_at` (데이터 생성 시간)
  - `updated_at` (데이터 수정 시간)
  - `image` (이미지 데이터)
  - `image_type` (이미지 타입, ex. 메인 or 서브)
  - `events_id` (해당 이미지 모임 id, FK)


- **tags** (모임 태그)
  - `id` (tag 고유값, PK)
  - `created_at` (데이터 생성 시간)
  - `updated_at` (데이터 수정 시간)
  - `name` (태그명)
  - `events_id` (해당 태그의 모임 id, FK)

<br>

## 🔖 기능

- **계정** 관련 기능
  - 회원가입
  - 회원 상세 조회
  - 회원 리스트 조회
  - 회원수정
  - 회원탈퇴
  - 로그인
  - JWT 발급 및 인증
  - 모임 '좋아요' 관리
  - '좋아요' 누른 모임 리스트 조회
  - 참여중인 모임 리스트 조회
  - email 인증
  - 패스워드 없이 email 로그인


- **모임** 관련 기능
  - 모임생성
  - 모임 상세 조회
  - 모임 리스트 조회
  - 모임명, 태그로 모임 검색
  - 지역으로 모임 검색
  - 모임수정
  - 모임삭제
  - 모임 참여 관리
  - 모임 참여중인 계정 리스트 조회


- **email** 관련 기능
  - email 발송
  - 계정 인증 토큰값 확인
  - 로그인 토큰값 혹인

<br>

## 🔖 API 목록

### JWT 입력
![](./swagger-jwt1.png)

<br>

![](./swagger-api1.png)
![](./swagger-api2.png)
![](./swagger-api3.png)

