package NewsPortal.com.example.NewsPortalApp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HomeResponseDto {
    private String title;
    private String content;
}
