package com.sb.seegene.controller;

import com.sb.seegene.dto.RoleGroupDto;
import com.sb.seegene.entity.RoleGroup;
import com.sb.seegene.service.RoleGroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("api/role/group")
@RestController
public class RoleGroupController {

    private final RoleGroupService roleGroupService;

    /**
     * RoleGroup 생성(권한그룹)
     *
     * @param roleGroup
     * @return
     */
    @PostMapping("/create")
    public ResponseEntity<RoleGroupDto> createRoleGroup(@RequestBody RoleGroup roleGroup) {
        RoleGroupDto saveRoleGroup = roleGroupService.createRoleGroup(roleGroup);
        return new ResponseEntity<>(saveRoleGroup, HttpStatus.OK);
    }

    /**
     * RoleGroup 수정
     *
     * @param roleGroup
     * @return
     */
    @PutMapping("/update")
    public ResponseEntity<RoleGroupDto> updateRoleGroup(@RequestBody RoleGroup roleGroup) {
        RoleGroupDto updateRoleGroup = roleGroupService.updateRoleGroup(roleGroup);
        if (!ObjectUtils.isEmpty(updateRoleGroup)) {
            return new ResponseEntity<>(updateRoleGroup, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(updateRoleGroup, HttpStatus.NOT_FOUND);
        }
    }

    /**
     * RoleGroup 전체조회
     *
     * @return
     */
    @GetMapping("/list")
    public ResponseEntity<List<RoleGroupDto>> getRoleGroupList() {
        List<RoleGroupDto> roleGroupList = roleGroupService.getRoleGroups();
        return new ResponseEntity<>(roleGroupList, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{roleId}")
    public ResponseEntity<String> deleteRoleGroup(@PathVariable("roleId") String roleId) {
        String message = roleGroupService.deleteRoleGroup(roleId);
        if (message.equals("완료")) {
            return new ResponseEntity<>(message, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
        }
    }



}
