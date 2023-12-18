package com.sb.seegene.entity;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Entity  // 객체와 테이블 매핑
@Table(name = "ROLE_GROUP")
public class RoleGroup {
    @Id // Primary key 지정
    @Column(name = "ROLEID")
    private String roleId;

    @NotNull
    @Column(name = "ROLENAME")
    private String roleName;
}
