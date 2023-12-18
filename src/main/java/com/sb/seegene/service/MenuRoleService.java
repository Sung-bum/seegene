package com.sb.seegene.service;

import com.sb.seegene.dto.MenuRoleDto;
import com.sb.seegene.entity.Menu;
import com.sb.seegene.entity.MenuRole;
import com.sb.seegene.entity.MenuRoleId;
import com.sb.seegene.entity.RoleGroup;
import com.sb.seegene.repository.MenuRepository;
import com.sb.seegene.repository.MenuRoleRepository;
import com.sb.seegene.repository.RoleGroupRepository;
import com.sb.seegene.vo.MenuRoleVo;
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
public class MenuRoleService {

    private final MenuRoleRepository menuRoleRepository;
    private final MenuRepository menuRepository;
    private final RoleGroupRepository roleGroupRepository;

    /**
     * 메뉴권한 생성
     *
     * @param menuRoleVo
     * @return
     */
    public MenuRoleDto createMenuRole(MenuRoleVo menuRoleVo) {
        MenuRoleId newMenuRoleId = getMenuRoleId(menuRoleVo);
        if (newMenuRoleId == null) {
            return null;
        }
        MenuRole newMenuRole = MenuRole.builder()
                .menuRoleId(newMenuRoleId)
                .role(menuRoleVo.getRole())
                .build();

        MenuRoleDto saveMenuRole = MenuRoleDto.of(menuRoleRepository.save(newMenuRole));
        return saveMenuRole;
    }

    /**
     * 메뉴권한 수정
     *
     * @param menuRoleVo
     * @return
     */
    public MenuRoleDto updateMenuRole(MenuRoleVo menuRoleVo) {
        MenuRoleDto updateMenuRole = null;
        try {
            MenuRoleId newMenuRoleId = getMenuRoleId(menuRoleVo);
            if (newMenuRoleId == null) {
                return null;
            }

            MenuRole newMenuRole = MenuRole.builder()
                    .menuRoleId(newMenuRoleId)
                    .role(menuRoleVo.getRole())
                    .build();

            MenuRoleDto existMeuRole = getMenuRole(newMenuRoleId);
            if(!ObjectUtils.isEmpty(existMeuRole)) {
                updateMenuRole = MenuRoleDto.of(menuRoleRepository.save(newMenuRole));
            }
        } catch (Exception e) {
            log.info("[Fail] e" + e.toString());
        } finally {
            return updateMenuRole;
        }
    }

    /**
     * 메뉴권한 단건 조회
     *
     * @param menuRoleId
     * @return
     */
    public MenuRoleDto getMenuRole(MenuRoleId menuRoleId) {
        return MenuRoleDto.of(menuRoleRepository.getById(menuRoleId));
    }

    /**
     * MenuRoleId 조회
     *
     * @param menuRoleVo
     * @return
     */
    public MenuRoleId getMenuRoleId(MenuRoleVo menuRoleVo) {
        Menu menu = menuRepository.getById(menuRoleVo.getMenuId());
        RoleGroup roleGroup = roleGroupRepository.getById(menuRoleVo.getRoleGroupId());
        if (ObjectUtils.isEmpty(menu) || ObjectUtils.isEmpty(roleGroup)) {
            return null;
        }
        MenuRoleId menuRoleId = MenuRoleId.builder()
                .menu(menu)
                .roleGroup(roleGroup)
                .build();
        return menuRoleId;
    }

    /**
     * 메뉴권한 삭제
     *
     * @param menuRoleVo
     * @return
     */
    public int deleteMenuRole(MenuRoleVo menuRoleVo) {
        MenuRoleId menuRoleId = getMenuRoleId(menuRoleVo);
        if (menuRoleId == null) {
            return 0;
        }
        menuRoleRepository.deleteById(menuRoleId);
        return 1;
    }

    /**
     * 메뉴권한 전체 조회
     *
     * @return
     */
    public List<MenuRoleDto> getMenuRoles() {
        return MenuRoleDto.listOf(menuRoleRepository.findAll());
    }

    /**
     * 메뉴 아이디로 메뉴권한 조회
     *
     * @param menuId
     * @return
     */
    public List<MenuRoleDto> getMenuRolesWithMenuId(String menuId) {
        return MenuRoleDto.listOf(menuRoleRepository.findAllByMenuId(menuId));
    }

    /**
     * 권한그룹 아이디로 메뉴권한 조회
     *
     * @param roleId
     * @return
     */
    public List<MenuRoleDto> getMenuRolesWithRoleId(String roleId) {
        return MenuRoleDto.listOf(menuRoleRepository.findAllByRoleId(roleId));
    }

    /**
     * 메뉴권한 조회 Pagination( Vo를 통한 검색 ) 메뉴ID, 권한ID
     *
     * @param menuRoleVo
     * @param pageable
     * @return
     */
    public Page<MenuRoleDto> getMenuRolePage(MenuRoleVo menuRoleVo, Pageable pageable) {
        String menuId = null;
        String roleId = null;
        if (StringUtils.hasText(menuRoleVo.getMenuId()))
            menuId = menuRoleVo.getMenuId();
        if(StringUtils.hasText(menuRoleVo.getRoleGroupId()))
            roleId = menuRoleVo.getRoleGroupId();

        if (menuId != null && roleId != null) {
            return MenuRoleDto.pageOf(menuRoleRepository.findByMenuIdAndRoleIdPage(menuId, roleId, pageable));
        } else if (menuId != null) {
            return MenuRoleDto.pageOf(menuRoleRepository.findByMenuIdPage(menuId, pageable));
        } else if (roleId != null) {
            return MenuRoleDto.pageOf(menuRoleRepository.findByRoleIdPage(roleId, pageable));
        } else {
            return MenuRoleDto.pageOf(menuRoleRepository.findAll(pageable));
        }
    }
}
