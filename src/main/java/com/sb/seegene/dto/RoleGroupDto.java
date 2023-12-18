package com.sb.seegene.dto;

import com.sb.seegene.entity.Menu;
import com.sb.seegene.entity.RoleGroup;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RoleGroupDto {
    private String roleId;
    private String roleName;

    public static RoleGroupDto of(RoleGroup roleGroup) {
        return new RoleGroupDto(roleGroup.getRoleId(),
                roleGroup.getRoleName()
        );
    }
}
