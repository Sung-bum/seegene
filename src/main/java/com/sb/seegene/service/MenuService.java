package com.sb.seegene.service;

import com.sb.seegene.dto.MenuDto;
import com.sb.seegene.entity.Menu;
import com.sb.seegene.repository.MenuRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MenuService {

    private final MenuRepository menuRepository;

    /**
     * Menu 생성
     *
     * @param menu
     * @return
     */
    public MenuDto createMenu(Menu menu) {
        MenuDto saveMenu = MenuDto.of(menuRepository.save(menu));
        return saveMenu;
    }

    /**
     * Menu 수정 (parent 값이 다르면 수정 불가)
     *
     * @param menu
     * @return
     */
    public MenuDto updateMenu(Menu menu) {
        MenuDto updateMenu = null;
        try {
            MenuDto existMenu = getMenu(menu.getId());

            if(!ObjectUtils.isEmpty(existMenu)) {
                Menu newMenu = Menu.builder()
                        .id(menu.getId())
                        .name(menu.getName())
                        .parent(existMenu.getParent())
                        .build();
                updateMenu = MenuDto.of(menuRepository.save(newMenu));
            }
        } catch (Exception e) {
            log.info("[Fail] e" + e.toString());
        } finally {
            return updateMenu;
        }
    }

    /**
     * Menu 조회(id)
     *
     * @param id
     * @return
     */
    public MenuDto getMenu(String id) {
        return MenuDto.of(menuRepository.getById(id));
    }

    /**
     * Menu 전체목록 조회
     *
     * @return
     */
    public List<MenuDto> getMenus() {
        return MenuDto.listOf(menuRepository.findAll());
    }

    /**
     * 해당하는 Menu 삭제
     *
     * @param id
     */
    public int deleteMenu(String id) {
        List<MenuDto> childMenu = MenuDto.listOf(menuRepository.findByChildMenu(id));
        if (childMenu.size() > 0) {
            return 0;
        } else {
            menuRepository.deleteById(id);
            return 1;
        }

    }
}
