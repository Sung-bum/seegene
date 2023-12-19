//package com.sb.seegene;
//
//import com.sb.seegene.dto.MemberDto;
//import com.sb.seegene.entity.Menu;
//import com.sb.seegene.entity.RoleGroup;
//import com.sb.seegene.vo.MemberVo;
//import com.sb.seegene.vo.MenuRoleVo;
//import com.sb.seegene.vo.UserRoleVo;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class DataInit {
//    public void initDataTest() {
//        // 사용자 추가
//        List<MemberVo> userList = getMemberList();
//        String memberUrl = "http://127.0.0.1:" + randomServerPort + "/api/member/signup";
//        userList.stream().map(m ->
//                testRestTemplate.postForEntity(memberUrl, m, MemberDto.class)
//        );
//
//        // 메뉴
//        List<Menu> menuList = getMenuList();
//        menuList.stream().map(m -> menuService.createMenu(m));
//
//        // 권한그룹
//        List<RoleGroup> roleGroupList = getRoleGroupList();
//        roleGroupList.stream().map(m -> roleGroupService.createRoleGroup(m));
//
//        // 메뉴권한
//        List<MenuRoleVo> menuRoleList = getMenuRoleList();
//        menuRoleList.stream().map(m -> menuRoleService.createMenuRole(m));
//
//        // 유저권한
//        List<UserRoleVo> userRoleList = getUserRoleList();
//        userRoleList.stream().map(m -> userRoleService.createUserRole(m));
//    }
//
//    // 임시 초기 데이터 목록
//    public List<MemberVo> getMemberList() {
//        MemberVo admin = MemberVo.builder()
//                .id("admin")
//                .password("1")
//                .name("관리자")
//                .status("s") // 가입확정상태
//                .build();
//        MemberVo user2 = MemberVo.builder()
//                .id("test1")
//                .password("1")
//                .name("시니어")
//                .status("s") // 가입확정상태
//                .build();
//        MemberVo user3 = MemberVo.builder()
//                .id("test2")
//                .password("1")
//                .name("신입사용자")
//                .status("s") // 가입확정상태
//                .build();
//        List<MemberVo> userList = new ArrayList<>();
//        userList.add(admin);
//        userList.add(user2);
//        userList.add(user3);
//        return userList;
//    }
//
//    public List<Menu> getMenuList() {
//        Menu menu1 = Menu.builder()
//                .id("m01")
//                .name("관리자메뉴")
//                .build();
//        Menu menu2 = Menu.builder()
//                .id("m011")
//                .name("관리자 서브메뉴1")
//                .parent("m01")
//                .build();
//        Menu menu3 = Menu.builder()
//                .id("u01")
//                .name("사용자메뉴")
//                .build();
//        Menu menu4 = Menu.builder()
//                .id("u011")
//                .name("사용자 서브메뉴1")
//                .parent("u01")
//                .build();
//        Menu menu5 = Menu.builder()
//                .id("u012")
//                .name("사용자 서브메뉴2")
//                .parent("u01")
//                .build();
//
//        List<Menu> menuList = new ArrayList<>();
//        menuList.add(menu1);
//        menuList.add(menu2);
//        menuList.add(menu3);
//        menuList.add(menu4);
//        menuList.add(menu5);
//        return menuList;
//    }
//
//    public List<RoleGroup> getRoleGroupList() {
//        RoleGroup roleGroup1 = RoleGroup.builder()
//                .roleId("sys")
//                .roleName("슈퍼관리자권한")
//                .build();
//        RoleGroup roleGroup2 = RoleGroup.builder()
//                .roleId("adm")
//                .roleName("관리자권한")
//                .build();
//        RoleGroup roleGroup3 = RoleGroup.builder()
//                .roleId("usr")
//                .roleName("사용자권한")
//                .build();
//        List<RoleGroup>  roleGroupList = new ArrayList<>();
//        roleGroupList.add(roleGroup1);
//        roleGroupList.add(roleGroup2);
//        roleGroupList.add(roleGroup3);
//        return roleGroupList;
//    }
//
//    public List<MenuRoleVo> getMenuRoleList() {
//        MenuRoleVo sysRole1 = MenuRoleVo.builder()
//                .menuId("m01")
//                .roleGroupId("sys")
//                .role("CRUD")
//                .build();
//        MenuRoleVo sysRole2 = MenuRoleVo.builder()
//                .menuId("m011")
//                .roleGroupId("sys")
//                .role("CRUD")
//                .build();
//        MenuRoleVo sysRole3 = MenuRoleVo.builder()
//                .menuId("u01")
//                .roleGroupId("sys")
//                .role("CRUD")
//                .build();
//        MenuRoleVo sysRole4 = MenuRoleVo.builder()
//                .menuId("u011")
//                .roleGroupId("sys")
//                .role("CRUD")
//                .build();
//        MenuRoleVo sysRole5 = MenuRoleVo.builder()
//                .menuId("u012")
//                .roleGroupId("sys")
//                .role("CRUD")
//                .build();
//
//        MenuRoleVo admRole1 = MenuRoleVo.builder()
//                .menuId("m01")
//                .roleGroupId("adm")
//                .role("CRU")
//                .build();
//        MenuRoleVo admRole2 = MenuRoleVo.builder()
//                .menuId("m011")
//                .roleGroupId("adm")
//                .role("CRU")
//                .build();
//        MenuRoleVo admRole3 = MenuRoleVo.builder()
//                .menuId("u01")
//                .roleGroupId("adm")
//                .role("CRUD")
//                .build();
//        MenuRoleVo admRole4 = MenuRoleVo.builder()
//                .menuId("u011")
//                .roleGroupId("adm")
//                .role("CRUD")
//                .build();
//        MenuRoleVo admRole5 = MenuRoleVo.builder()
//                .menuId("u012")
//                .roleGroupId("adm")
//                .role("CRUD")
//                .build();
//
//        MenuRoleVo usrRole1 = MenuRoleVo.builder()
//                .menuId("u011")
//                .roleGroupId("usr")
//                .role("CRU")
//                .build();
//        MenuRoleVo usrRole2 = MenuRoleVo.builder()
//                .menuId("u012")
//                .roleGroupId("usr")
//                .role("CRU")
//                .build();
//
//        List<MenuRoleVo> menuRoleList = new ArrayList<>();
//        menuRoleList.add(sysRole1);
//        menuRoleList.add(sysRole2);
//        menuRoleList.add(sysRole3);
//        menuRoleList.add(sysRole4);
//        menuRoleList.add(sysRole5);
//        menuRoleList.add(admRole1);
//        menuRoleList.add(admRole2);
//        menuRoleList.add(admRole3);
//        menuRoleList.add(admRole4);
//        menuRoleList.add(admRole5);
//        menuRoleList.add(usrRole1);
//        menuRoleList.add(usrRole2);
//        return menuRoleList;
//    }
//
//    public List<UserRoleVo> getUserRoleList() {
//        UserRoleVo userRoleVo1 = UserRoleVo.builder()
//                .memberId("admin")
//                .roleGroupId("sys")
//                .build();
//        UserRoleVo userRoleVo2 = UserRoleVo.builder()
//                .memberId("test1")
//                .roleGroupId("adm")
//                .build();
//        UserRoleVo userRoleVo3 = UserRoleVo.builder()
//                .memberId("test2")
//                .roleGroupId("usr")
//                .build();
//
//        List<UserRoleVo> userRoleList = new ArrayList<>();
//        userRoleList.add(userRoleVo1);
//        userRoleList.add(userRoleVo2);
//        userRoleList.add(userRoleVo3);
//        return userRoleList;
//    }
//}
