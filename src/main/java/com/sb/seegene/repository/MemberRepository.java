package com.sb.seegene.repository;

import com.sb.seegene.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, String> {

    @Query("select m from Member m where m.id = :id and m.password =:password")
    Optional<Member> findMember(String id, String password);

    @Query("select m from Member m where m.id = :id and m.status = 's'")
    Optional<Member> findMemberByStatus(String id);

    Optional<Member> findById(String id);
}
