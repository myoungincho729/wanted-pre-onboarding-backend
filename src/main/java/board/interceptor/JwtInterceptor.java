package board.interceptor;

import board.exception.BusinessException;
import board.exception.ExceptionCode;
import board.jwt.JwtUtils;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.servlet.HandlerInterceptor;

@RequiredArgsConstructor
public class JwtInterceptor implements HandlerInterceptor {
    private final JwtUtils jwtUtils;
    public static ThreadLocal<String> threadLocal = new ThreadLocal<>();
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("Authorization");
        if (token == null) {
            throw new BusinessException(ExceptionCode.LOGIN_NEEDED);
        }
        if (!token.startsWith("Bearer ")) {
            throw new BusinessException(ExceptionCode.LOGIN_NEEDED);
        }
        try {
            String email = jwtUtils.getEmailFromToken(token.substring(7));
            if (email == null) {
                throw new BusinessException(ExceptionCode.LOGIN_NEEDED);
            }
            threadLocal.set(email);
        } catch (JwtException e) {
            throw e;
        }
        return true;
    }
}
