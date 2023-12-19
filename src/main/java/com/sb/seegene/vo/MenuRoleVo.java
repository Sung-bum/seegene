package com.sb.seegene.vo;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MenuRoleVo {
    private String menuId;
    private String roleGroupId;
    private String role;
}
