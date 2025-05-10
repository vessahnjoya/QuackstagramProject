DELIMITER //

CREATE PROCEDURE save_post(
    IN new_post_id INT,
    IN new_user_id INT,
    IN new_caption VARCHAR(225),
    IN image_path VARCHAR(225),
    IN new_time_stamp DATETIME,
    IN new_like_count INT
)
BEGIN
    INSERT INTO post (post_id, user_id, caption, image_path, time_stamp, like_count)
    VALUES (new_post_id, new_user_id, new_caption, image_path, new_time_stamp, new_like_count);
END //

DELIMITER ;

DELIMITER //

CREATE PROCEDURE save_comment(
IN new_comment_id INT,
IN new_post_id INT,
IN new_time_stamp DATETIME,
IN new_user_id INT,
IN comment_text TEXT)
BEGIN
	INSERT INTO comment (comment_id, post_id, time_stamp, user_id, comment_text)
	VALUES (new_comment_id, new_post_id, new_time_stamp, new_user_id, comment_text);
END //

DELIMITER ;

DELIMITER //

CREATE PROCEDURE register_new_user(
IN new_user_id INT,
IN new_username VARCHAR(50),
IN new_user_password varchar(225),
IN new_bio TEXT,
IN new_profile_picture_path varchar(225))
BEGIN 
	INSERT INTO users(user_id, username, user_password, bio, profile_picture_path)
	VALUES (new_user_id, new_username, new_user_password, new_bio, new_profile_picture_path);
END //

DELIMITER ;

DELIMITER // 

CREATE PROCEDURE followUser(
IN new_follower_id INT,
IN new_followed_id INT,
IN new_time_stamp DATETIME)
BEGIN
	INSERT INTO follow(follower_id, followed_id, time_stamp)
	VALUES (new_follower_id, new_followed_id, new_time_stamp);
END //

DELIMITER; 

DELIMITER //

CREATE FUNCTION get_Username(this_user_id INT) RETURNS VARCHAR(50)
    DETERMINISTIC
BEGIN
	DECLARE this_username VARCHAR(50);
	SET this_username = (SELECT username FROM users WHERE user_id = this_user_id);
RETURN this_username;
END //

DELIMITER ;

DELIMITER //

CREATE FUNCTION getUser_id(this_username VARCHAR(50)) RETURNS INT
    DETERMINISTIC
BEGIN
    DECLARE user_id INT;
    SET user_id = (SELECT u.user_id FROM users u WHERE LOWER(u.username) = LOWER(this_username));
    IF user_id IS NULL THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Username not found';
    END IF;
    RETURN user_id;
END //

DELIMITER ;

DELIMITER //

CREATE FUNCTION count_users_posts(id INT) RETURNS INT
    DETERMINISTIC
BEGIN
    DECLARE post_count INT;
    SELECT count(*) INTO post_count
    FROM post
    WHERE user_id  = id;
    RETURN post_count;
END //

DELIMITER ;

DELIMITER //

CREATE TRIGGER `log_after_following` AFTER INSERT ON follow 
FOR EACH ROW 
BEGIN
	INSERT INTO follow_log(follow_message)
	values(
    CONCAT(get_Username(new.follower_id), ' has followed ', get_Username(new.followed_id))
    );
END //

DELIMITER ;

DELIMITER //

CREATE TRIGGER after_inser_in_post_table AFTER INSERT ON post
 FOR EACH ROW 
 BEGIN
	DECLARE post_count INT;
	SET post_count = count_users_posts(NEW.user_id);
	
	INSERT INTO postlogs(message)
	values (
	CONCAT(get_Username(NEW.user_id), ' has ',post_count, ' posts')
	);
END //

DELIMITER ;