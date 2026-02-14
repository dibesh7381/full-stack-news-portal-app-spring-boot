package NewsPortal.com.example.NewsPortalApp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class AllNewsResponseDto {

    private Long id;
    private String title;
    private String description;
    private String imageUrl;
    private Long reporterId;      // ← add this
    private String reporterName;
    private LocalDateTime createdAt;
}

