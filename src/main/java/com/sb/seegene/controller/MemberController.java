package com.sb.seegene.controller;

import com.sb.seegene.dto.MemberDto;
import com.sb.seegene.dto.MenuRoleDto;
import com.sb.seegene.entity.Member;
import com.sb.seegene.service.MemberService;
import com.sb.seegene.vo.ApprovalVo;
import com.sb.seegene.vo.MemberVo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("api/member")
@RestController
public class MemberController {

    private final MemberService memberService;

    /**
     * Member 생성(회원가입 - 최초 status w 로 대기 상태)
     *
     * @param member
     * @return
     * @throws Exception
     */
    @PostMapping("/create")
    public ResponseEntity<MemberDto> createMember(@RequestBody Member member) {
        member.setStatus("w");
        MemberDto savedMember = memberService.createMember(member);
        return new ResponseEntity<>(savedMember, HttpStatus.OK);
    }

    /**
     * 토큰 추가 후 회원가입
     * Member 생성(회원가입 - 최초 status w 로 대기 상태)
     *
     * @param memberVo
     * @return
     */
    @PostMapping("/signup")
    public ResponseEntity<?> singUp(@RequestBody MemberVo memberVo) {
        memberVo.setStatus("w"); // 가입승인대기
        MemberDto saveMemberDto = memberService.singUp(memberVo);
        if (!ObjectUtils.isEmpty(saveMemberDto)) {
            return new ResponseEntity<>(saveMemberDto, HttpStatus.OK);
        } else {
            String message = "사용중인 ID입니다.";
            return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
        }

    }

    /**
     * Member 수정
     *
     * @param member
     * @return
     */
    @PutMapping("/update")
    public ResponseEntity<MemberDto> updateMember(@RequestBody Member member) {
        MemberDto updateMember = memberService.updateMember(member);
        if (!ObjectUtils.isEmpty(updateMember)) {
            return new ResponseEntity<>(updateMember, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(updateMember, HttpStatus.NOT_FOUND);
        }
    }

    /**
     * 유저 가입확정 및 권한 부여
     *
     * @param approvalVo
     * @return
     */
    @PutMapping("/approval")
    public ResponseEntity<MemberDto> approvalMember(@RequestBody ApprovalVo approvalVo) {
        MemberDto updateMember = memberService.approvalMember(approvalVo);
        if (!ObjectUtils.isEmpty(updateMember)) {
            return new ResponseEntity<>(updateMember, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(updateMember, HttpStatus.NOT_FOUND);
        }
    }

    /**
     * 사용자 메뉴리스트
     *
     * @param id
     * @return
     */
    @GetMapping("/menu/list/{id}")
    public ResponseEntity<?> menuList(@PathVariable("id") String id) {
        List<MenuRoleDto> list = memberService.menuList(id);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }
}
