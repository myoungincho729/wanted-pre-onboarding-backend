package board.article.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ArticleUpdateDto {
    @NotBlank(message = "제목은 값이 있어야 합니다.")
    private String title;
    @NotBlank(message = "내용은 값이 있어야 합니다.")
    private String content;

    public ArticleUpdateDto(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
