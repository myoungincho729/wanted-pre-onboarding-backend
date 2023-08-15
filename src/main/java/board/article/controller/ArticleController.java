package board.article.controller;

import board.article.dto.ArticlePostDto;
import board.article.dto.ArticleResponseDto;
import board.article.dto.ArticleUpdateDto;
import board.article.dto.PageResponseDto;
import board.article.service.ArticleService;
import board.interceptor.JwtInterceptor;
import board.response.Response;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/article")
@RequiredArgsConstructor
public class ArticleController {
    private final ArticleService articleService;

    @PostMapping("/create")
    public ResponseEntity createArticle(@RequestBody @Valid ArticlePostDto articleDto) {
        ArticleResponseDto responseDto = articleService.createArticle(JwtInterceptor.threadLocal.get(), articleDto);
        return ResponseEntity.ok().body(
                Response.builder()
                .data(responseDto)
                .build()
        );
    }

    @PatchMapping("/update/{articleId}")
    public ResponseEntity updateArticle(
            @PathVariable Long articleId,
            @RequestBody @Valid ArticleUpdateDto updateDto) {
        ArticleResponseDto responseDto = articleService.updateArticle(articleId, updateDto, JwtInterceptor.threadLocal.get());
        return ResponseEntity.ok().body(
                Response.builder()
                        .data(responseDto)
                        .build()
        );
    }

    @DeleteMapping("/delete/{articleId}")
    public ResponseEntity deleteArticle(@PathVariable Long articleId) {
        articleService.deleteArticle(articleId, JwtInterceptor.threadLocal.get());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/list")
    public ResponseEntity getArticles(
           @RequestParam(defaultValue = "1") int page,
           @RequestParam(defaultValue = "5") int size
    ) {
        PageResponseDto pageResponseDto = articleService.getArticles(page, size);
        return ResponseEntity.ok().body(
                Response.builder()
                        .data(pageResponseDto)
                        .build()
        );
    }

    @GetMapping("/{articleId}")
    public ResponseEntity articleDetails(@PathVariable Long articleId) {
        ArticleResponseDto responseDto = articleService.getArticle(articleId);
        return ResponseEntity.ok().body(
                Response.builder()
                        .data(responseDto)
                        .build()
        );
    }
}
