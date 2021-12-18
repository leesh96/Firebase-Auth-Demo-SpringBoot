package com.suho.demo.firebaseauth.service;

import com.suho.demo.firebaseauth.domain.Member;
import com.suho.demo.firebaseauth.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class MemberService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public Member loadUserByUsername(String uid) throws UsernameNotFoundException {
        return memberRepository.findByFirebaseUID(uid).orElseThrow();
    }

    @Transactional
    public Member signIn(Member member) {
        Optional<Member> signInMember = memberRepository.findByFirebaseUID(member.getFirebaseUID());

        return signInMember.orElseGet(() -> memberRepository.save(member));
    }

    @Transactional
    public void withdraw(Member member) {
        memberRepository.delete(member);
    }
}
