package board.member.controller;

import board.member.dto.LoginDto;
import board.member.dto.LoginResponseDto;
import board.member.dto.MemberDto;
import board.member.dto.MemberResponseDto;
import board.member.service.MemberService;
import board.response.Response;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @PostMapping
    public ResponseEntity join(@RequestBody @Valid MemberDto memberDto) {
        MemberResponseDto memberResponseDto = memberService.createMember(memberDto);
        Response response = Response.builder()
                .data(memberResponseDto)
                .build();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody @Valid LoginDto loginDto) {
        LoginResponseDto token = memberService.login(loginDto);
        Response response = Response.builder()
                .data(token)
                .build();
        return ResponseEntity.ok()
                .body(response);
    }
}
