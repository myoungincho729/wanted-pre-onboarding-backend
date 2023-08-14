package board.member.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LoginResponseDto {
    private String token;

    @Builder
    public LoginResponseDto(String token) {
        this.token = token;
    }

    public static LoginResponseDto from(String token) {
        return LoginResponseDto.builder()
                .token(token)
                .build();
    }
}
