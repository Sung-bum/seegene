package com.sb.seegene.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;

@SuperBuilder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class MenuRoleId implements Serializable {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="MENUID")
    private Menu menu;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="ROLEID")
    private RoleGroup roleGroup;
}
