# 게시판 프로젝트

## **프로젝트 주제**

- **SpringBoot를 활용한 게시판 프로젝트**

## **프로젝트 구조**
![image](https://user-images.githubusercontent.com/110509654/209464571-2bbabb3d-717e-4f0e-91ab-e8715f7f8c48.png)

## **ERD**
![image](https://user-images.githubusercontent.com/110509654/209901522-66e524f7-6113-443b-9743-8bf4d2cec29a.png)

- MEMBER : 회원 테이블
- BORAD : 게시판 테이블 (Board -> BOARD)

## 사용 기술 스택
- **Language : Java, HTML, CSS, JavaScript**
- **DBMS : MySQL (Amazon RDS 활용)**
- **Java Version : Java 11**
- **IDE : Intellij IDEA 2022.2.3 (Ultimate Edition)**
- **etc : Thymeleaf, BootStrap, MyBatis**
- **배포 : AWS**

## 프로젝트 기능

**회원가입과 로그인**
- 회원가입
    - 회원가입에 필요한 정보 - 이메일, 이름, 전화번호, 성별, 비밀번호
- 로그인
    - 로그인시 회원가입한 적 없는 이메일을 이용하여 로그인을 시도할 경우 에러 발생
    - 로그인시 비밀번호가 일치하지 않는다면 에러 발생

**게시판 글 작성**
- 로그인한 유저만 게시글 작성

**게시판 글 목록 조회**
- 로그인한 유저에 한해 게시판 글 조회

**게시판 글 상세보기**
- 게시판의 글은 로그인한 유저만 볼 수 있음
    - 유저가 로그인하지 않았다면 에러를 발생
- 게시판 글 상세보기에서는 제목, 작성자, 본문, 작성일, 수정일의 내용 확인 가능

**게시판 글 수정**
- 유저는 자신이 쓴 게시글만 수정

**게시판 글 삭제**
- 유저는 자신이 쓴 게시글만 삭제

