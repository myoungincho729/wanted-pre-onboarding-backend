package board.advice;

import board.response.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalControllerAdvice {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity validationFail(BindingResult bindingResult) {
        Response response = Response.builder()
                .error(bindingResult.getFieldError().getDefaultMessage())
                .build();
        return ResponseEntity.badRequest().body(response);
    }
}
