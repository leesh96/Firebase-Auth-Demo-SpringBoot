package com.suho.demo.firebaseauth.service;

import com.suho.demo.firebaseauth.domain.Member;
import com.suho.demo.firebaseauth.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MemberService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public Member loadUserByUsername(String uid) throws UsernameNotFoundException {
        return memberRepository.findByFirebaseUID(uid).get();
    }

    public Member signIn(Member member) {
        return memberRepository.save(member);
    }
}
