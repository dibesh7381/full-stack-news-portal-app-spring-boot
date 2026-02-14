package NewsPortal.com.example.NewsPortalApp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class NewsResponseDto {
    private Long id;
    private String title;
    private String description;
    private String imageUrl;
    private LocalDateTime createdAt;
}

