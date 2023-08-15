package board.article.controller;

import board.article.dto.ArticlePostDto;
import board.article.dto.ArticleResponseDto;
import board.article.service.ArticleService;
import board.jwt.JwtUtils;
import board.member.controller.MemberController;
import board.member.dto.LoginDto;
import board.member.dto.LoginResponseDto;
import board.member.dto.MemberPostDto;
import board.member.service.MemberService;
import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Transactional
class ArticleControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    Gson gson;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    MemberService memberService;

    @Autowired
    ArticleService articleService;

    String email = "m1@gmail.com";
    String password = "12345678";
    String email2 = "m2@gmail.com";
    String password2 = "12345678";
    String token;
    String token2;
    @BeforeEach
    void loginSetup() {
        MemberPostDto postDto = new MemberPostDto(email, password);
        LoginDto loginDto = new LoginDto(email, password);
        memberService.createMember(postDto);
        LoginResponseDto loginResponseDto = memberService.login(loginDto);

        token = loginResponseDto.getToken();

        MemberPostDto postDto2 = new MemberPostDto(email2, password2);
        LoginDto loginDto2 = new LoginDto(email2, password2);
        memberService.createMember(postDto2);
        LoginResponseDto loginResponseDto2 = memberService.login(loginDto2);

        token2 = loginResponseDto2.getToken();
    }

    @DisplayName("게시글 생성 - 정상동작")
    @Test
    void createArticleTest1() throws Exception {
        //given
        String title = "article1";
        String content = "hello spring";
        ArticlePostDto articlePostDto = new ArticlePostDto(title, content);
        String articleContent = gson.toJson(articlePostDto);

        //when
        ResultActions actions =
                mockMvc.perform(
                        post("/article/create")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(articleContent)
                                .header("Authorization", token)
                );

        //then
        actions
                .andExpect(jsonPath("$.data.title").value(articlePostDto.getTitle()))
                .andExpect(jsonPath("$.data.content").value(articlePostDto.getContent()))
                .andExpect(jsonPath("$.data.writerEmail").value(email));
    }

    @DisplayName("게시글 생성 - 로그인 토큰 없음 에러")
    @Test
    void createArticleTest2() throws Exception {
        //given
        String title = "article1";
        String content = "hello spring";
        ArticlePostDto articlePostDto = new ArticlePostDto(title, content);
        String articleContent = gson.toJson(articlePostDto);

        //when
        ResultActions actions =
                mockMvc.perform(
                        post("/article/create")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(articleContent)
                );

        //then
        actions
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(jsonPath("$.error").value("Login needed"));
    }

    @DisplayName("게시글 생성 - 로그인 잘못된 토큰 에러")
    @Test
    void createArticleTest3() throws Exception {
        //given
        String title = "article1";
        String content = "hello spring";
        ArticlePostDto articlePostDto = new ArticlePostDto(title, content);
        String articleContent = gson.toJson(articlePostDto);

        //when
        ResultActions actions =
                mockMvc.perform(
                        post("/article/create")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(articleContent)
                                .header("Authorization", "nananana")
                );

        //then
        actions
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(jsonPath("$.error").value("Login needed"));
    }

    @DisplayName("게시글 생성 - 토큰 만료 에러")
    @Test
    void createArticleTest4() throws Exception {
        //given
        String title = "article1";
        String content = "hello spring";
        ArticlePostDto articlePostDto = new ArticlePostDto(title, content);
        String articleContent = gson.toJson(articlePostDto);
        String expiredToken = "Bearer " + jwtUtils.createExpiredToken(email);

        //when
        ResultActions actions =
                mockMvc.perform(
                        post("/article/create")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(articleContent)
                                .header("Authorization", expiredToken)
                );

        //then
        actions
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(jsonPath("$.error").value("Token expired. Please login again."));
    }

    @DisplayName("게시글 생성 - 이상한 토큰값 에러")
    @Test
    void createArticleTest5() throws Exception {
        //given
        String title = "article1";
        String content = "hello spring";
        ArticlePostDto articlePostDto = new ArticlePostDto(title, content);
        String articleContent = gson.toJson(articlePostDto);
        String invalidToken = "Bearer hello spring";

        //when
        ResultActions actions =
                mockMvc.perform(
                        post("/article/create")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(articleContent)
                                .header("Authorization", invalidToken)
                );

        //then
        actions
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(jsonPath("$.error").value("Token invalid"));
    }

    @DisplayName("게시글 수정 - 정상동작")
    @Test
    void updateArticleTest1() throws Exception {
        //given
        String title = "article1";
        String content = "hello spring";
        ArticlePostDto articlePostDto = new ArticlePostDto(title, content);
        ArticleResponseDto responseDto = articleService.createArticle(email, articlePostDto);

        String newTitle = "new article";
        String newContent = "new hello spring";
        ArticlePostDto newArticlePostDto = new ArticlePostDto(newTitle, newContent);
        Long articleId = responseDto.getId();
        String articleContent = gson.toJson(newArticlePostDto);

        //when
        ResultActions actions =
                mockMvc.perform(
                        patch("/article/update/" + articleId)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(articleContent)
                                .header("Authorization", token)
                );

        //then
        actions
                .andExpect(jsonPath("$.data.title").value(newArticlePostDto.getTitle()))
                .andExpect(jsonPath("$.data.content").value(newArticlePostDto.getContent()))
                .andExpect(jsonPath("$.data.writerEmail").value(email));
    }

    @DisplayName("게시글 수정 - 멤버 아닌 경우 에러")
    @Test
    void updateArticleTest2() throws Exception {
        //given
        String title = "article1";
        String content = "hello spring";
        ArticlePostDto articlePostDto = new ArticlePostDto(title, content);
        ArticleResponseDto responseDto = articleService.createArticle(email, articlePostDto);

        String newTitle = "new article";
        String newContent = "new hello spring";
        ArticlePostDto newArticlePostDto = new ArticlePostDto(newTitle, newContent);
        Long articleId = responseDto.getId();
        String articleContent = gson.toJson(newArticlePostDto);

        //when
        ResultActions actions =
                mockMvc.perform(
                        patch("/article/update/" + articleId)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(articleContent)
                                .header("Authorization", token2)
                );

        //then
        actions
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(jsonPath("$.error").value("Article is not written by member"));
    }

    @DisplayName("게시글 수정 - 게시글 id 없음 에러")
    @Test
    void updateArticleTest3() throws Exception {
        //given
        String title = "article1";
        String content = "hello spring";
        ArticlePostDto articlePostDto = new ArticlePostDto(title, content);
        ArticleResponseDto responseDto = articleService.createArticle(email, articlePostDto);

        String newTitle = "new article";
        String newContent = "new hello spring";
        ArticlePostDto newArticlePostDto = new ArticlePostDto(newTitle, newContent);
        Long articleId = responseDto.getId() + 1;
        String articleContent = gson.toJson(newArticlePostDto);

        //when
        ResultActions actions =
                mockMvc.perform(
                        patch("/article/update/" + articleId)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(articleContent)
                                .header("Authorization", token)
                );

        //then
        actions
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(jsonPath("$.error").value("Article not found"));
    }

    @DisplayName("게시글 삭제 - 정상동작")
    @Test
    void deleteArticleTest1() throws Exception {
        //given
        String title = "article1";
        String content = "hello spring";
        ArticlePostDto articlePostDto = new ArticlePostDto(title, content);
        ArticleResponseDto responseDto = articleService.createArticle(email, articlePostDto);
        Long articleId = responseDto.getId();

        //when
        ResultActions actions =
                mockMvc.perform(
                        delete("/article/delete/" + articleId)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", token)
                );

        //then
        actions
                .andExpect(status().isNoContent());
    }

    @DisplayName("게시글 삭제 - 작성자 아님 에러")
    @Test
    void deleteArticleTest2() throws Exception {
        //given
        String title = "article1";
        String content = "hello spring";
        ArticlePostDto articlePostDto = new ArticlePostDto(title, content);
        ArticleResponseDto responseDto = articleService.createArticle(email, articlePostDto);
        Long articleId = responseDto.getId();

        //when
        ResultActions actions =
                mockMvc.perform(
                        delete("/article/delete/" + articleId)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", token2)
                );

        //then
        actions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(jsonPath("$.error").value("Article is not written by member"));
    }

    @DisplayName("게시글 조회 - 정상동작")
    @Test
    void getArticleTest1() throws Exception {
        //given
        String title = "article1";
        String content = "hello spring";
        ArticlePostDto articlePostDto = new ArticlePostDto(title, content);
        ArticleResponseDto responseDto = articleService.createArticle(email, articlePostDto);
        Long articleId = responseDto.getId();

        //when
        ResultActions actions =
                mockMvc.perform(
                        get("/article/" + articleId)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                );

        //then
        actions
                .andExpect(jsonPath("$.data.title").value(articlePostDto.getTitle()))
                .andExpect(jsonPath("$.data.content").value(articlePostDto.getContent()))
                .andExpect(jsonPath("$.data.writerEmail").value(email));
    }

    @DisplayName("게시글 조회 - 게시글 id 없음 에러")
    @Test
    void getArticleTest2() throws Exception {
        //given
        String title = "article1";
        String content = "hello spring";
        ArticlePostDto articlePostDto = new ArticlePostDto(title, content);
        ArticleResponseDto responseDto = articleService.createArticle(email, articlePostDto);
        Long articleId = responseDto.getId() + 12345;

        //when
        ResultActions actions =
                mockMvc.perform(
                        get("/article/" + articleId)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                );

        //then
        actions
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(jsonPath("$.error").value("Article not found"));
    }
}