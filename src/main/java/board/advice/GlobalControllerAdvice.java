package board.advice;

import board.exception.BusinessException;
import board.response.Response;
import io.jsonwebtoken.JwtException;
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

    @ExceptionHandler(JwtException.class)
    public ResponseEntity jwtException(JwtException e) {
        Response response = Response.builder()
                .error(e.getMessage())
                .build();
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity bussinessException(BusinessException e) {
        Response response = Response.builder()
                .error(e.getMessage())
                .build();
        return ResponseEntity.badRequest().body(response);
    }
}
