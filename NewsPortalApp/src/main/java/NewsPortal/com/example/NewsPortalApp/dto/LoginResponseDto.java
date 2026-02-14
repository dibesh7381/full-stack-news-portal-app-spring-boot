package NewsPortal.com.example.NewsPortalApp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponseDto {
    private Long userId;
    private String role;
    private String token;
}

