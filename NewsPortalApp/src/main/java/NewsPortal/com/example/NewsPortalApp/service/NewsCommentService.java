package NewsPortal.com.example.NewsPortalApp.service;

import NewsPortal.com.example.NewsPortalApp.dto.CommentRequestDto;
import NewsPortal.com.example.NewsPortalApp.dto.CommentResponseDto;
import NewsPortal.com.example.NewsPortalApp.dto.CommentUpdateDto;
import NewsPortal.com.example.NewsPortalApp.entity.News;
import NewsPortal.com.example.NewsPortalApp.entity.NewsComment;
import NewsPortal.com.example.NewsPortalApp.entity.User;
import NewsPortal.com.example.NewsPortalApp.exception.CustomApiException;
import NewsPortal.com.example.NewsPortalApp.repository.NewsCommentRepository;
import NewsPortal.com.example.NewsPortalApp.repository.NewsRepository;
import NewsPortal.com.example.NewsPortalApp.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class NewsCommentService {

    private final NewsCommentRepository commentRepository;
    private final NewsRepository newsRepository;
    private final UserRepository userRepository;

    // ADD COMMENT
    public CommentResponseDto addComment(Long userId, Long newsId, CommentRequestDto request) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomApiException(
                        HttpStatus.NOT_FOUND,
                        "User not found"
                ));

        News news = newsRepository.findById(newsId)
                .orElseThrow(() -> new CustomApiException(
                        HttpStatus.NOT_FOUND,
                        "News not found"
                ));

        // Reporter cannot comment on own news
        if (news.getReporter().getId().equals(userId)) {
            throw new CustomApiException(
                    HttpStatus.FORBIDDEN,
                    "You cannot comment on your own news"
            );
        }

        NewsComment comment = new NewsComment();
        comment.setUser(user);
        comment.setNews(news);
        comment.setContent(request.getContent());

        NewsComment saved = commentRepository.save(comment);

        return new CommentResponseDto(
                saved.getId(),
                user.getId(),
                user.getUsername(),
                saved.getContent(),
                saved.getCreatedAt()
        );
    }

    // GET COMMENTS BY NEWS
    public List<CommentResponseDto> getCommentsByNews(Long newsId) {

        newsRepository.findById(newsId)
                .orElseThrow(() -> new CustomApiException(
                        HttpStatus.NOT_FOUND,
                        "News not found"
                ));

        return commentRepository
                .findByNewsIdOrderByCreatedAtDesc(newsId)
                .stream()
                .map(c -> new CommentResponseDto(
                        c.getId(),
                        c.getUser().getId(),     // added
                        c.getUser().getUsername(),
                        c.getContent(),
                        c.getCreatedAt()
                ))
                .toList();
    }

    // UPDATE COMMENT
    public CommentResponseDto updateComment(
            Long userId,
            Long commentId,
            CommentUpdateDto request
    ) {

        NewsComment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CustomApiException(
                        HttpStatus.NOT_FOUND,
                        "Comment not found"
                ));

        if (!comment.getUser().getId().equals(userId)) {
            throw new CustomApiException(
                    HttpStatus.FORBIDDEN,
                    "You can update only your own comment"
            );
        }

        comment.setContent(request.getContent());
        NewsComment updated = commentRepository.save(comment);

        return new CommentResponseDto(
                updated.getId(),
                updated.getUser().getId(),   // added
                updated.getUser().getUsername(),
                updated.getContent(),
                updated.getCreatedAt()
        );
    }

    // DELETE COMMENT
    public void deleteComment(Long userId, Long commentId) {

        NewsComment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CustomApiException(
                        HttpStatus.NOT_FOUND,
                        "Comment not found"
                ));

        if (!comment.getUser().getId().equals(userId)) {
            throw new CustomApiException(
                    HttpStatus.FORBIDDEN,
                    "You can delete only your own comment"
            );
        }

        commentRepository.delete(comment);
    }
}
