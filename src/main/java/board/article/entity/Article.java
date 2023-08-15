package board.article.entity;

import board.member.entity.Member;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long articleId;

    private String title;

    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder
    public Article(String title, String content, Member member) {
        this.title = title;
        this.content = content;
        this.member = member;
    }

    public static Article from(String title, String content, Member member) {
        return Article.builder()
                .title(title)
                .content(content)
                .member(member)
                .build();
    }

    public boolean isWriter(Member member) {
        return this.member.getMemberId().equals(member.getMemberId());
    }

    public void changeTitle(String title) {
        if (title == null || title.isEmpty()) {
            return;
        }
        this.title = title;
    }

    public void changeContent(String content) {
        if (content == null || content.isEmpty()) {
            return;
        }
        this.content = content;
    }
}
