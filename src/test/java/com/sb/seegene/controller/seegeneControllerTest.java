package com.sb.seegene.controller;

import com.sb.seegene.dto.*;
import com.sb.seegene.entity.Menu;
import com.sb.seegene.entity.RoleGroup;
import com.sb.seegene.service.*;
import com.sb.seegene.utills.DatabaseCleanUp;
import com.sb.seegene.vo.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class seegeneControllerTest {
    @Autowired
    DatabaseCleanUp databaseCleanUp;

    @Autowired
    MemberService memberService;

    @Autowired
    MenuService menuService;

    @Autowired
    RoleGroupService roleGroupService;

    @Autowired
    MenuRoleService menuRoleService;

    @Autowired
    UserRoleService userRoleService;

    @Autowired
    TestRestTemplate testRestTemplate;

    @LocalServerPort
    int randomServerPort;

    private MemberVo memberVo;

    @BeforeEach
    void beforeEach() {
        // Member 회원가입
        memberVo = MemberVo.builder()
                .id("tester1")
                .password("1")
                .name("테스트유저")
                .status("w") // 가입대기상태
                .build();
    }

//    @AfterEach
//    void afterEach() {
//        databaseCleanUp.truncateAllEntity();
//    }

    /**
     * 과제 내용 순차적으로 테스트 진행
     * 1. 회원가입
     * 2. 로그인
     * 3. 사용자당 전체 Menu 조회
     * 4. 사용자 승인처리 및 권한부여
     * 5. 메뉴 생성 (조회 수정 삭제 테스트 생략) API는 존재
     * 6. 그룹권한 생성
     * 7. 그룹권한 수정
     * 8. 그룹권한 삭제
     * 9. 메뉴권한 생성(조회 수정 삭제 테스트 생략) API는 존재
     * 10. 유저권한 생성(조회 수정 삭제 테스트 생략) API는 존재
     */

    /**
     * 1. 회원가입
     */
    @Test
    public void signUpTest() {
        // API 요청 설정
        String url = "http://127.0.0.1:" + randomServerPort + "/api/member/signup";
        ResponseEntity<MemberDto> responseEntity = testRestTemplate.postForEntity(url, memberVo, MemberDto.class);

        // 응답 검증
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        MemberDto savedMemberDto = responseEntity.getBody();
        assertThat(savedMemberDto.getId()).isEqualTo(memberVo.getId());
        assertThat(savedMemberDto.getName()).isEqualTo(memberVo.getName());
    }

    /**
     * 2. 로그인
     * 테스트상에서 토큰을 바로 발급받아서 실험, 실제 api에서는 가입확정상태가 아니면 토큰 미발급
     */
    @Test
    public void signInTest() {
        LoginVo loginVo = LoginVo.builder()
                .id("tester1")
                .username("tester1")
                .password("1")
                .build();

        //로그인 요청
        JwtToken jwtToken = memberService.jwtToken(loginVo.getUsername(), loginVo.getPassword());

        // HttpHeaders 객체 생성 및 토큰 추가
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth(jwtToken.getAccessToken());
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        // API 요청 설정
        String url = "http://127.0.0.1:" + randomServerPort + "/api/login/test";
        ResponseEntity<String> responseEntity = testRestTemplate.postForEntity(url, new HttpEntity<>(httpHeaders), String.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isEqualTo(loginVo.getUsername());
    }

    /**
     * 3. 기본 데이터로 설정한 사용자로 메뉴 리스트 검색
     */
    @Test
    public void menuListTest() {
        String memberId = "admin";

        LoginVo loginVo = LoginVo.builder()
                .id("admin")
                .username("admin")
                .password("1")
                .build();

        //로그인 요청
        JwtToken jwtToken = memberService.jwtToken(loginVo.getUsername(), loginVo.getPassword());

        // HttpHeaders 객체 생성 및 토큰 추가
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth(jwtToken.getAccessToken());
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity header = new HttpEntity(httpHeaders);

        String url = "http://127.0.0.1:" + randomServerPort + "/api/member/menu/list/"+memberId;
//        ResponseEntity<ArrayList> responseEntity = testRestTemplate.getForEntity(url, new HttpEntity<>(httpHeaders), ArrayList.class);
        ResponseEntity<ArrayList> responseEntity = testRestTemplate.exchange(url, HttpMethod.GET, header, ArrayList.class);

        // 응답 검증
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        ArrayList list = responseEntity.getBody();
        System.out.println(list);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        // 기초 데이터시 admin에 설정된 메뉴 5개
        assertThat(list.size()==5);
    }

    /**
     *  4. 맨 위 테스트에서 가입한 유저 w(가입대기) -> s(가입승인) 변경하고, 권한그룹은 usr2 부여
     */
    @Test
    public void approvalTest() {
        ApprovalVo approvalVo = ApprovalVo.builder()
                .memberId("tester1")
                .status("s")
                .roleGroupId("usr2")
                .build();

        LoginVo loginVo = LoginVo.builder()
                .id("admin")
                .username("admin")
                .password("1")
                .build();
        JwtToken jwtToken = memberService.jwtToken(loginVo.getUsername(), loginVo.getPassword());

        // HttpHeaders 객체 생성 및 토큰 추가
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth(jwtToken.getAccessToken());
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<ApprovalVo> header = new HttpEntity<>(approvalVo, httpHeaders);

        String url = "http://127.0.0.1:" + randomServerPort + "/api/member/approval";
        ResponseEntity<MemberDto> responseEntity = testRestTemplate.exchange(url, HttpMethod.PUT, header, MemberDto.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        MemberDto updateMember = responseEntity.getBody();
        assertThat(updateMember.getStatus()).isEqualTo(approvalVo.getStatus());
    }

    /**
     * 5. 메뉴 생성
     */
    @Test
    public void menuCreate() {
        // jwt 토큰 생성
        LoginVo loginVo = LoginVo.builder()
                .id("admin")
                .username("admin")
                .password("1")
                .build();
        JwtToken jwtToken = memberService.jwtToken(loginVo.getUsername(), loginVo.getPassword());

        // HttpHeaders 객체 생성 및 토큰 추가
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth(jwtToken.getAccessToken());
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        // jwt 토큰 end

        Menu menu = Menu.builder()
                .id("c001")
                .name("테스트메뉴")
                .parent("u01")
                .build();

        HttpEntity<Menu> header = new HttpEntity<>(menu, httpHeaders);

        String url = "http://127.0.0.1:" + randomServerPort + "/api/menu/create";
        ResponseEntity<MenuDto> responseEntity = testRestTemplate.exchange(url, HttpMethod.POST, header, MenuDto.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        MenuDto menuDto = responseEntity.getBody();
        assertThat(menuDto.getId()).isEqualTo(menu.getId());
    }

    /**
     * 6. 그룹권한 생성
     */
    @Test
    public void roleGroupCreateTest() {
        // jwt 토큰 생성
        LoginVo loginVo = LoginVo.builder()
                .id("admin")
                .username("admin")
                .password("1")
                .build();
        JwtToken jwtToken = memberService.jwtToken(loginVo.getUsername(), loginVo.getPassword());

        // HttpHeaders 객체 생성 및 토큰 추가
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth(jwtToken.getAccessToken());
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        // jwt 토큰 end

        RoleGroup roleGroup = RoleGroup.builder()
                .roleId("usr3")
                .roleName("사용자권한3")
                .build();

        HttpEntity<RoleGroup> header = new HttpEntity<>(roleGroup, httpHeaders);

        String url = "http://127.0.0.1:" + randomServerPort + "/api/role/group/create";
        ResponseEntity<RoleGroupDto> responseEntity = testRestTemplate.exchange(url, HttpMethod.POST, header, RoleGroupDto.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        RoleGroupDto roleGroupDto = responseEntity.getBody();
        assertThat(roleGroupDto.getRoleId()).isEqualTo(roleGroup.getRoleId());
    }

    /**
     * 7. 그룹권한 수정
     */
    @Test
    public void roleGroupUpdateTest() {
        // jwt 토큰 생성
        LoginVo loginVo = LoginVo.builder()
                .id("admin")
                .username("admin")
                .password("1")
                .build();
        JwtToken jwtToken = memberService.jwtToken(loginVo.getUsername(), loginVo.getPassword());

        // HttpHeaders 객체 생성 및 토큰 추가
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth(jwtToken.getAccessToken());
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        // jwt 토큰 end

        RoleGroup roleGroup = RoleGroup.builder()
                .roleId("usr3")
                .roleName("사용자권한3 수정")
                .build();

        HttpEntity<RoleGroup> header = new HttpEntity<>(roleGroup, httpHeaders);

        String url = "http://127.0.0.1:" + randomServerPort + "/api/role/group/update";
        ResponseEntity<RoleGroupDto> responseEntity = testRestTemplate.exchange(url, HttpMethod.PUT, header, RoleGroupDto.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        RoleGroupDto roleGroupDto = responseEntity.getBody();
        assertThat(roleGroupDto.getRoleName()).isEqualTo(roleGroup.getRoleName());
    }

    /**
     * 8. 그룹권한 삭제
     */
    @Test
    public void roleGroupDeleteTest() {
        // jwt 토큰 생성
        LoginVo loginVo = LoginVo.builder()
                .id("admin")
                .username("admin")
                .password("1")
                .build();
        JwtToken jwtToken = memberService.jwtToken(loginVo.getUsername(), loginVo.getPassword());

        // HttpHeaders 객체 생성 및 토큰 추가
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth(jwtToken.getAccessToken());
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        // jwt 토큰 end

        String roleId = "usr3";

        HttpEntity header = new HttpEntity(httpHeaders);

        String url = "http://127.0.0.1:" + randomServerPort + "/api/role/group/delete/" + roleId;
        ResponseEntity<String> responseEntity = testRestTemplate.exchange(url, HttpMethod.DELETE, header, String.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    /**
     * 9. 메뉴권한 생성
     */
    @Test
    public void menuRoleCreateTest() {
        // jwt 토큰 생성
        LoginVo loginVo = LoginVo.builder()
                .id("admin")
                .username("admin")
                .password("1")
                .build();
        JwtToken jwtToken = memberService.jwtToken(loginVo.getUsername(), loginVo.getPassword());

        // HttpHeaders 객체 생성 및 토큰 추가
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth(jwtToken.getAccessToken());
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        // jwt 토큰 end

        MenuRoleVo menuRoleVo = MenuRoleVo.builder()
                .menuId("u01")
                .roleGroupId("usr2")
                .role("R")
                .build();
        HttpEntity<MenuRoleVo> header = new HttpEntity<>(menuRoleVo, httpHeaders);

        String url = "http://127.0.0.1:" + randomServerPort + "/api/menu/role/create";
        ResponseEntity<MenuRoleDto> responseEntity = testRestTemplate.exchange(url, HttpMethod.POST, header, MenuRoleDto.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        MenuRoleDto menuRoleDto = responseEntity.getBody();
        assertThat(menuRoleDto.getRole()).isEqualTo(menuRoleVo.getRole());

    }

    /**
     * 10. 유저권한 생성
     */
    @Test
    public void userRoleCreateTest() {
        // jwt 토큰 생성
        LoginVo loginVo = LoginVo.builder()
                .id("admin")
                .username("admin")
                .password("1")
                .build();
        JwtToken jwtToken = memberService.jwtToken(loginVo.getUsername(), loginVo.getPassword());

        // HttpHeaders 객체 생성 및 토큰 추가
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth(jwtToken.getAccessToken());
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        // jwt 토큰 end
        UserRoleVo userRoleVo = UserRoleVo.builder()
                .memberId("test1")
                .roleGroupId("usr2")
                .build();
        HttpEntity<UserRoleVo> header = new HttpEntity<>(userRoleVo, httpHeaders);

        String url = "http://127.0.0.1:" + randomServerPort + "/api/user/role/create";
        ResponseEntity<UserRoleDto> responseEntity = testRestTemplate.exchange(url, HttpMethod.POST, header, UserRoleDto.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

}

