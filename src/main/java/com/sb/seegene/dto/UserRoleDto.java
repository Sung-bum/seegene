package com.sb.seegene.dto;

import com.sb.seegene.entity.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserRoleDto {
    private MemberDto memberDto;
    private RoleGroupDto roleGroupDto;

    public static UserRoleDto of(UserRole userRole) {
        return new UserRoleDto(MemberDto.of(userRole.getUserRoleId().getMember()),
                RoleGroupDto.of(userRole.getUserRoleId().getRoleGroup())
        );
    }

    public static List<UserRoleDto> listOf(List<UserRole> userRoles) {
        List<UserRoleDto> list = new ArrayList<>();
        for (UserRole userRole: userRoles) {
            list.add(new UserRoleDto(
                    MemberDto.of(userRole.getUserRoleId().getMember()),
                    RoleGroupDto.of(userRole.getUserRoleId().getRoleGroup())
            ));
        }
        return list;
    }

    public static Page<UserRoleDto> pageOf(Page<UserRole> userRoles) {
        Page<UserRoleDto> userRolePage = userRoles.map( m ->
                new UserRoleDto(
                        MemberDto.of(m.getUserRoleId().getMember()),
                        RoleGroupDto.of(m.getUserRoleId().getRoleGroup())
                ));
        return userRolePage;
    }
}
