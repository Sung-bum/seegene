INIT DATA - seegene > ddl > script.txt

TABLE : MEMBER(유저테이블 - ID, NAME, PASSWORD, STATUS(w: 가입대기 s: 가입확정))
MEMBER_ROLES(토큰시 필요권한을 위한 테이블 현재 과제상에서는 test용도로만 사용 - MEMBER_ID, ROLES)
MENU(메뉴 테이블 - ID, NAME, PARENT)
MENU_ROLE(메뉴 권한 테이블 - MENUID, ROLEID, ROLE(CRUD 권한) )
USER_ROLE(유저 권한 테이블 - MEMBERID, ROLEID)
ROLE_GROUP(그룹권한 테이블, 가상의 그룹권한을 만들고 이를 유저권한에 부여하여 권한을 만든다 - ROLEID, ROLENAME)


*개발환경
JAVA 11
jwt 토큰 사용
H2 local 환경
JPA
Spring Framework 2.6.7

*사용자 필요 API
1. 가입 API - MemberController > singUp 
2. 로그인 API - LoginController > signIn 
3. 사용자별 메뉴 조회 API - MemberController > menuList

*어드민 필요 API
1. Menu CRUD - MenuController
2. Role CRUD
   - 권한 정의를 권한그룹을 만들고 그룹에 매핑하는 메뉴를 사용자에게 지정
   - RoleGroupController (그룹권한)
   - MenuRoleController (메뉴권한)
   - UserRoleController (유저권한)
3. 승인 API - MemberController > approvalMember

GIT 주소 : https://github.com/Sung-bum/seegene

TEST 작성 ( CRUD 몇몇 테스트 생략 )