package com.sb.seegene.repository;

import com.sb.seegene.dto.MenuDto;
import com.sb.seegene.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MenuRepository extends JpaRepository<Menu, String> {
    @Query("select m from Menu m where m.parent = :id")
    List<Menu> findByChildMenu(String id);
}
