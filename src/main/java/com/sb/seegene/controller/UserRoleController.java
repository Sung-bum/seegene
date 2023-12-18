package com.sb.seegene.controller;

import com.sb.seegene.dto.UserRoleDto;
import com.sb.seegene.service.UserRoleService;
import com.sb.seegene.vo.UserRoleVo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("api/user/role")
@RestController
public class UserRoleController {

    private final UserRoleService userRoleService;

    /**
     * 유저권한 등록
     *
     * @param userRoleVo
     * @return
     */
    @PostMapping("/create")
    public ResponseEntity<UserRoleDto> createUserRole(@RequestBody UserRoleVo userRoleVo) {
        UserRoleDto saveUserRole = userRoleService.createUserRole(userRoleVo);
        if (saveUserRole == null) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(saveUserRole, HttpStatus.OK);
    }

    /**
     * 유저권한 수정
     *
     * @param userRoleVo
     * @return
     */
    @PutMapping("/update")
    public ResponseEntity<UserRoleDto> updateUserRole(@RequestBody UserRoleVo userRoleVo) {
        UserRoleDto updateUserRole = userRoleService.updateUserRole(userRoleVo);
        if (!ObjectUtils.isEmpty(updateUserRole)) {
            return new ResponseEntity<>(updateUserRole, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(updateUserRole, HttpStatus.NOT_FOUND);
        }
    }

    /**
     * 유저권한 삭제
     *
     * @param userRoleVo
     * @return
     */
    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteUserRole(@RequestBody UserRoleVo userRoleVo) {
        int i = userRoleService.deleteUserRole(userRoleVo);
        String message = "";
        if (i == 0) {
            message = "MenuId, GroupId 를 확인하세요.";
            return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
        } else {
            message = "메뉴 권한 삭제 완료";
            return new ResponseEntity<>(message,HttpStatus.OK);
        }
    }

    /**
     * 유저권한 전체 조회
     *
     * @return
     */
    @GetMapping("/list")
    public ResponseEntity<List<UserRoleDto>> getUserRoles() {
        List<UserRoleDto> userRoleDtos =userRoleService.getUserRoles();
        return new ResponseEntity<>(userRoleDtos, HttpStatus.OK);
    }

    /**
     * 사용자 아이디로 유저권한 조회
     *
     * @param memberId
     * @return
     */
    @GetMapping("/listmember/{memberId}")
    public ResponseEntity<List<UserRoleDto>> getUserRolesWithMemberId(@PathVariable("memberId") String memberId) {
        List<UserRoleDto> userRoleDtos =userRoleService.getUserRolesWithMemberId(memberId);
        return new ResponseEntity<>(userRoleDtos, HttpStatus.OK);
    }

    /**
     * 권한그룹 아이디로 유저권한 조회
     *
     * @param roleId
     * @return
     */
    @GetMapping("/listmember/{roleId}")
    public ResponseEntity<List<UserRoleDto>> getUserRolesWithRoleId(@PathVariable("roleId") String roleId) {
        List<UserRoleDto> userRoleDtos =userRoleService.getUserRoleWithRoleId(roleId);
        return new ResponseEntity<>(userRoleDtos, HttpStatus.OK);
    }

    /**
     * 유저권한 조회 Pagination( Vo를 통한 검색 ) 유저ID, 권한ID
     *
     * @param userRoleVo
     * @param pageable
     * @return
     */
    @GetMapping("/page/list")
    public ResponseEntity<Page<UserRoleDto>> getUserRolePage(@RequestBody UserRoleVo userRoleVo, Pageable pageable) {
        Page<UserRoleDto> userRoleDtos = userRoleService.getUserRolePage(userRoleVo, pageable);
        if (userRoleDtos != null) {
            return new ResponseEntity<>(userRoleDtos, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }
}
