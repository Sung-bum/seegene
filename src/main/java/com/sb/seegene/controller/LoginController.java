package com.sb.seegene.controller;

import com.sb.seegene.config.SecurityUtil;
import com.sb.seegene.dto.JwtToken;
import com.sb.seegene.dto.MemberDto;
import com.sb.seegene.entity.Member;
import com.sb.seegene.service.MemberService;
import com.sb.seegene.vo.LoginVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("api/login")
@RestController
public class LoginController {

    private final MemberService memberService;

    /**
     * Login Member
     *
     * @param member
     * @return
     */
    @PostMapping
    public ResponseEntity<MemberDto> login(@RequestBody Member member) {
        String id = member.getId();
        String password = member.getPassword();

        MemberDto loginMember = memberService.findMember(id, password);
        if (ObjectUtils.isEmpty(loginMember)) {
            // id, password 실패
            return new ResponseEntity<>(loginMember, HttpStatus.BAD_REQUEST);
        }

        MemberDto statusMember = memberService.findMemberByStatus(id);
        if (ObjectUtils.isEmpty(statusMember)) {
            // 승인대기 상태
            return new ResponseEntity<>(statusMember, HttpStatus.UNAUTHORIZED);
        } else {
            return new ResponseEntity<>(statusMember, HttpStatus.OK);
        }
    }

    /**
     * JWT 토큰을 사용한 사용자 로그인
     * 
     * @param loginVo
     * @return
     */
    @PostMapping("/jwt/sign-in")
    public ResponseEntity<?> signIn(@RequestBody LoginVo loginVo) {


        Map<String, Object> result = memberService.signIn(loginVo);
        String httpStatus = (String) result.get("httpStatus");
        if (httpStatus.equals("BAD_REQUEST")) {
            return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
        } else if (httpStatus.equals("UNAUTHORIZED")) {
            return new ResponseEntity<>(result, HttpStatus.UNAUTHORIZED);
        } else {
            return new ResponseEntity<>(result, HttpStatus.OK);
        }

    }

    @PostMapping("/test")
    public String test() {
        String userId = SecurityUtil.getCurrentUsername();
        return userId;
    }
}
