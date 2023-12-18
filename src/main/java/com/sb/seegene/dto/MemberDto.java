package com.sb.seegene.dto;

import com.sb.seegene.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberDto {
    private String id;
    private String name;
//    private String password;
    private String status;

    public static MemberDto of(Member member) {
        return new MemberDto(member.getId(),
                member.getName(),
//                member.getPassword(),
                member.getStatus()
        );
    }
}
