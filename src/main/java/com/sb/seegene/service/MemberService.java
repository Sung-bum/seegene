package com.sb.seegene.service;

import com.sb.seegene.dto.MemberDto;
import com.sb.seegene.dto.MenuRoleDto;
import com.sb.seegene.dto.UserRoleDto;
import com.sb.seegene.entity.Member;
import com.sb.seegene.entity.MenuRole;
import com.sb.seegene.repository.MemberRepository;
import com.sb.seegene.repository.MenuRoleRepository;
import com.sb.seegene.repository.UserRoleRepository;
import com.sb.seegene.vo.ApprovalVo;
import com.sb.seegene.vo.UserRoleVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final UserRoleService userRoleService;
    private final UserRoleRepository userRoleRepository;
    private final MenuRoleRepository menuRoleRepository;

    /**
     * Member 생성
     *
     * @param member
     * @return
     */
    public MemberDto createMember(Member member) {
        MemberDto saveMember = MemberDto.of(memberRepository.save(member));
        return saveMember;
    }

    /**
     * Member 수정
     *
     * @param member
     * @return
     */
    public MemberDto updateMember(Member member) {
        MemberDto updateMember = null;
        try {
            MemberDto existMember = getMember(member.getId());
            if (!ObjectUtils.isEmpty(existMember)) {
                updateMember = MemberDto.of(memberRepository.save(member));
            }
        } catch (Exception e) {
//            e.printStackTrace();
            log.info("[Fail] e" + e.toString());
        } finally {
            return updateMember;
        }
    }

    /**
     * Member 조회(id)
     *
     * @param id
     * @return
     */
    public MemberDto getMember(String id) {
        return MemberDto.of(memberRepository.getById(id));
    }

    /**
     * id, password 조회(로그인)
     *
     * @param id
     * @param password
     * @return
     */
    public MemberDto findMember(String id, String password) {
        return MemberDto.of(memberRepository.findMember(id, password));
    }

    /**
     * 상대값 조회(승인대기, 승인확정)
     *
     * @param id
     * @return
     */
    public MemberDto findMemberByStatus(String id) {
        return MemberDto.of(memberRepository.findMemberByStatus(id));
    }

    /**
     * 사용자 가입승인, 권한(Role) 부여
     *
     * @param approvalVo
     * @return
     */
    public MemberDto approvalMember(ApprovalVo approvalVo) {
        MemberDto updateMember = null;
        try {
            MemberDto existMember = getMember(approvalVo.getMemberId());
            if (!ObjectUtils.isEmpty(existMember)) {
                // 가입 상태 변경
                Member member = memberRepository.getById(approvalVo.getMemberId());
                member.setStatus(approvalVo.getStatus());
                updateMember = MemberDto.of(memberRepository.save(member));
            }

            UserRoleVo newUserRoleVo = new UserRoleVo();
            newUserRoleVo.setMemberId(approvalVo.getMemberId());
            newUserRoleVo.setRoleGroupId(approvalVo.getRoleGroupId());

            UserRoleDto result = userRoleService.createUserRole(newUserRoleVo);
            if (result == null) {
                return null;
            }
        } catch (Exception e) {
//            e.printStackTrace();
            log.info("[Fail] e" + e.toString());
        } finally {
            return updateMember;
        }
    }

    /**
     * 사용자 메뉴 리스트
     *
     * @param id
     * @return
     */
    public List<MenuRoleDto> menuList(String id) {
        // role group list
        List<UserRoleDto> userRoleDtoList = UserRoleDto.listOf(userRoleRepository.findAllByid(id));
        List<String> roleIds = new ArrayList<>();
        for (UserRoleDto userRoleDto : userRoleDtoList) {
            roleIds.add(userRoleDto.getRoleGroupDto().getRoleId());
        }

        List<MenuRoleDto> menuRoleDtoList = MenuRoleDto.listOf(menuRoleRepository.findAllByid(roleIds));
        return menuRoleDtoList;
    }



}
