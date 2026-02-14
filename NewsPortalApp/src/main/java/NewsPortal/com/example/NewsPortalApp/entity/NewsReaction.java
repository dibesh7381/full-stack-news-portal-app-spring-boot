package NewsPortal.com.example.NewsPortalApp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "news_reactions")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewsReaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "news_id", nullable = false)
    private News news;

    // Simple string instead of enum
    @Column(nullable = false)
    private String reaction; // "LIKE" or "DISLIKE"

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;
}


