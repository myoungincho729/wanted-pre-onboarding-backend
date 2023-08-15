package board.member.service;

import board.member.dto.LoginDto;
import board.member.dto.MemberPostDto;
import board.member.dto.MemberResponseDto;
import board.member.repository.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class MemberServiceTest {
    @Autowired
    MemberService memberService;
    @Autowired
    MemberRepository memberRepository;

    @DisplayName("유저 생성 테스트 - 정상동작")
    @Test
    void memberCreateTest1() {
        //given
        String email = "myoungin@gmail.com";
        String password = "qwer1234";
        MemberPostDto memberDto = new MemberPostDto(email, password);

        //when
        MemberResponseDto memberResponseDto = memberService.createMember(memberDto);

        //then
        assertEquals(email, memberResponseDto.getEmail());
    }

    @DisplayName("유저 생성 테스트 - 에러(중복 이메일)")
    @Test
    void memberCreateTest2() {
        //given
        String email = "myoungin@gmail.com";
        String password = "qwer1234";
        MemberPostDto memberDto = new MemberPostDto(email, password);

        String email2 = "myoungin@gmail.com";
        String password2 = "1234qwer";
        MemberPostDto memberDto2 = new MemberPostDto(email2, password2);

        //when
        MemberResponseDto memberResponseDto = memberService.createMember(memberDto);

        //then
        Assertions.assertThatThrownBy(() -> {
            memberService.createMember(memberDto2);
        }).isInstanceOf(RuntimeException.class);
    }

//    @DisplayName("로그인 테스트(jwt 적용 전) - 정상동작")
//    @Test
    void loginTest1() {
        //given
        String email = "myoungin@gmail.com";
        String password = "qwer1234";
        MemberPostDto memberDto = new MemberPostDto(email, password);

        LoginDto loginDto = new LoginDto(email, password);

        //when
        MemberResponseDto memberResponseDto = memberService.createMember(memberDto);
        String token = memberService.login(loginDto).getToken();

        //then
        assertEquals(token, "token");
    }

    @DisplayName("로그인 테스트(jwt 적용 전) - 에러")
    @Test
    void loginTest2() {
        //given
        String email = "myoungin@gmail.com";
        String password = "qwer1234";
        MemberPostDto memberDto = new MemberPostDto(email, password);

        LoginDto loginDto = new LoginDto(email, "1234qwer");

        //when
        MemberResponseDto memberResponseDto = memberService.createMember(memberDto);

        //then
        Assertions.assertThatThrownBy(
                () -> memberService.login(loginDto)
        ).isInstanceOf(RuntimeException.class);
    }

    @DisplayName("로그인 테스트(jwt 적용) - 정상동작")
    @Test
    void loginTest3() {
        //given
        String email = "myoungin@gmail.com";
        String password = "qwer1234";
        MemberPostDto memberDto = new MemberPostDto(email, password);

        LoginDto loginDto = new LoginDto(email, "qwer1234");

        //when
        MemberResponseDto memberResponseDto = memberService.createMember(memberDto);

        //then
        String token = memberService.login(loginDto).getToken();
        System.out.println(token);
    }

}