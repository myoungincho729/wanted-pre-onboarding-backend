package board.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class Response<T> {
    private String error;
    private T data;

    @Builder
    public Response(String error, T data) {
        this.error = error;
        this.data = data;
    }

}
