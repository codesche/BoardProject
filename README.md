# ShortBoard(모두의 기록)

## **프로젝트 주제**

- **누구나 이용할 수 있는 커뮤니티 게시판**

## **프로젝트 기간**
- **22.12.30 - 23.1.22**

## **프로젝트 구조**
![image](https://user-images.githubusercontent.com/110509654/213926327-1607e20d-ac0b-45bd-8919-fba04a611c24.png)


## **ERD**
![image](https://user-images.githubusercontent.com/110509654/213927600-4503b28d-fb65-4997-97e4-675a3fb837d7.png)


- member : 회원 테이블
- board : 게시판 테이블
- login_history : 사용자 접속 내역

## 사용 기술 스택
- **Language : Java, HTML, CSS, JavaScript**
- **DBMS : MySQL**
- **Java Version : Java 11**
- **IDE : Intellij IDEA 2022.2.3 (Ultimate Edition)**
- **etc : Thymeleaf, BootStrap, Ajax**
- **Terminal : putty, Git Bash**
- **release : AWS EC2, RDS, filezilla**
- **ORM : mybatis**

## 핵심 API 정리 
![image](https://user-images.githubusercontent.com/110509654/213930406-fd5ad202-0150-458f-984a-0f45a9bb5945.png)

![image](https://user-images.githubusercontent.com/110509654/213930449-eda34856-ed46-4539-a9cd-7c3f0d64fab5.png)

![image](https://user-images.githubusercontent.com/110509654/213930545-641d4060-e431-432a-8bd2-941ffb6d20e6.png)

## 프로젝트 기능

**1. 회원가입**
- 회원가입에 필요한 정보 - 아이디, 비밀번호, 확인 비밀번호, 이름, 전화번호, 성별, 이메일, 우편번호, 주소, 상세 주소
- 회원가입시 등록한 이메일로 인증 메일 전송됨
- 이메일에서 "가입 완료" 버튼 누르면 회원상태가 활성화됨

**2. 로그인**
- ID, PW 입력
- 메일인증 안 된 상태에서 로그인할 경우 로그인 안 됨
- 정지된 회원, 탈퇴된 회원 로그인 안 됨
- 비밀번호 초기화 : 가입시 입력한 이메일 입력 후 비밀번호 초기화 과정 진행 (이메일 인증 필요)

**3. 메인페이지**
- 메인 페이지 하단에 주기적으로 이미지 슬라이드 변경
- 유저가 양쪽에 있는 화살표 통해 수동으로 넘길 수 있음
- DropMenu 구성하여 로그인, 회원가입, 로그아웃, 마이페이지 메뉴 구성

**4. 회원 목록**
- 로그인한 유저에 한해서 회원 아이디 조회 가능

**5. 마이페이지**
- 로그인 후 DropMenu에 있는 마이페이지 클릭시 회원 상세정보 조회 가능
- 회원정보 수정, 비밀번호 변경, 회원 탈퇴 기능 적용

**6. 게시판**
- 유저가 로그인하지 않을 경우 로그인 화면으로 전환 (로그인을 해야 게시판 글 조회 가능)
- 게시판 목록 조건별로 조회 가능하도록 적용 (조건 : 제목, 작성자, 작성일)
- 게시판 페이징 기능 처리
- 게시판 글쓰기 가능
- 제목 클릭하면 게시판 상세내용 조회 및 수정 가능
- 유저는 자기가 쓴 글만 수정 및 삭제할 수 있음 (화면에서 글 수정 버튼 클릭후 수정, 삭제 가능하도록 적용)

**7. 관리자 모드**
- 관리자는 관리자 화면의 회원 관리 메뉴에서 가입한 회원들의 상세내용 조회 가능
- 아이디 클릭하면 상세내용 조회 가능 (아이디, 이름, 연락처, 회원 상태, 로그인 히스토리 내역 등)
- 관리자는 회원의 회원 상태와 비밀번호를 변경할 수 있음

=> 회원상태 유형 - **ING**: 활성화, **STOP**: 정지, **WITHDRAW:** 탈퇴

**8. 배포**
- AWS에서 MySQL RDS 생성 후 EC2와 연동
- Elastic IP Address 생성
- filezila 통해 build한 파일을 Elastic IP와 연동된 Amazon EC2로 전송하여 배포 작업 진행

