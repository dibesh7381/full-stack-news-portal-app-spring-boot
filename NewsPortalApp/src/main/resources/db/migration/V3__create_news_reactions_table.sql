CREATE TABLE news_reactions (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    news_id BIGINT NOT NULL,
    reaction VARCHAR(10) NOT NULL, -- LIKE or DISLIKE
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_reaction_user
        FOREIGN KEY (user_id)
        REFERENCES users(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_reaction_news
        FOREIGN KEY (news_id)
        REFERENCES news(id)
        ON DELETE CASCADE
);
