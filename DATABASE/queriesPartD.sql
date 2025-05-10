--Question 1

SELECT u.username, COUNT(f.followed_id) AS follower_count
FROM users u
JOIN follow f ON u.user_id = f.followed_id
GROUP BY u.username
HAVING COUNT(f.followed_id) > 1;

--Question 2

SELECT u.username, COUNT(p.post_id) AS total_posts
FROM users u
LEFT JOIN post p ON u.user_id = p.user_id
GROUP BY u.username;

--Question 3

SELECT c.comment_text, c.time_stamp, u.username AS commenter
FROM comment c
JOIN post p ON c.post_id = p.post_id
JOIN users target ON p.user_id = target.user_id
JOIN users u ON c.user_id = u.user_id
WHERE target.username = X; -- X depends on the username you choose

--Question 4

SELECT p.post_id, u.username, COUNT(l.user_id) AS like_count
FROM post p
JOIN users u ON p.user_id = u.user_id
LEFT JOIN like_table l ON p.post_id = l.post_id
GROUP BY p.post_id, u.username
ORDER BY like_count DESC
LIMIT 3;

--Question 5

SELECT u.username, COUNT(l.post_id) AS likes_given
FROM users u
LEFT JOIN like_table l ON u.user_id = l.user_id
GROUP BY u.username;

--Question 6

SELECT u.username
FROM users u
LEFT JOIN post p ON u.user_id = p.user_id
WHERE p.post_id IS NULL;

--Question 7

SELECT u1.username AS user1, u2.username AS user2
FROM follow f1
JOIN follow f2 ON f1.follower_id = f2.followed_id AND f1.followed_id = f2.follower_id
JOIN users u1 ON f1.follower_id = u1.user_id
JOIN users u2 ON f1.followed_id = u2.user_id
WHERE u1.user_id < u2.user_id; -- prevents duplicate pairs

--Question 8

SELECT u.username, COUNT(p.post_id) AS post_count
FROM users u
JOIN post p ON u.user_id = p.user_id
GROUP BY u.username
ORDER BY post count DESC
LIMIT 1; -- else will return every user who posted at least once

--Question 9

SELECT u.username, COUNT(f.follower_id) AS follower_count
FROM users u
LEFT JOIN follow f ON u.user_id = f.followed_id
GROUP BY u.username
ORDER BY follower_count DESC
LIMIT 3;

--Question 10

SELECT post_id FROM post
WHERE like_count >= (SELECT COUNT(user_id) FROM users); --The > is because a post has 20 likes so we count it

--Question 11

SELECT p.user_id, MAX(like_count) AS max_likes FROM post p
LEFT JOIN top_active_users tau ON get_Username(p.post_id) = tau.username
GROUP BY p.user_id
ORDER BY max_likes DESC
LIMIT 3;
--Question 12

SELECT user_id, AVG(like_count) AS avg_likes FROM post
GROUP BY user_id
ORDER BY avg_likes DESC;

--Question 13

SELECT p.post_id from post p
left join comment c on p.post_id = c.post_id
group by p.post_id, p.like_count
having count(c.comment_id) > p.like_count;

--Question 14

SELECT DISTINCT lt.user_id
FROM like_table lt
WHERE NOT EXISTS (
SELECT p.post_id
FROM post p
WHERE p.user_id = X --X depends on the user_id you choose
AND NOT EXISTS (
SELECT 1
FROM like_table lt2
WHERE lt2.post_id = p.post_id
AND lt2.user_id = lt.user_id)
);

--Question 15

SELECT p1.user_id, p1.post_id, p1.like_count
FROM post p1
WHERE p1.post_id = (
SELECT p2.post_id
FROM post p2
WHERE p2.user_id = p1.user_id
ORDER BY p2.like_count desc
LIMIT 1
);

--Question 16

SELECT u.username,
COUNT(DISTINCT f1.follower_id) AS followers, COUNT(DISTINCT f2.followed_id)
AS following,
COUNT(DISTINCT f1.follower_id) / NULLIF(COUNT(DISTINCT f2.followed_id),
0) AS ratio FROM users u
LEFT JOIN follow f1 ON u.user_id = f1.followed_id
LEFT JOIN follow f2 ON u.user_id = f2.follower_id
GROUP BY u.user_id
ORDER BY ratio DESC;

--Question 17

SELECT DATE_FORMAT(time_stamp, '%Y-%m') AS post_month, COUNT(*)
AS total_posts
FROM post
GROUP BY post_month
ORDER BY total_posts DESC
Limit 1;

--Question 18

SELECT u.user_id, u.username FROM users u
WHERE u.user_id NOT IN (
SELECT l.user_id
FROM like_table l
JOIN post p ON l.post_id = p.post_id
WHERE p.user_id = X --X depends on the user_id you choose
UNION
SELECT c.user_id
FROM comment c
JOIN post p ON c.post_id = p.post_id
WHERE p.user_id = X)
order by u.user_id ASC;

--Question 19

SELECT followed_id AS user_id, COUNT(follower_id) AS follower_increase
FROM follow
WHERE time_stamp >= DATE_SUB('2025-05-08 17:01:00', INTERVAL X DAY) --X depends on the number of days you choose
GROUP BY followed_id
ORDER BY follower_increase DESC
LIMIT 1;

--Question 20

SELECT u.username, COUNT(f.follower_id) AS follower_count
FROM users u
JOIN follow f ON u.user_id = f.followed_id
GROUP BY u.user_id, u.username
HAVING COUNT(f.follower_id) > ( SELECT COUNT(*) * 0.1 FROM users);