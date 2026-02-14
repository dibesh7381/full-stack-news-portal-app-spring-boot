package NewsPortal.com.example.NewsPortalApp.repository;

import NewsPortal.com.example.NewsPortalApp.entity.NewsComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NewsCommentRepository extends JpaRepository<NewsComment, Long> {

    List<NewsComment> findByNewsIdOrderByCreatedAtDesc(Long newsId);

}

