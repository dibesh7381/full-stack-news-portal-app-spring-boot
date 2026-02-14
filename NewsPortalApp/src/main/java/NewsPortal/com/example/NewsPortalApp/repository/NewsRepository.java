package NewsPortal.com.example.NewsPortalApp.repository;

import NewsPortal.com.example.NewsPortalApp.entity.News;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NewsRepository extends JpaRepository<News, Long> {

    List<News> findByReporterId(Long reporterId);

    List<News> findAllByOrderByCreatedAtDesc();
}

