package com.sb.seegene.repository;

import com.sb.seegene.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, String> {

    @Query("select m from Member m where m.id = :id and m.password =:password")
    Member findMember(String id, String password);

    @Query("select m from Member m where m.id = :id and m.status = 's'")
    Member findMemberByStatus(String id);
}
