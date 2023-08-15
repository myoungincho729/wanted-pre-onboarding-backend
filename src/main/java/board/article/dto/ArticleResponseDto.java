package board.article.dto;

import board.article.entity.Article;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ArticleResponseDto {
    private Long id;
    private String title;
    private String content;
    private String writerEmail;

    @Builder
    public ArticleResponseDto(Long id, String title, String content, String writerEmail) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.writerEmail = writerEmail;
    }

    public static ArticleResponseDto from(Article savedArticle) {
        return ArticleResponseDto.builder()
                .id(savedArticle.getArticleId())
                .title(savedArticle.getTitle())
                .content(savedArticle.getContent())
                .writerEmail(savedArticle.getMember().getEmail())
                .build();
    }
}
