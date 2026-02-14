package NewsPortal.com.example.NewsPortalApp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProfileResponseDto {
    private String username;
    private String email;
    private String role;
}
