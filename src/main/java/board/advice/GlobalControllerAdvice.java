package board.advice;

import board.response.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class GlobalControllerAdvice {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity validationFail(BindingResult bindingResult) {
        int size = bindingResult.getFieldErrors().size();
        System.out.println(size);
        Response response = Response.builder()
                .error(bindingResult.getFieldError().getDefaultMessage())
                .build();
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity validationFail(RuntimeException e) {
        Response response = Response.builder()
                .error(e.getMessage())
                .build();
        return ResponseEntity.badRequest().body(response);
    }
}
