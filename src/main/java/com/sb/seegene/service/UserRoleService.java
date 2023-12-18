package com.sb.seegene.service;

import com.sb.seegene.dto.UserRoleDto;
import com.sb.seegene.entity.Member;
import com.sb.seegene.entity.RoleGroup;
import com.sb.seegene.entity.UserRole;
import com.sb.seegene.entity.UserRoleId;
import com.sb.seegene.repository.MemberRepository;
import com.sb.seegene.repository.RoleGroupRepository;
import com.sb.seegene.repository.UserRoleRepository;
import com.sb.seegene.vo.UserRoleVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserRoleService {

    private final UserRoleRepository userRoleRepository;
    private final MemberRepository memberRepository;
    private final RoleGroupRepository roleGroupRepository;

    /**
     * 유저권한 생성
     *
     * @param userRoleVo
     * @return
     */
    public UserRoleDto createUserRole(UserRoleVo userRoleVo) {
        UserRoleId userRoleId = getUserRoleId(userRoleVo);
        if (userRoleId == null) {
            return null;
        }

        UserRole newUserRole = UserRole.builder()
                .userRoleId(userRoleId)
                .build();
        UserRoleDto saveUserRole = UserRoleDto.of(userRoleRepository.save(newUserRole));
        return saveUserRole;
    }

    /**
     * 유저권한 수정
     *
     * @param userRoleVo
     * @return
     */
    public UserRoleDto updateUserRole(UserRoleVo userRoleVo) {
        UserRoleDto updateUserRole = null;
        try {
            UserRoleId userRoleId = getUserRoleId(userRoleVo);
            if (userRoleId == null) {
                return null;
            }

            UserRole newUserRole = UserRole.builder()
                    .userRoleId(userRoleId)
                    .build();

            UserRoleDto existUserRole = getUserRole(userRoleId);
            if (!ObjectUtils.isEmpty(existUserRole)) {
                updateUserRole = UserRoleDto.of(userRoleRepository.save(newUserRole));
            }
        } catch (Exception e) {
            log.info("[Fail] e" + e.toString());
        } finally {
            return updateUserRole;
        }
    }

    /**
     * 유저권한 단건 조회
     *
     * @param userRoleId
     * @return
     */
    private UserRoleDto getUserRole(UserRoleId userRoleId) {
        return UserRoleDto.of(userRoleRepository.getById(userRoleId));
    }

    /**
     * UserRoleId 조회
     *
     * @param userRoleVo
     * @return
     */
    public UserRoleId getUserRoleId(UserRoleVo userRoleVo) {
        Member member = memberRepository.getById(userRoleVo.getMemberId());
        RoleGroup roleGroup = roleGroupRepository.getById(userRoleVo.getRoleGroupId());
        if (ObjectUtils.isEmpty(member) || ObjectUtils.isEmpty(roleGroup)) {
            return null;
        }

        UserRoleId userRoleId = UserRoleId.builder()
                .member(member)
                .roleGroup(roleGroup)
                .build();
        return userRoleId;
    }

    /**
     * 유저권한 삭제
     *
     * @param userRoleVo
     * @return
     */
    public int deleteUserRole(UserRoleVo userRoleVo) {
        UserRoleId userRoleId = getUserRoleId(userRoleVo);
        if(userRoleId == null) {
            return 0;
        }
        userRoleRepository.deleteById(userRoleId);
        return 1;
    }

    /**
     * 사용자권한 전체 조회
     *
     * @return
     */
    public List<UserRoleDto> getUserRoles() {
        return UserRoleDto.listOf(userRoleRepository.findAll());
    }

    /**
     * 사용자 아이디로 유저권한 조회
     *
     * @param memberId
     * @return
     */
    public List<UserRoleDto> getUserRolesWithMemberId(String memberId) {
        return UserRoleDto.listOf(userRoleRepository.findAllByid(memberId));
    }

    /**
     * 권한그룹 아이디로 유저권한 조회
     *
     * @param roleId
     * @return
     */
    public List<UserRoleDto> getUserRoleWithRoleId(String roleId) {
        return UserRoleDto.listOf(userRoleRepository.findAllByRoleId(roleId));
    }

    /**
     * 유저권한 조회 Pagination( Vo를 통한 검색 ) 유저ID, 권한ID
     *
     * @param userRoleVo
     * @param pageable
     * @return
     */
    public Page<UserRoleDto> getUserRolePage(UserRoleVo userRoleVo, Pageable pageable) {
        String memberId = null;
        String roleId = null;
        if (StringUtils.hasText(userRoleVo.getMemberId()))
            memberId = userRoleVo.getMemberId();
        if(StringUtils.hasText(userRoleVo.getRoleGroupId()))
            roleId = userRoleVo.getRoleGroupId();

        if (memberId != null && roleId != null) {
            return UserRoleDto.pageOf(userRoleRepository.findByMemberIdAndRoleIdPage(memberId, roleId, pageable));
        } else if (memberId != null) {
            return UserRoleDto.pageOf(userRoleRepository.findByMemberIdPage(memberId, pageable));
        } else if (roleId != null) {
            return UserRoleDto.pageOf(userRoleRepository.findByRoleIdPage(roleId, pageable));
        } else {
            return UserRoleDto.pageOf(userRoleRepository.findAll(pageable));
        }
    }
}
