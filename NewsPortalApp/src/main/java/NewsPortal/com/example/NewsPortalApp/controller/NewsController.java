package NewsPortal.com.example.NewsPortalApp.controller;

import NewsPortal.com.example.NewsPortalApp.dto.*;
import NewsPortal.com.example.NewsPortalApp.service.NewsService;
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
public class NewsController {

    private final NewsService newsService;

    // PUBLIC - GET ALL NEWS
    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponseDto<List<AllNewsResponseDto>>> getAllNews() {

        List<AllNewsResponseDto> response = newsService.getAllNews();

        ApiResponseDto<List<AllNewsResponseDto>> apiResponse =
                new ApiResponseDto<>(true, "All news fetched", response);

        return ResponseEntity.ok(apiResponse);
    }

    // REPORTER - CREATE NEWS
    @PostMapping
    @PreAuthorize("hasAuthority('REPORTER')")
    public ResponseEntity<ApiResponseDto<NewsResponseDto>> createNews(
            Authentication authentication,
            @ModelAttribute CreateNewsRequestDto request) {

        Long userId = (Long) authentication.getPrincipal();

        NewsResponseDto response = newsService.createNews(userId, request);

        ApiResponseDto<NewsResponseDto> apiResponse =
                new ApiResponseDto<>(true, "News created successfully", response);

        return ResponseEntity.ok(apiResponse);
    }

    // REPORTER - GET MY NEWS
    @GetMapping("/my")
    @PreAuthorize("hasAuthority('REPORTER')")
    public ResponseEntity<ApiResponseDto<List<NewsResponseDto>>> getMyNews(
            Authentication authentication) {

        Long userId = (Long) authentication.getPrincipal();

        List<NewsResponseDto> response = newsService.getMyNews(userId);

        ApiResponseDto<List<NewsResponseDto>> apiResponse =
                new ApiResponseDto<>(true, "Reporter news fetched", response);

        return ResponseEntity.ok(apiResponse);
    }

    // REPORTER - UPDATE NEWS
    @PutMapping("/{newsId}")
    @PreAuthorize("hasAuthority('REPORTER')")
    public ResponseEntity<ApiResponseDto<NewsResponseDto>> updateNews(
            @PathVariable Long newsId,
            Authentication authentication,
            @ModelAttribute UpdateNewsRequestDto request) {

        Long userId = (Long) authentication.getPrincipal();

        NewsResponseDto response =
                newsService.updateNews(userId, newsId, request);

        ApiResponseDto<NewsResponseDto> apiResponse =
                new ApiResponseDto<>(true, "News updated successfully", response);

        return ResponseEntity.ok(apiResponse);
    }

    // REPORTER - DELETE NEWS
    @DeleteMapping("/{newsId}")
    @PreAuthorize("hasAuthority('REPORTER')")
    public ResponseEntity<ApiResponseDto<Void>> deleteNews(
            @PathVariable Long newsId,
            Authentication authentication) {

        Long userId = (Long) authentication.getPrincipal();

        newsService.deleteNews(userId, newsId);

        ApiResponseDto<Void> apiResponse =
                new ApiResponseDto<>(true, "News deleted successfully", null);

        return ResponseEntity.ok(apiResponse);
    }
}
