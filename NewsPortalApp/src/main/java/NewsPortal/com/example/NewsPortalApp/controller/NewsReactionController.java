package NewsPortal.com.example.NewsPortalApp.controller;

import NewsPortal.com.example.NewsPortalApp.dto.ApiResponseDto;
import NewsPortal.com.example.NewsPortalApp.dto.ReactionRequestDto;
import NewsPortal.com.example.NewsPortalApp.dto.ReactionResponseDto;
import NewsPortal.com.example.NewsPortalApp.service.NewsReactionService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reactions")
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class NewsReactionController {

    private final NewsReactionService reactionService;

    // ADD or UPDATE reaction (LIKE / DISLIKE)
    @PostMapping("/{newsId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponseDto<ReactionResponseDto>> reactToNews(
            @PathVariable Long newsId,
            @RequestBody ReactionRequestDto request,
            Authentication authentication) {

        Long userId = (Long) authentication.getPrincipal();

        ReactionResponseDto response =
                reactionService.reactToNews(userId, newsId, request);

        return ResponseEntity.ok(
                new ApiResponseDto<>(true, "Reaction updated", response)
        );
    }

    // GET reaction counts
    @GetMapping("/{newsId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponseDto<ReactionResponseDto>> getReactions(
            @PathVariable Long newsId) {

        ReactionResponseDto response =
                reactionService.getReactionCounts(newsId);

        return ResponseEntity.ok(
                new ApiResponseDto<>(true, "Reaction counts fetched", response)
        );
    }
}

