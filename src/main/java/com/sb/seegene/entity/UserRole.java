package com.sb.seegene.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.io.Serializable;

@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity  // 객체와 테이블 매핑
@Table(name = "USER_ROLE")
public class UserRole implements Serializable{

    @EmbeddedId
    private UserRoleId userRoleId;
}
