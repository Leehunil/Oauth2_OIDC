package ohsoontaxi.login.global.error;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import ohsoontaxi.login.global.error.exception.ErrorCode;
import ohsoontaxi.login.global.error.exception.OhSoonException;
import org.springframework.http.MediaType;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class ExceptionFilter extends OncePerRequestFilter {

    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (OhSoonException e) {
            writeErrorResponse(response, e.getErrorCode(), request.getRequestURL().toString());
        } catch (Exception e) {
            if (e.getCause() instanceof OhSoonException) {
                writeErrorResponse(
                        response,
                        ((OhSoonException) e.getCause()).getErrorCode(),
                        request.getRequestURL().toString());
            } else {
                e.printStackTrace();
                writeErrorResponse(
                        response,
                        ErrorCode.INTERNAL_SERVER_ERROR,
                        request.getRequestURL().toString());
            }
        }
    }

    private void writeErrorResponse(HttpServletResponse response, ErrorCode errorCode, String path) throws IOException {

        ErrorResponse errorResponse = new ErrorResponse(errorCode.getStatus(), errorCode.getReason(), path);

        response.setStatus(errorCode.getStatus());
        response.setCharacterEncoding("UTF-8");
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }
}

