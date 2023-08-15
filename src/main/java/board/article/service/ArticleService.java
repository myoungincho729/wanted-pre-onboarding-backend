package board.article.service;

import board.article.dto.*;
import board.article.entity.Article;
import board.article.repository.ArticleRepository;
import board.exception.BusinessException;
import board.exception.ExceptionCode;
import board.member.entity.Member;
import board.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ArticleService {
    private final ArticleRepository articleRepository;
    private final MemberRepository memberRepository;

    public ArticleResponseDto createArticle(String email, ArticlePostDto articleDto) {
        Member member = getExistingMember(email);
        Article article = Article.from(articleDto.getTitle(), articleDto.getContent(), member);
        Article savedArticle = articleRepository.save(article);
        return ArticleResponseDto.from(savedArticle);
    }

    public ArticleResponseDto updateArticle(Long articleId, ArticleUpdateDto updateDto, String email) {
        Member member = getExistingMember(email);
        Article article = getExistArticle(member, articleId);
        article.changeTitle(updateDto.getTitle());
        article.changeContent(updateDto.getContent());
        Article savedArticle = articleRepository.save(article);
        return ArticleResponseDto.from(savedArticle);
    }

    private Member getExistingMember(String email) {
        Optional<Member> optionalMember = memberRepository.findByEmail(email);
        if (optionalMember.isEmpty()) {
            throw new BusinessException(ExceptionCode.MEMBER_NOT_FOUND);
        }
        return optionalMember.get();
    }

    private Article getExistArticle(Member member, Long articleId) {
        Optional<Article> optionalArticle = articleRepository.findById(articleId);
        if (optionalArticle.isEmpty()) {
            throw new BusinessException(ExceptionCode.ARTICLE_NOT_FOUND);
        }
        if (!optionalArticle.get().isWriter(member)) {
            throw new BusinessException(ExceptionCode.ARTICLE_MEMBER_NOT_MATCH);
        }
        return optionalArticle.get();
    }

    public void deleteArticle(Long articleId, String email) {
        Member member = getExistingMember(email);
        Article article = getExistArticle(member, articleId);
        articleRepository.delete(article);
    }

    public ArticleResponseDto getArticle(Long articleId) {
        Article article = getExistArticleForAll(articleId);
        return ArticleResponseDto.from(article);
    }

    private Article getExistArticleForAll(Long articleId) {
        Optional<Article> optionalArticle = articleRepository.findById(articleId);
        if (optionalArticle.isEmpty()) {
            throw new BusinessException(ExceptionCode.ARTICLE_NOT_FOUND);
        }
        return optionalArticle.get();
    }

    public PageResponseDto getArticles(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page - 1, size);
        Page<Article> articles = articleRepository.findAllByPage(pageRequest);
        List<ArticleResponseDto> articleResponseDtos = articles.getContent().stream()
                .map(article -> ArticleResponseDto.from(article))
                .collect(Collectors.toList());
        PageInfo pageInfo = PageInfo.from(page, size, articles.getTotalElements(), articles.getTotalPages());
        return PageResponseDto.builder()
                .pageInfo(pageInfo)
                .list(Collections.singletonList(articleResponseDtos))
                .build();
    }
}
