package com.sb.seegene.entity;

import com.sun.istack.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.io.Serializable;

@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity  // 객체와 테이블 매핑
@Table(name = "MENU_ROLE")
public class MenuRole implements Serializable {
    @EmbeddedId
    private MenuRoleId menuRoleId;

    @NotNull
    @Column(name = "ROLE")
    private String role;
}
