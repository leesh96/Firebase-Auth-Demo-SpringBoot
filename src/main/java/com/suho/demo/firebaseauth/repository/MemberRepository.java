package com.suho.demo.firebaseauth.repository;

import com.suho.demo.firebaseauth.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByFirebaseUID(String uid);
}
