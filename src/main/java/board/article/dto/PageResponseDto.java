package board.article.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class PageResponseDto<T> {
    private List<T> list;
    private PageInfo pageInfo;

    @Builder
    public PageResponseDto(List<T> list, PageInfo pageInfo) {
        this.list = list;
        this.pageInfo = pageInfo;
    }
}
