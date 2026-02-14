package NewsPortal.com.example.NewsPortalApp.service;

import NewsPortal.com.example.NewsPortalApp.config.CloudinaryConfig;
import NewsPortal.com.example.NewsPortalApp.dto.*;
import NewsPortal.com.example.NewsPortalApp.entity.News;
import NewsPortal.com.example.NewsPortalApp.entity.User;
import NewsPortal.com.example.NewsPortalApp.exception.CustomApiException;
import NewsPortal.com.example.NewsPortalApp.repository.NewsRepository;
import NewsPortal.com.example.NewsPortalApp.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class NewsService {

    private final NewsRepository newsRepository;
    private final UserRepository userRepository;
    private final CloudinaryConfig cloudinaryConfig;

    // CREATE NEWS
    public NewsResponseDto createNews(Long userId, CreateNewsRequestDto request) {

        User reporter = userRepository.findById(userId)
                .orElseThrow(() -> new CustomApiException(
                        HttpStatus.NOT_FOUND,
                        "User not found"
                ));

        if (!"REPORTER".equals(reporter.getRole())) {
            throw new CustomApiException(
                    HttpStatus.FORBIDDEN,
                    "Only reporters can add news"
            );
        }

        String imageUrl = cloudinaryConfig.uploadImage(request.getImage());

        News news = new News();
        news.setTitle(request.getTitle());
        news.setDescription(request.getDescription());
        news.setImageUrl(imageUrl);
        news.setReporter(reporter);

        News saved = newsRepository.save(news);

        System.out.println("CreatedAt after save: " + saved.getCreatedAt());

        return new NewsResponseDto(
                saved.getId(),
                saved.getTitle(),
                saved.getDescription(),
                saved.getImageUrl(),
                saved.getCreatedAt()
        );
    }

    // GET REPORTER NEWS
    public List<NewsResponseDto> getMyNews(Long userId) {

        List<News> newsList = newsRepository.findByReporterId(userId);

        return newsList.stream()
                .map(n -> new NewsResponseDto(
                        n.getId(),
                        n.getTitle(),
                        n.getDescription(),
                        n.getImageUrl(),
                        n.getCreatedAt()
                ))
                .toList();
    }

    // GET ALL NEWS (PUBLIC)
    public List<AllNewsResponseDto> getAllNews() {

        List<News> newsList = newsRepository.findAllByOrderByCreatedAtDesc();

        return newsList.stream()
                .map(n -> new AllNewsResponseDto(
                        n.getId(),
                        n.getTitle(),
                        n.getDescription(),
                        n.getImageUrl(),
                        n.getReporter().getId(),       // reporterId added
                        n.getReporter().getUsername(),
                        n.getCreatedAt()
                ))
                .toList();
    }


    // UPDATE NEWS
    public NewsResponseDto updateNews(Long userId, Long newsId, UpdateNewsRequestDto request) {

        News news = newsRepository.findById(newsId)
                .orElseThrow(() -> new CustomApiException(
                        HttpStatus.NOT_FOUND,
                        "News not found"
                ));

        if (!news.getReporter().getId().equals(userId)) {
            throw new CustomApiException(
                    HttpStatus.FORBIDDEN,
                    "You can update only your own news"
            );
        }

        news.setTitle(request.getTitle());
        news.setDescription(request.getDescription());

        if (request.getImage() != null && !request.getImage().isEmpty()) {
            String imageUrl = cloudinaryConfig.uploadImage(request.getImage());
            news.setImageUrl(imageUrl);
        }

        News updated = newsRepository.save(news);

        return new NewsResponseDto(
                updated.getId(),
                updated.getTitle(),
                updated.getDescription(),
                updated.getImageUrl(),
                updated.getCreatedAt()
        );
    }

    // DELETE NEWS
    public void deleteNews(Long userId, Long newsId) {

        News news = newsRepository.findById(newsId)
                .orElseThrow(() -> new CustomApiException(
                        HttpStatus.NOT_FOUND,
                        "News not found"
                ));

        if (!news.getReporter().getId().equals(userId)) {
            throw new CustomApiException(
                    HttpStatus.FORBIDDEN,
                    "You can delete only your own news"
            );
        }

        newsRepository.delete(news);
    }
}

