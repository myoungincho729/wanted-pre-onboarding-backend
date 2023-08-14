package board.response;

public class Response<T> {
    private String error;
    private T data;

    public Response(String error, T data) {
        this.error = error;
        this.data = data;
    }

    public Response error(String message) {
        return new Response(message, null);
    }

    public Response data(T data) {
        return new Response(null, data);
    }
}
