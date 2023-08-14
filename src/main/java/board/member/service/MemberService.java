package board.member.service;

import board.member.dto.LoginDto;
import board.member.dto.MemberDto;
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

    public MemberResponseDto createMember(MemberDto memberDto) {
        verifyNewEmail(memberDto.getEmail());
        //password encrypt
        Member member = Member.from(memberDto.getEmail(), memberDto.getPassword());
        member = memberRepository.save(member);
        return MemberResponseDto.fromMember(member);
    }

    public String login(LoginDto loginDto) {
        Member member = getExistingMember(loginDto.getEmail());
        if (!member.checkPassword(loginDto.getPassword())) {
            throw new RuntimeException("password doesn't match");
        }
        // generate jwt token
        return "token";
    }

    private Member getExistingMember(String email) {
        Optional<Member> optionalMember = memberRepository.findByEmail(email);
        if (optionalMember.isEmpty()) {
            throw new RuntimeException("email not exists");
        }
        return optionalMember.get();
    }

    private void verifyNewEmail(String email) {
        if (memberRepository.findByEmail(email).isPresent()) {
            throw new RuntimeException("email already exists");
        }
    }
}
