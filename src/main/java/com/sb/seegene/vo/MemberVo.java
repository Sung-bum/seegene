package com.sb.seegene.vo;

import com.sb.seegene.entity.Member;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberVo {
    private String id;
    private String name;
    private String password;
    private String status;
    private List<String> roles = new ArrayList<>();

    public Member toEntity(String encodedPassword, List<String> roles) {
        return Member.builder()
                .id(id)
                .name(name)
//                .password(encodedPassword)
                .password(password)
                .status("w")
                .roles(roles)
                .build();
    }
}
