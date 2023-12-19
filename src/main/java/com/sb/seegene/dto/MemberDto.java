package com.sb.seegene.dto;

import com.sb.seegene.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.ObjectUtils;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberDto {
    private String id;
    private String name;
//    private String password;
    private String status;

    public static MemberDto of(Member member) {
        if (ObjectUtils.isEmpty(member))
            return null;
        return new MemberDto(member.getId(),
                member.getName(),
//                member.getPassword(),
                member.getStatus()
        );
    }
}
