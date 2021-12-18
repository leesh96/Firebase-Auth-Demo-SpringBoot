package com.suho.demo.firebaseauth.controller.api;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.suho.demo.firebaseauth.controller.api.dto.MemberResponse;
import com.suho.demo.firebaseauth.domain.Member;
import com.suho.demo.firebaseauth.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
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
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid Request Header");
            }
            firebaseToken = firebaseAuth.verifyIdToken(header.substring(7));
        } catch (IllegalArgumentException | FirebaseAuthException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "{\"code\":\"Invalid Token\", \"message\":\"" + e.getMessage() + "\"}");
        }

        return new MemberResponse(memberService.signIn(Member.builder()
                .name(firebaseToken.getName())
                .email(firebaseToken.getEmail())
                .firebaseUID(firebaseToken.getUid())
                .build()
        ));
    }

    @GetMapping("/login")
    public MemberResponse login(Authentication authentication) {
        Member member = ((Member) authentication.getPrincipal());
        return new MemberResponse(member);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/withdraw")
    public void withdraw(Authentication authentication) {
        Member member = ((Member) authentication.getPrincipal());
        memberService.withdraw(member);
    }
}
