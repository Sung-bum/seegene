package com.sb.seegene.controller;

import com.sb.seegene.dto.MenuDto;
import com.sb.seegene.entity.Menu;
import com.sb.seegene.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("api/menu")
@RestController
public class MenuController {

    private final MenuService menuService;

    /**
     * Menu 생성
     *
     * @param menu
     * @return
     */
    @PostMapping("/create")
    public ResponseEntity<MenuDto> createMenu(@RequestBody Menu menu) {
        MenuDto saveMenu = menuService.createMenu(menu);
        return new ResponseEntity<>(saveMenu, HttpStatus.OK);
    }

    /**
     * Menu 수정(메뉴명만 수정)
     *
     * @param menu
     * @return
     */
    @PutMapping("/update")
    public ResponseEntity<MenuDto> updateMenu(@RequestBody Menu menu) {
        MenuDto updateMenu = menuService.updateMenu(menu);
        if (!ObjectUtils.isEmpty(updateMenu)) {
            return new ResponseEntity<>(updateMenu, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(updateMenu, HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Menu List 조회
     *
     * @return
     */
    @GetMapping("/list")
    public ResponseEntity<List<MenuDto>> getMenus() {
        List<MenuDto> menus = menuService.getMenus();
        return new ResponseEntity<>(menus, HttpStatus.OK);
    }

    /**
     * Menu 조회
     *
     * @param id
     * @return
     */
    @GetMapping("{id}")
    public ResponseEntity<MenuDto> getMenu(@PathVariable("id") String id) {
        MenuDto menuDto = menuService.getMenu(id);
        return new ResponseEntity<>(menuDto, HttpStatus.OK);
    }

    /**
     * Menu 삭제
     * 400 BAD_REQUEST = 자식 메뉴가 있어서 삭제 불가능
     *
     * @param id
     * @return
     */
    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteMenu(@PathVariable("id") String id) {
        int i = menuService.deleteMenu(id);
        if (i == 0) {
            String message = "[" + id + "] 하위 메뉴가 존재합니다.";
            return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<>(id, HttpStatus.OK);
        }
    }

}
