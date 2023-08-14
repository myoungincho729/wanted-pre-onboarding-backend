package board.member.dto;

import board.validator.EmailConstraint;
import board.validator.PasswordConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

@Getter
@Setter
@NoArgsConstructor
@Validated
public class LoginDto {

    @EmailConstraint
    private String email;
    @PasswordConstraint
    private String password;

    public LoginDto(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
