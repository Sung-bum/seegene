package com.sb.seegene.vo;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApprovalVo {
    private String memberId;
    private String status;
    private String roleGroupId;
}
