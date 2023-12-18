package com.sb.seegene.service;

import com.sb.seegene.dto.RoleGroupDto;
import com.sb.seegene.entity.RoleGroup;
import com.sb.seegene.repository.RoleGroupRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoleGroupService {

    private final RoleGroupRepository roleGroupRepository;

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
     * RoleGroupId에 속한 메뉴 조회
     *
     * @param id
     * @return
     */
    public RoleGroup getRoleGroupWithMenu(String id) {

        return null;
    }
}
