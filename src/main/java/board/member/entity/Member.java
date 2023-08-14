package board.member.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;

    private String email;
    private String password;

    @Builder
    public Member(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public static Member from(String email, String password) {
        return Member.builder()
                .email(email)
                .password(password)
                .build();
    }

    public boolean checkPassword(String password) {
        return this.password.equals(password);
    }
}
