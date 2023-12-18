package com.sb.seegene.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Entity  // 객체와 테이블 매핑
@Table(name = "MENU")
public class Menu {
    @Id // Primary key 지정
    @Column(name = "ID")
    private String id;

    @NotNull
    @Column(name = "NAME")
    private String name;

    @Column(name = "PARENT")
    private String parent;

}
