package NewsPortal.com.example.NewsPortalApp.repository;

import NewsPortal.com.example.NewsPortalApp.entity.News;
import NewsPortal.com.example.NewsPortalApp.dto.NewsResponseDto;
import NewsPortal.com.example.NewsPortalApp.dto.AllNewsResponseDto;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NewsRepository extends JpaRepository<News, Long> {

    // REPORTER OWN NEWS (JPQL + DTO)
    @Query("""
        SELECT new NewsPortal.com.example.NewsPortalApp.dto.NewsResponseDto(
            n.id,
            n.title,
            n.description,
            n.imageUrl,
            n.createdAt
        )
        FROM News n
        WHERE n.reporter.id = :reporterId
        ORDER BY n.createdAt DESC
    """)
    List<NewsResponseDto> findMyNews(@Param("reporterId") Long reporterId);


    // PUBLIC ALL NEWS (JPQL + DTO)
    @Query("""
        SELECT new NewsPortal.com.example.NewsPortalApp.dto.AllNewsResponseDto(
            n.id,
            n.title,
            n.description,
            n.imageUrl,
            n.reporter.id,
            n.reporter.username,
            n.createdAt
        )
        FROM News n
        ORDER BY n.createdAt DESC
    """)
    List<AllNewsResponseDto> findAllNewsDto();
}

