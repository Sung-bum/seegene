package com.sb.seegene.service;

import com.sb.seegene.dto.MenuRoleDto;
import com.sb.seegene.dto.RoleGroupDto;
import com.sb.seegene.dto.UserRoleDto;
import com.sb.seegene.entity.RoleGroup;
import com.sb.seegene.repository.RoleGroupRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoleGroupService {

    private final RoleGroupRepository roleGroupRepository;
    private final MenuRoleService menuRoleService;
    private final UserRoleService userRoleService;

    /**
     * RoleGroup 생성(권한그룹)
     *
     * @param roleGroup
     * @return
     */
    public RoleGroupDto createRoleGroup(RoleGroup roleGroup) {
        RoleGroupDto saveRoleGroup = RoleGroupDto.of(roleGroupRepository.save(roleGroup));
        return saveRoleGroup;
    }

    /**
     * RoleGroup(권한그룹) 수정
     *
     * @param roleGroup
     * @return
     */
    public RoleGroupDto updateRoleGroup(RoleGroup roleGroup) {
        RoleGroupDto updateRoleGroup = null;
        try {
            RoleGroupDto existRoleGroup = getRoleGroup(roleGroup.getRoleId());
            if (!ObjectUtils.isEmpty(existRoleGroup)) {
                updateRoleGroup = RoleGroupDto.of(roleGroupRepository.save(roleGroup));
            }
        } catch (Exception e) {
            log.info("[Fail] e" + e.toString());
        } finally {
            return updateRoleGroup;
        }
    }

    /**
     * RoleGroup 조회(id)
     *
     * @param id
     * @return
     */
    public RoleGroupDto getRoleGroup(String id) {
        return RoleGroupDto.of(roleGroupRepository.getById(id));
    }


    /**
     * RoleGroup 전체 검색
     *
     * @return
     */
    public List<RoleGroupDto> getRoleGroups() {
        return RoleGroupDto.listOf(roleGroupRepository.findAll());
    }

    public String deleteRoleGroup(String roleId) {
        List<MenuRoleDto> menuRoleList =  menuRoleService.getMenuRolesWithRoleId(roleId);
        if (menuRoleList.size() > 0) {
            return "메뉴권한에 포함된 권한이 존재합니다.";
        }
        List<UserRoleDto> userRoleList = userRoleService.getUserRoleWithRoleId(roleId);
        if (userRoleList.size() > 0) {
            return "유저권한에 포함된 권한이 존재합니다.";
        }
        roleGroupRepository.deleteById(roleId);
        return "완료";
    }
}
