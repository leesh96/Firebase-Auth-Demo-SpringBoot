package com.suho.demo.firebaseauth.controller.api;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.suho.demo.firebaseauth.controller.api.dto.MemberResponse;
import com.suho.demo.firebaseauth.domain.Member;
import com.suho.demo.firebaseauth.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RequiredArgsConstructor
@RequestMapping("/api/member")
@RestController
public class MemberApiController {

    private final MemberService memberService;
    private final FirebaseAuth firebaseAuth;

    @PostMapping("/signin")
    public MemberResponse signIn(@RequestHeader("Authorization") String header) {
        final FirebaseToken firebaseToken;

        try {
            if (header == null || !header.startsWith("Bearer ")) {
                throw new IllegalArgumentException("Invalid Authorization Header");
            }
            firebaseToken = firebaseAuth.verifyIdToken(header.substring(7));
        } catch (IllegalArgumentException | FirebaseAuthException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "{\"code\":\"INVALID_TOKEN\", \"message\":\"" + e.getMessage() + "\"}");
        }

        return new MemberResponse(memberService.signIn(Member.builder()
                .name(firebaseToken.getName())
                .email(firebaseToken.getEmail())
                .firebaseUID(firebaseToken.getUid())
                .build()
        ));
    }
}
