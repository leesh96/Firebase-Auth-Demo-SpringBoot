# Firebase-Auth-Demo-SpringBoot

Firebase Auth를 사용하여 자체 백엔드 서버와 인증, 인가를 구현한 데모의 스프링부트 백엔드 입니다.

## Structure

- FirebaseInitializer를 사용하여 FirebaseAuth를 빈 설정
- SpringSecurity를 사용하여 인가 처리 (회원가입 API는 ignore, 나머지 API는 인증 필요)
- Member 엔티티는 UserDetails를 구현
- 스프링 시큐리티에서 거치는 여러 개의 필터가 있는데 FirebaseTokenFilter를 자체 구현하여 사용자 인증 정보로 UsernameAndPasswordAuthenticationFilter로 전달
- FirebaseTokenFilter는 OncePerRequestFilter로 구현하여 요청마다 필터를 거치도록 구현 -> 컴포넌트 어노테이션이나 빈 어노테이션 사용하면 ignore 안되고 모든 요청이 거치기 때문에 안됌
- FirebaseTokenFilter에서 클라이언트로 부터 요청 헤더로 받은 FirebaseIdToken 검증 -> 토큰 취소 여부도 확인
- 인증된 사용자 정보는 Authentication.getPrincipal 으로 확인할 수 있다.

스프링부트는 공부하는 수준이라 제대로 된 구현은 아닐 수도 있다.

## Reference

[https://velog.io/@couchcoding/Firebase로-Google-로그인-구현하기-Spring-파트](https://velog.io/@couchcoding/Firebase로-Google-로그인-구현하기-Spring-파트)

