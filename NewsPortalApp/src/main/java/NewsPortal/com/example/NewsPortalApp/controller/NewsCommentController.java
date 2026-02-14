package NewsPortal.com.example.NewsPortalApp.controller;

import NewsPortal.com.example.NewsPortalApp.dto.ApiResponseDto;
import NewsPortal.com.example.NewsPortalApp.dto.CommentRequestDto;
import NewsPortal.com.example.NewsPortalApp.dto.CommentResponseDto;
import NewsPortal.com.example.NewsPortalApp.dto.CommentUpdateDto;
import NewsPortal.com.example.NewsPortalApp.service.NewsCommentService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/news")
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class NewsCommentController {

    private final NewsCommentService commentService;

    // ADD COMMENT (AUTHENTICATED USER)
    @PostMapping("/{newsId}/comments")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponseDto<CommentResponseDto>> addComment(
            @PathVariable Long newsId,
            Authentication authentication,
            @RequestBody CommentRequestDto request
    ) {

        Long userId = (Long) authentication.getPrincipal();

        CommentResponseDto response =
                commentService.addComment(userId, newsId, request);

        ApiResponseDto<CommentResponseDto> apiResponse =
                new ApiResponseDto<>(true, "Comment added successfully", response);

        return ResponseEntity.ok(apiResponse);
    }

    // GET COMMENTS
    @GetMapping("/{newsId}/comments")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponseDto<List<CommentResponseDto>>> getComments(
            @PathVariable Long newsId
    ) {

        List<CommentResponseDto> response =
                commentService.getCommentsByNews(newsId);

        ApiResponseDto<List<CommentResponseDto>> apiResponse =
                new ApiResponseDto<>(true, "Comments fetched", response);

        return ResponseEntity.ok(apiResponse);
    }

    // UPDATE COMMENT
    @PutMapping("/{newsId}/comments/{commentId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponseDto<CommentResponseDto>> updateComment(
            @PathVariable Long newsId,
            @PathVariable Long commentId,
            Authentication authentication,
            @RequestBody CommentUpdateDto request
    ) {

        Long userId = (Long) authentication.getPrincipal();

        CommentResponseDto response =
                commentService.updateComment(userId, commentId, request);

        ApiResponseDto<CommentResponseDto> apiResponse =
                new ApiResponseDto<>(true, "Comment updated successfully", response);

        return ResponseEntity.ok(apiResponse);
    }

    // DELETE COMMENT
    @DeleteMapping("/{newsId}/comments/{commentId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponseDto<Void>> deleteComment(
            @PathVariable Long newsId,
            @PathVariable Long commentId,
            Authentication authentication
    ) {

        Long userId = (Long) authentication.getPrincipal();

        commentService.deleteComment(userId, commentId);

        ApiResponseDto<Void> apiResponse =
                new ApiResponseDto<>(true, "Comment deleted successfully", null);

        return ResponseEntity.ok(apiResponse);
    }
}
