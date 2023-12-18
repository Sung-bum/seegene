package com.sb.seegene.controller;

import com.sb.seegene.dto.MenuRoleDto;
import com.sb.seegene.entity.MenuRole;
import com.sb.seegene.service.MenuRoleService;
import com.sb.seegene.vo.MenuRoleVo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("api/menu/role")
@RestController
public class MenuRoleController {

    private final MenuRoleService menuRoleService;

    /**
     * 메뉴권한 등록
     * 권한그룹 이름으로 메뉴 권한을 등록한다.
     *
     * @param menuRoleVo
     * @return
     */
    @PostMapping("/create")
    public ResponseEntity<MenuRoleDto> createMenuRole(@RequestBody MenuRoleVo menuRoleVo) {
        MenuRoleDto saveMenuRole = menuRoleService.createMenuRole(menuRoleVo);
        if (saveMenuRole == null) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(saveMenuRole, HttpStatus.OK);
    }

    /**
     * 메뉴권한 권한 수정
     *
     * @param menuRoleVo
     * @return
     */
    @PutMapping("/update")
    public ResponseEntity<MenuRoleDto> updateMenuRole(@RequestBody MenuRoleVo menuRoleVo) {
        MenuRoleDto updateMenuRole = menuRoleService.updateMenuRole(menuRoleVo);
        if (!ObjectUtils.isEmpty(updateMenuRole)) {
           return new ResponseEntity<>(updateMenuRole, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(updateMenuRole, HttpStatus.NOT_FOUND);
        }
    }

    /**
     * 메뉴권한 전체 조회
     *
     * @param
     * @return
     */
    @GetMapping("/list")
    public ResponseEntity<List<MenuRoleDto>> getMenuRoles() {
        List<MenuRoleDto> menuRoleDtos = menuRoleService.getMenuRoles();
        return new ResponseEntity<>(menuRoleDtos, HttpStatus.OK);
    }

    /**
     * 메뉴권한 메뉴ID 조회
     *
     * @param menuId
     * @return
     */
    @GetMapping("/listmenu/{menuId}")
    public ResponseEntity<List<MenuRoleDto>> getMenuRolesWithMenuId(@PathVariable("menuId") String menuId) {
        List<MenuRoleDto> menuRoleDtos = menuRoleService.getMenuRolesWithMenuId(menuId);
        return new ResponseEntity<>(menuRoleDtos, HttpStatus.OK);
    }

    /**
     * 메뉴권한 권한ID 조회(roleId)
     *
     * @param roleId
     * @return
     */
    @GetMapping("/listrole/{roleId}")
    public ResponseEntity<List<MenuRoleDto>> getMenuRolesWithRoleId(@PathVariable("roleId") String roleId) {
        List<MenuRoleDto> menuRoleDtos = menuRoleService.getMenuRolesWithRoleId(roleId);
        return new ResponseEntity<>(menuRoleDtos, HttpStatus.OK);
    }

    /**
     * 메뉴권한 조회 Pagination( Vo를 통한 검색 ) 메뉴ID, 권한ID
     *
     * @param menuRoleVo
     * @param pageable
     * @return
     */
    @GetMapping("/page/list")
    public ResponseEntity<Page<MenuRoleDto>> getMenuRolePage(@RequestBody MenuRoleVo menuRoleVo, Pageable pageable) {
        Page<MenuRoleDto> menuRoleDtos = menuRoleService.getMenuRolePage(menuRoleVo, pageable);
        if (menuRoleDtos != null) {
            return new ResponseEntity<>(menuRoleDtos, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * 메뉴권한 삭제
     *
     * @param menuRoleVo
     * @return
     */
    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteMenuRole(@RequestBody MenuRoleVo menuRoleVo) {
        int i = menuRoleService.deleteMenuRole(menuRoleVo);
        String message = "";
        if (i == 0) {
            message = "MenuId, GroupId 를 확인하세요.";
            return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
        } else {
            message = "메뉴 권한 삭제 완료";
            return new ResponseEntity<>(message,HttpStatus.OK);
        }
    }


}
