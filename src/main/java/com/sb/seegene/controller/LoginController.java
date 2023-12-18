package com.sb.seegene.controller;

import com.sb.seegene.dto.MemberDto;
import com.sb.seegene.entity.Member;
import com.sb.seegene.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
