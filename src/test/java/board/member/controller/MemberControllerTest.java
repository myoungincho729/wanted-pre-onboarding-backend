package board.member.controller;

import board.member.dto.LoginDto;
import board.member.dto.MemberPostDto;
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
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
public class MemberControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private Gson gson;

    @BeforeEach
    void memberSetup() throws Exception {
        MemberPostDto postDto = new MemberPostDto("m1@gmail.com", "12345678");
        String content = gson.toJson(postDto);

        // when
        ResultActions actions =
                mockMvc.perform(
                        post("/user")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(content)
                );
    }

    @DisplayName("회원가입 - 정상동작")
    @Test
    public void postMemberTest1() throws Exception {
        // given
        MemberPostDto postDto = new MemberPostDto("myoungin@gmail.com", "12345678");
        String content = gson.toJson(postDto);

        // when
        ResultActions actions =
                mockMvc.perform(
                        post("/user")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(content)
                );

        // then
        actions
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.data.email").value(postDto.getEmail()))
                .andExpect(jsonPath("$.error").isEmpty());
    }

    @DisplayName("회원가입 - 중복이메일 에러")
    @Test
    public void postMemberTest2() throws Exception {
        // given
        MemberPostDto postDto = new MemberPostDto("myoungin@gmail.com", "12345678");
        String content = gson.toJson(postDto);
        MemberPostDto postDto2 = new MemberPostDto("myoungin@gmail.com", "12345678");
        String content2 = gson.toJson(postDto);

        // when
        ResultActions actions =
                mockMvc.perform(
                        post("/user")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(content)
                );
        ResultActions actions2 =
                mockMvc.perform(
                        post("/user")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(content2)
                );

        // then
        actions2
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(jsonPath("$.error").value("Email exists"));
    }

    @DisplayName("회원가입 - 이메일 유효성 에러")
    @Test
    public void postMemberTest3() throws Exception {
        // given
        MemberPostDto postDto = new MemberPostDto("myoungingmail.com", "12345678");
        String content = gson.toJson(postDto);

        // when
        ResultActions actions =
                mockMvc.perform(
                        post("/user")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(content)
                );

        // then
        actions
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(jsonPath("$.error").value("Email 안에는 @가 포함되어야 합니다."));
    }

    @DisplayName("회원가입 - 비밀번호 유효성 에러")
    @Test
    public void postMemberTest4() throws Exception {
        // given
        MemberPostDto postDto = new MemberPostDto("myoungin@gmail.com", "1234567");
        String content = gson.toJson(postDto);

        // when
        ResultActions actions =
                mockMvc.perform(
                        post("/user")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(content)
                );

        // then
        actions
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(jsonPath("$.error").value("Password는 8자 이상이어야 합니다."));
    }

    @DisplayName("로그인 - 비밀번호 다름 에러")
    @Test
    void loginTest2() throws Exception {
        //given
        LoginDto loginDto = new LoginDto("m1@gmail.com", "123456789");
        String content = gson.toJson(loginDto);

        //when
        ResultActions actions = mockMvc.perform(
                post("/user/login")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
        );

        //then
        actions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(jsonPath("$.error").value("Password not match"));
    }

    @DisplayName("로그인 - 이메일 다름 에러")
    @Test
    void loginTest3() throws Exception {
        //given
        LoginDto loginDto = new LoginDto("m2@gmail.com", "12345678");
        String content = gson.toJson(loginDto);

        //when
        ResultActions actions = mockMvc.perform(
                post("/user/login")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
        );

        //then
        actions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(jsonPath("$.error").value("Member not found"));
    }
}
