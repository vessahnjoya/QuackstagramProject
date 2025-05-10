-- ======================================================
-- View: most_liked_posts
-- Purpose: Identify posts with more than 2 likes 
-- Category: Content Popularity
-- Uses: GROUP BY + HAVING
-- ======================================================
CREATE VIEW most_liked_posts AS
SELECT post_id, COUNT(*) AS like_count
FROM like_table
GROUP BY post_id
HAVING COUNT(*) > 2;
-- ======================================================
-- View: top_active_users
-- Purpose: Identify users who are highly active in liking posts
-- Category: User Behavior
-- Uses: GROUP BY + HAVING
-- ======================================================
CREATE VIEW top_active_users AS
SELECT u.username, COUNT(*) AS total_likes_given
FROM users u
JOIN like_table l ON u.user_id = l.user_id
GROUP BY u.username
HAVING COUNT(*) > 2;
-- ======================================================
-- View: avg_likes_per_post
-- Purpose: Calculate the average number of likes per post across the system
-- Category: System Analytics
-- Uses: Subquery
-- ======================================================
CREATE VIEW avg_likes_per_post AS
SELECT AVG(like_counts.like_count) AS avg_likes
FROM (
    SELECT post_id, COUNT(*) AS like_count
    FROM like_table
    GROUP BY post_id
) AS like_counts;

-- ======================================================
-- Index: idx_like_post_id
-- Purpose: Speeds up post-level aggregations (e.g., most_liked_posts)
-- ======================================================
CREATE INDEX idx_like_post_id ON like_table(post_id);


-- ======================================================
-- Index: idx_like_user_id
-- Purpose: Speeds up user-level aggregations (e.g., top_active_users)
-- ======================================================
CREATE INDEX idx_like_user_id ON like_table(user_id);