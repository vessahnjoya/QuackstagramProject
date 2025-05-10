CREATE TABLE users (
    user_id INT(11) AUTO_INCREMENT,
    username VARCHAR(50) UNIQUE NOT NULL,
    user_password VARCHAR(255) NOT NULL,
    bio TEXT,
    profile_picture_path VARCHAR(255),
    PRIMARY KEY (user_id)
);

CREATE TABLE post (
    post_id INT(11) AUTO_INCREMENT,
    user_id INT(11) NOT NULL,
    caption TEXT,
    image_path VARCHAR(255),
    time_stamp DATETIME DEFAULT CURRENT_TIMESTAMP,
    like_count INT(11) DEFAULT 0,
    PRIMARY KEY (post_id),
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);

CREATE TABLE message (
    message_id INT(11) AUTO_INCREMENT,
    sender_id INT(11) NOT NULL,
    receiver_id INT(11) NOT NULL,
    message_text TEXT NOT NULL,
    time_stamp DATETIME DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (message_id),
    FOREIGN KEY (sender_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (receiver_id) REFERENCES users(user_id) ON DELETE CASCADE
);

CREATE TABLE notification (
    notification_id INT(11) AUTO_INCREMENT,
    recipient_id INT(11) NOT NULL,
    sender_id INT(11) NOT NULL,
    post_id INT(11),
    message TEXT,
    time_stamp DATETIME DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (notification_id),
    FOREIGN KEY (recipient_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (sender_id) REFERENCES users(user_id) ON DELETE CASCADE,


    FOREIGN KEY (post_id) REFERENCES post(post_id) ON DELETE CASCADE
);

CREATE TABLE like_table (
    user_id INT(11) NOT NULL,
    post_id INT(11) NOT NULL,
    time_stamp DATETIME DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (user_id, post_id),
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (post_id) REFERENCES post(post_id) ON DELETE CASCADE
);

CREATE TABLE comment (
    comment_id INT(11) AUTO_INCREMENT,
    post_id INT(11) NOT NULL,
    time_stamp DATETIME DEFAULT CURRENT_TIMESTAMP,
    user_id INT(11) NOT NULL,
    comment_text TEXT NOT NULL,
    PRIMARY KEY (comment_id),
    FOREIGN KEY (post_id) REFERENCES post(post_id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);

CREATE TABLE follow (
    follower_id INT(11) NOT NULL,
    followed_id INT(11) NOT NULL,
    time_stamp DATETIME DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (follower_id, followed_id),
    FOREIGN KEY (follower_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (followed_id) REFERENCES users(user_id) ON DELETE CASCADE
);

CREATE TABLE `postlogs` (
  `log_id` int NOT NULL AUTO_INCREMENT,
  `message` text,
  PRIMARY KEY (`log_id`)
);

CREATE TABLE `follow_log` (
  `log_id` int NOT NULL AUTO_INCREMENT,
  `follow_message` text,
  PRIMARY KEY (`log_id`)
);

INSERT INTO users (username, user_password, bio, profile_picture_path) VALUES
('Louis', 'pslli', 'Bio', 'img/storage/profile/Louis.png'),
('abdul', 'inxel1', 'zzzzz', 'img/storage/profile/abdul.png'),
('Xylo', 'Piuuoapx', 'Fierce warrior, not solo', 'img/storage/profile/Xylo.png'),
('Zara', 'Piuuoapx', 'Humanoid robot much like the rest', 'img/storage/profile/Zara.png'),
('Lorin', 'Piuuoapx', 'For copyright reasons, I am not Grogu', 'img/storage/profile/Lorin.png'),
('Mystar', 'inxel', 'Xylo and I are not the same!', 'img/storage/profile/Mystar.png');

INSERT INTO message (sender_id, receiver_id, message_text, time_stamp) VALUES
(1, 6, 'wagwan', '2025-05-09 00:15:55'),
(1, 6, 'my guy', '2025-05-09 00:15:55'),
(1, 6, 'my friend', '2025-05-09 00:15:55');

INSERT INTO post (user_id, caption, image_path, time_stamp, like_count) VALUES
(5, 'In the cookie jar my hand was not.','img/uploaded/Lorin_1.png' , '2023-12-17 19:07:43', 1),
(5, 'Meditate I must.', 'img/uploaded/Lorin_2.png', '2023-12-17 19:09:35', 0),
(3, 'My tea strong as Force is.', 'img/uploaded/Xylo_1.png', '2023-12-17 19:22:40', 0),
(3, 'Jedi mind trick failed.', 'img/uploaded/Xylo_2.png', '2023-12-17 19:23:14', 0),
(4, 'Lost my map I have. Oops.', 'img/uploaded/Zara_1.png', '2023-12-17 19:24:31', 20),
(4, 'Yoga with Yoda', 'img/uploaded/Zara_2.png', '2023-12-17 19:25:03', 5),
(6, 'Cookies gone?', 'img/uploaded/Mystar_1.png', '2023-12-17 19:26:50', 1),
(6, 'In my soup a fly is.', 'img/uploaded/Mystar_2.png', '2023-12-17 19:27:24', 1),
(5, 'Enter a caption', 'img/uploaded/Lorin_3.png', '2025-02-24 19:01:24', 0),
(5, 'Enter a caption', 'img/uploaded/Lorin_4.png', '2025-02-25 22:44:26', 0),
(2, 'Enter a caption', 'img/uploaded/abdul_1.png', '2025-03-06 16:03:25', 0),
(2, 'post', 'img/uploaded/abdul_2.png', '2025-03-06 16:06:45', 0);

--Note: Only Insert these after creating the function get_Username
INSERT INTO notification (recipient_id, sender_id, post_id, message, time_stamp) VALUES
(5, 4, 1, concat(get_Username(4), ' Liked your post'), '2023-12-17 19:29:41'),
(6, 5, 7, concat(get_Username(5), ' Liked your post'), '2025-03-10 17:01:22');

INSERT INTO like_table (user_id, post_id, time_stamp) VALUES
(5, 5, '2025-05-09 00:00:00'), -- Lorin liking Zara_1
(5, 6, '2025-05-09 00:00:00'), -- Lorin liking Zara_2
(5, 8, '2025-05-09 00:00:00'), -- Lorin liking Mystar_2
(5, 7, '2025-05-09 00:00:00'); -- Lorin liking Mystar_1

INSERT INTO follow (follower_id, followed_id, time_stamp) VALUES
(3, 5, '2025-05-09 00:00:00'), -- Xylo follows Lorin
(4, 5, '2025-05-09 00:00:00'), -- Zara follows Lorin
(6, 5, '2025-05-09 00:00:00'), -- Mystar follows Lorin
(6, 4, '2025-05-09 00:00:00'), -- Mystar follows Zara
(5, 6, '2025-05-09 00:00:00'), -- Lorin follows Mystar
(5, 4, '2025-05-09 00:00:00'); -- Lorin follows Zara

INSERT INTO comment (post_id, time_stamp, user_id, comment_text) VALUES
(5, '2025-05-09 00:00:00', 5, 'great picture'),
(5, '2025-05-09 00:00:00', 5, 'amazing picture'),
(6, '2025-05-09 00:00:00', 5, 'looks great'),
(6, '2025-05-09 00:00:00', 5, 'looks great');