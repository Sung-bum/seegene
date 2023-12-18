package com.sb.seegene.dto;

import com.sb.seegene.entity.MenuRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MenuRoleDto {
    private MenuDto menuDto;
    private RoleGroupDto roleGroupDto;
    private String role;

    public static MenuRoleDto of(MenuRole menuRole) {
        return new MenuRoleDto(MenuDto.of(menuRole.getMenuRoleId().getMenu()),
                RoleGroupDto.of(menuRole.getMenuRoleId().getRoleGroup()),
                menuRole.getRole()
        );
    }

    public static List<MenuRoleDto> listOf(List<MenuRole> menuRoles) {
        List<MenuRoleDto> list = new ArrayList<>();
        for (MenuRole menuRole : menuRoles) {
            list.add(new MenuRoleDto(
                    MenuDto.of(menuRole.getMenuRoleId().getMenu()),
                    RoleGroupDto.of(menuRole.getMenuRoleId().getRoleGroup()),
                    menuRole.getRole()
            ));
        }
        return list;
    }

    public static Page<MenuRoleDto> pageOf(Page<MenuRole> menuRoles) {
        Page<MenuRoleDto> menuRolePage = menuRoles.map( m ->
                        new MenuRoleDto(
                                MenuDto.of(m.getMenuRoleId().getMenu()),
                                RoleGroupDto.of(m.getMenuRoleId().getRoleGroup()),
                                m.getRole()
                        ));
        return menuRolePage;
    }

}
