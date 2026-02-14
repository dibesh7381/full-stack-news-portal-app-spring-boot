package NewsPortal.com.example.NewsPortalApp.exception;

import NewsPortal.com.example.NewsPortalApp.dto.ApiResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Handle custom API exception
    @ExceptionHandler(CustomApiException.class)
    public ResponseEntity<ApiResponseDto<Object>> handleCustomException(
            CustomApiException ex) {

        ApiResponseDto<Object> response =
                new ApiResponseDto<>(false, ex.getMessage(), null);

        return new ResponseEntity<>(response, ex.getStatus());
    }

    // Handle all other exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponseDto<Object>> handleGenericException(
            Exception ex) {

        ApiResponseDto<Object> response =
                new ApiResponseDto<>(false, "Something went wrong", null);

        return ResponseEntity.internalServerError().body(response);
    }
}
