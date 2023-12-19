package com.sb.seegene.service;

import com.sb.seegene.config.jwt.JwtTokenProvider;
import com.sb.seegene.dto.JwtToken;
import com.sb.seegene.dto.MemberDto;
import com.sb.seegene.dto.MenuRoleDto;
import com.sb.seegene.dto.UserRoleDto;
import com.sb.seegene.entity.Member;
import com.sb.seegene.entity.MenuRole;
import com.sb.seegene.repository.MemberRepository;
import com.sb.seegene.repository.MenuRoleRepository;
import com.sb.seegene.repository.UserRoleRepository;
import com.sb.seegene.vo.ApprovalVo;
import com.sb.seegene.vo.LoginVo;
import com.sb.seegene.vo.MemberVo;
import com.sb.seegene.vo.UserRoleVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final UserRoleService userRoleService;
    private final UserRoleRepository userRoleRepository;
    private final MenuRoleRepository menuRoleRepository;

    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;

    private PasswordEncoder passwordEncoder;

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

    @Transactional
    public MemberDto singUp(MemberVo memberVo) {
        if(memberRepository.existsById(memberVo.getId())) {
            return null;
//            throw new IllegalArgumentException("사용 중인 ID입니다.");
        }
        List<String> roles = new ArrayList<>();
        roles.add("USER");
        return MemberDto.of(memberRepository.save(memberVo.toEntity(memberVo.getPassword(), roles)));
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
        return MemberDto.of(memberRepository.findMember(id, password).orElse(null));
    }

    /**
     * 상대값 조회(승인대기, 승인확정)
     *
     * @param id
     * @return
     */
    public MemberDto findMemberByStatus(String id) {
        return MemberDto.of(memberRepository.findMemberByStatus(id).orElse(null));
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

    /**
     * 토큰 생성
     *
     * @param username
     * @param password
     * @return
     */
    @Transactional
    public JwtToken jwtToken(String username, String password) {
        // 1. username + password 를 기반으로 Authentication 객체 생성
        // 이때 authentication 은 인증 여부를 확인하는 authenticated 값이 false
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);

        // 2. 실제 검증. authenticate() 메서드를 통해 요청된 Member 에 대한 검증 진행
        // authenticate 메서드가 실행될 때 CustomUserDetailsService 에서 만든 loadUserByUsername 메서드 실행
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // 3. 인증 정보를 기반으로 JWT 토큰 생성
        JwtToken jwtToken = jwtTokenProvider.generateToken(authentication);

        return jwtToken;
    }

    /**
     * 로그인
     *
     * @param loginVo
     * @return
     */
    public Map<String, Object> signIn(LoginVo loginVo) {
        String username = loginVo.getId();
        String password = loginVo.getPassword();

        Map<String, Object> result = new HashMap<>();
        MemberDto loginMember = findMember(username, password);
        if (ObjectUtils.isEmpty(loginMember)) {
            // id, password 실패
            String message = "Id, Password 확인해주세요.";
            result.put("message", message);
            result.put("httpStatus", "BAD_REQUEST");
            return result;
        }

        MemberDto statusMember = findMemberByStatus(username);
        if (ObjectUtils.isEmpty(statusMember)) {
            // 승인대기 상태
            String message = "승인 대기상태입니다.";
            result.put("message", message);
            result.put("httpStatus", "UNAUTHORIZED");
            return result;
        }

        JwtToken jwtToken = jwtToken(username, password);
        log.info("request username = {}, password = {}", username, password);
        log.info("jwtToken accessToken = {}, refreshToken = {}", jwtToken.getAccessToken(), jwtToken.getRefreshToken());
        result.put("JwtToken", jwtToken);
        result.put("httpStatus", "OK");
        return result;
    }

}
