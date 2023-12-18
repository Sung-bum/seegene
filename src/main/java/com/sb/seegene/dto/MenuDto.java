package com.sb.seegene.dto;

import com.sb.seegene.entity.Menu;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MenuDto {
    private String id;
    private String name;
    private String parent;

    public static MenuDto of(Menu menu) {
        return new MenuDto(menu.getId(),
                menu.getName(),
                menu.getParent()
        );
    }

    public static List<MenuDto> listOf(List<Menu> menus) {
        List<MenuDto> list = new ArrayList<>();
        for(Menu menu : menus) {
            list.add(new MenuDto(menu.getId(),
                    menu.getName(),
                    menu.getParent()
            ));
        }
        return list;
    }
}
