package NewsPortal.com.example.NewsPortalApp.repository;

import NewsPortal.com.example.NewsPortalApp.entity.NewsReaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NewsReactionRepository extends JpaRepository<NewsReaction, Long> {

    Optional<NewsReaction> findByUserIdAndNewsId(Long userId, Long newsId);

    long countByNewsIdAndReaction(Long newsId, String reaction);
}

