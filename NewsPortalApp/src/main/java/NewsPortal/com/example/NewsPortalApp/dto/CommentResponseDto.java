package NewsPortal.com.example.NewsPortalApp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class CommentResponseDto {
    private Long id;
    private Long userId;       // add this
    private String username;
    private String content;
    private LocalDateTime createdAt;
}

