package com.sb.seegene.vo;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginVo {
    private String id;
    private String username;
    private String password;

    public static LoginVo of(LoginVo loginVo) {
        return new LoginVo(loginVo.getId(), loginVo.getId(), loginVo.getPassword());
    }
}
