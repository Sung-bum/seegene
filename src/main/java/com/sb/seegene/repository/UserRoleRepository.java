package com.sb.seegene.repository;

import com.sb.seegene.entity.UserRole;
import com.sb.seegene.entity.UserRoleId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRoleRepository extends JpaRepository<UserRole, UserRoleId> {

    @Query("select m from UserRole m where m.userRoleId.member.id = :memberId")
    List<UserRole> findAllByid(String memberId);

    @Query("select m from UserRole m where m.userRoleId.roleGroup.roleId = :roleId")
    List<UserRole> findAllByRoleId(String roleId);

    @Query("select m from UserRole m where m.userRoleId.member.id = :memberId")
    Page<UserRole> findByMemberIdPage(String memberId, Pageable pageable);

    @Query("select m from UserRole m where m.userRoleId.roleGroup.roleId = :roleId")
    Page<UserRole> findByRoleIdPage(String roleId, Pageable pageable);

    @Query("select m from UserRole m where m.userRoleId.member.id = :memberId and m.userRoleId.roleGroup.roleId = :roleId")
    Page<UserRole> findByMemberIdAndRoleIdPage(String memberId, String roleId, Pageable pageable);
}
