package board.member.dto;

import board.member.entity.Member;
import lombok.Builder;
import lombok.Getter;

@Getter
public class MemberResponseDto {
    private String email;

    @Builder
    public MemberResponseDto(String email) {
        this.email = email;
    }

    public static MemberResponseDto fromMember(Member member) {
        return MemberResponseDto.builder()
                .email(member.getEmail())
                .build();
    }
}
