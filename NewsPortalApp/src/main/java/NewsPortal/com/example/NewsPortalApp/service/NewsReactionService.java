package NewsPortal.com.example.NewsPortalApp.service;

import NewsPortal.com.example.NewsPortalApp.dto.ReactionRequestDto;
import NewsPortal.com.example.NewsPortalApp.dto.ReactionResponseDto;
import NewsPortal.com.example.NewsPortalApp.entity.News;
import NewsPortal.com.example.NewsPortalApp.entity.NewsReaction;
import NewsPortal.com.example.NewsPortalApp.entity.User;
import NewsPortal.com.example.NewsPortalApp.exception.CustomApiException;
import NewsPortal.com.example.NewsPortalApp.repository.NewsReactionRepository;
import NewsPortal.com.example.NewsPortalApp.repository.NewsRepository;
import NewsPortal.com.example.NewsPortalApp.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class NewsReactionService {

    private final NewsReactionRepository reactionRepository;
    private final NewsRepository newsRepository;
    private final UserRepository userRepository;

    public ReactionResponseDto reactToNews(
            Long userId,
            Long newsId,
            ReactionRequestDto request
    ) {

        String reactionType = request.getReaction();

        if (!"LIKE".equals(reactionType) && !"DISLIKE".equals(reactionType)) {
            throw new CustomApiException(
                    HttpStatus.BAD_REQUEST,
                    "Reaction must be LIKE or DISLIKE"
            );
        }

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

        // Reporter cannot react to own news
        if (news.getReporter().getId().equals(userId)) {
            throw new CustomApiException(
                    HttpStatus.FORBIDDEN,
                    "You cannot react to your own news"
            );
        }

        // check existing reaction
        NewsReaction reaction = reactionRepository
                .findByUserIdAndNewsId(userId, newsId)
                .orElse(null);

        // TOGGLE LOGIC
        if (reaction != null && reaction.getReaction().equals(reactionType)) {
            reactionRepository.delete(reaction);
        } else {
            if (reaction == null) {
                reaction = new NewsReaction();
                reaction.setUser(user);
                reaction.setNews(news);
            }
            reaction.setReaction(reactionType);
            reactionRepository.save(reaction);
        }

        long likes = reactionRepository.countByNewsIdAndReaction(newsId, "LIKE");
        long dislikes = reactionRepository.countByNewsIdAndReaction(newsId, "DISLIKE");

        return new ReactionResponseDto(likes, dislikes);
    }

    public ReactionResponseDto getReactionCounts(Long newsId) {

        newsRepository.findById(newsId)
                .orElseThrow(() -> new CustomApiException(
                        HttpStatus.NOT_FOUND,
                        "News not found"
                ));

        long likes = reactionRepository.countByNewsIdAndReaction(newsId, "LIKE");
        long dislikes = reactionRepository.countByNewsIdAndReaction(newsId, "DISLIKE");

        return new ReactionResponseDto(likes, dislikes);
    }
}


