# 원티드 프리온보딩 백엔드 인턴십 - 선발 과제

---
### 지원자 : 조명인

---
### 애플리케이션 실행 방법 및 API 문서

- git clone 후에 적절한 환경변수를 설정합니다. 이후 ```./gradle build -> java -jar ./build/libs/board-0.0.1-SNAPSHOT.jar```를 실행합니다.
  - ```환경변수 : AWS_RDS_ENDPOINT, AWS_RDS_USERNAME, AWS_RDS_PASSWORD, JWT_SECRET_KEY```
  - 디비 없이 실행하려면 ```java -jar -Dspring.profiles.active=test ./build/libs/board-0.0.1-SNAPSHOT.jar```를 실행합니다.
- [배포링크](http://ec2-43-201-66-128.ap-northeast-2.compute.amazonaws.com:8082)
- [API문서](https://documenter.getpostman.com/view/22639502/2s9Xy6rVpe)

| NO. | Description   | Method | Path                   | Authorization | Request Parameters      |
| :-: | ------------- | ------ | ---------------------- | :-----------: | ----------------------- |
| 1   | 사용자 회원가입   | POST   | /user                  | X              | `body`  { email, password } |
| 2   | 사용자 로그인     | POST   | /user/login             | X             | `body`  { email, password } |
| 3   | 새로운 게시글 생성 | POST   | /article/create          | O             | `body`  { title, content } |
| 4   | 게시글 전체 조회  | GET    | /article/list             | X             | `query` <br>page(default = 1)<br>size(default = 5) |
| 5   | 특정 게시글 조회  | GET    | /article/{article_id}     | X             |                         |
| 6   | 특정 게시글 수정  | PUT    | /article/{article_id}     | O             | `body`  { title, content } |
| 7   | 특정 게시글 삭제  | DELETE | /article/{article_id}     | O             |                         |

---

### 테이블 구조

- [ERD](./wanted_ERD.pdf)

---

### 데모 영상 링크

- [유튜브 데모 영상](https://www.youtube.com/watch?v=FZ0MmPx4-oQ)

---

### 구현 방법 설명

- ```JAVA 17, Spring Boot 3.1.2, Spring Data JPA```를 사용하여 구현하였습니다.
- RestControllerAdvice를 활용하여 모든 예외처리를 하나의 클래스에서 하도록 구현했습니다.
- 예외처리를 할 때에 ExceptionCode라는 enum안에 비즈니스 로직의 에러에 해당하는 값과 메세지를 정의하였습니다.
- 모든 정상요청/예외의 응답 바디의 구조가 ```data/error```필드로 같도록 통일하였습니다.

- 정상요청 예시 
```js
{
    "error": null,
    "data": {
        "id": 6,
        "title": "asdf66",
        "content": "asdf166",
        "writerEmail": "myoungin2@naver.com"
    }
}
```
- 에러 예시 
```js
{
    "error": "Login needed",
    "data": null
}
```

- 테스트 환경과 프로덕션 환경을 분리하였습니다.
- 26개의 테스트를 작성하였으며, Jacoco 기준 93%의 테스트 커버리지를 달성했습니다. 테스트 환경은 H2 임베디드 데이터베이스를 사용합니다.
- AWS EC2에서 배포하였으며, AWS RDS(MySQL 8.0.32)를 연동했습니다.

---

- **과제 1. 사용자 회원가입 엔드포인트**
    - 유효성 검증을 위해 커스텀 어노테이션을 정의했습니다.(@EmailConstraint, @PasswordContraint)
- **과제 2. 사용자 로그인 엔드포인트**
    - JwtUtils라는 유틸 클래스를 정의하여 JWT 토큰을 생성/확인하는 메서드를 구현했습니다.
    - 유효성도 마찬가지로 과제 1에서 정의한 어노테이션을 사용했습니다.
- **과제 3. 새로운 게시글을 생성하는 엔드포인트**
    - 게시글은 로그인한 사용자, 즉 JWT토큰을 Authorization 헤더에 가지고 있는 요청만 받아들입니다.
    - 인터셉터를 정의하여 글 생성/수정/삭제 요청시 토큰을 확인한 후 쓰레드로컬에 이메일을 저장합니다.
- **과제 4. 게시글 목록을 조회하는 엔드포인트**
    - @RequestParam 어노테이션을 이용하여 현재 페이지, 페이지당 글의 개수를 파라미터로 받고 그 값에 해당하는 글 목록을 반환합니다.
    - PageInfo라는 필드를 추가하여 글 목록에 대한 메타데이터를 반환하도록 하였습니다.
    - 파라미터의 디폴트값은 page=1, size=5입니다. 양수 유효성 체크를 진행하였습니다.
    - Pageable.of(page, size) 메서드를 사용하여 페이지네이션을 구현했습니다.
- **과제 5. 특정 게시글을 조회하는 엔드포인트**
    - Repository.findById(articleId)메서드를 이용하여 글을 찾은 후 반환하였습니다.
    - 글이 없을 경우 에러를 반환합니다.
- **과제 6. 특정 게시글을 수정하는 엔드포인트**
    - 인터셉터에서 로그인한 유저의 토큰을 확인한 후 쓰레드로컬에 이메일을 저장합니다.
    - 게시글을 찾은 후 작성자의 아이디가 같은 경우에만 수정하도록 구현하였습니다.
- **과제 7. 특정 게시글을 삭제하는 엔드포인트**
    - 게시글을 찾은 후 작성자의 아이디가 같은 경우에만 삭제할 수 있도록 구현하였습니다.
