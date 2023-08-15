package board.member.service;

import board.exception.BusinessException;
import board.exception.ExceptionCode;
import board.jwt.JwtUtils;
import board.member.dto.LoginDto;
import board.member.dto.LoginResponseDto;
import board.member.dto.MemberPostDto;
import board.member.dto.MemberResponseDto;
import board.member.entity.Member;
import board.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    public MemberResponseDto createMember(MemberPostDto memberDto) {
        verifyNewEmail(memberDto.getEmail());
        //password encrypt
        String encodedPassword = passwordEncoder.encode(memberDto.getPassword());
        Member member = Member.from(memberDto.getEmail(), encodedPassword);
        member = memberRepository.save(member);
        return MemberResponseDto.fromMember(member);
    }

    public LoginResponseDto login(LoginDto loginDto) {
        Member member = getExistingMember(loginDto.getEmail());
        if (!member.correctPassword(passwordEncoder, loginDto.getPassword())) {
            throw new BusinessException(ExceptionCode.PASSWORD_NOT_MATCH);
        }
        // generate jwt token
        String token = jwtUtils.createToken(member.getEmail());
        return LoginResponseDto.from("Bearer " + token);
    }

    private Member getExistingMember(String email) {
        Optional<Member> optionalMember = memberRepository.findByEmail(email);
        if (optionalMember.isEmpty()) {
            throw new BusinessException(ExceptionCode.MEMBER_NOT_FOUND);
        }
        return optionalMember.get();
    }

    private void verifyNewEmail(String email) {
        if (memberRepository.findByEmail(email).isPresent()) {
            throw new BusinessException(ExceptionCode.EMAIL_EXISTS);
        }
    }
}
