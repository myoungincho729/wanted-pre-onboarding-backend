package board.member.dto;

import jakarta.validation.*;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

class ValidationTest {
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    Validator validator = factory.getValidator();

    @DisplayName("유효성 테스트 - 정상동작")
    @Test
    void validationTest1() {
        //given
        List<String> emails = List.of("aaa@gmail.com", "myoungin@gmail.com", "@", ".@");
        List<String> passwords = List.of("12345678", "123456789", "qwer1234", "qwerqwer");

        //when
        for (int index = 0; index < emails.size(); index++) {
            LoginDto loginDto = new LoginDto(emails.get(index), passwords.get(index));
            Set<ConstraintViolation<LoginDto>> violations = validator.validate(loginDto);

            Assertions.assertThat(violations.size()).isEqualTo(0);
        }
    }

    @DisplayName("유효성 테스트 - 예외")
    @Test
    void validationTest2() {
        //given
        List<String> emails = List.of("aaagmail.com", "myoungin@gmail.com", "...");
        List<String> passwords = List.of("12345678", "123456", "1234");
        List<Integer> results = List.of(1, 1, 2);

        //when
        for (int index = 0; index < emails.size(); index++) {
            LoginDto loginDto = new LoginDto(emails.get(index), passwords.get(index));
            Set<ConstraintViolation<LoginDto>> violations = validator.validate(loginDto);

            Assertions.assertThat(violations.size()).isEqualTo(results.get(index));
        }
    }

}