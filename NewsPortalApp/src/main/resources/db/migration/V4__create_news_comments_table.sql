CREATE TABLE news_comments (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    news_id BIGINT NOT NULL,
    content TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_comment_user
        FOREIGN KEY (user_id)
        REFERENCES users(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_comment_news
        FOREIGN KEY (news_id)
        REFERENCES news(id)
        ON DELETE CASCADE
);
