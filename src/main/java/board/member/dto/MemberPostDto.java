package board.member.dto;

import board.validator.EmailConstraint;
import board.validator.PasswordConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MemberPostDto {
    @EmailConstraint
    private String email;
    @PasswordConstraint
    private String password;

    public MemberPostDto(String email, String password) {
        this.email = email;
        this.password = password;
    }
}