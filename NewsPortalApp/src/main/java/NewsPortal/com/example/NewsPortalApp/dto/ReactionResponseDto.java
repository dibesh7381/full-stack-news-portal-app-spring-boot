package NewsPortal.com.example.NewsPortalApp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReactionResponseDto {
    private long likes;
    private long dislikes;
    private String userReaction; // LIKE, DISLIKE, or null
}


