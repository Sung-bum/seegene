package com.sb.seegene.repository;

import com.sb.seegene.entity.MenuRole;
import com.sb.seegene.entity.MenuRoleId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MenuRoleRepository extends JpaRepository<MenuRole, MenuRoleId> {

    @Query("select m from MenuRole m where m.menuRoleId.roleGroup.roleId IN (:roleIds)")
    List<MenuRole> findAllByid(List<String> roleIds);

    @Query("select m from MenuRole m where m.menuRoleId.menu.id = :menuId")
    List<MenuRole> findAllByMenuId(String menuId);

    @Query("select m from MenuRole m where m.menuRoleId.roleGroup.roleId = :roleId")
    List<MenuRole> findAllByRoleId(String roleId);

    @Query("select m from MenuRole m where m.menuRoleId.menu.id = :menuId")
    Page<MenuRole> findByMenuIdPage(String menuId, Pageable pageable);

    @Query("select m from MenuRole m where m.menuRoleId.roleGroup.roleId = :roleId")
    Page<MenuRole> findByRoleIdPage(String roleId, Pageable pageable);

    @Query("select m from MenuRole m where m.menuRoleId.menu.id = :menuId and m.menuRoleId.roleGroup.roleId = :roleId")
    Page<MenuRole> findByMenuIdAndRoleIdPage(String menuId, String roleId, Pageable pageable);
}
