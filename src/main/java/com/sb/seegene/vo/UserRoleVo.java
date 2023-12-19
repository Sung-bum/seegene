package com.sb.seegene.vo;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRoleVo {
    private String memberId;
    private String roleGroupId;
}
