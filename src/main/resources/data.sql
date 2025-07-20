INSERT INTO users (username, password, enabled, apikey, email) VALUES ('henk', '$2a$12$tJ1OH9MNbJVoMn53ZeDXQeN1vNK/tN0zO530T0dExTY1y6guOoNni', true, '7847493', 'test@testy.tst');
INSERT INTO users (username, password, enabled, apikey, email) VALUES ('jim', '$2a$12$tJ1OH9MNbJVoMn53ZeDXQeN1vNK/tN0zO530T0dExTY1y6guOoNni', true, '7847494', 'test@testy.test');

INSERT INTO user_profiles (avatar, bio) VALUES (null, 'Admin user for testing');
INSERT INTO user_profiles (avatar, bio) VALUES (null, 'Regular user for testing');

UPDATE users SET user_profile_id = 1 WHERE username = 'henk';
UPDATE users SET user_profile_id = 2 WHERE username = 'jim';

INSERT INTO roles (username, role) VALUES ('henk', 'USER');
INSERT INTO roles (username, role) VALUES ('henk', 'ADMIN');
INSERT INTO roles (username, role) VALUES ('jim', 'USER');

INSERT INTO users_roles (user_id, role_username, role_name) VALUES ('henk', 'henk', 'USER');
INSERT INTO users_roles (user_id, role_username, role_name) VALUES ('henk', 'henk', 'ADMIN');
INSERT INTO users_roles (user_id, role_username, role_name) VALUES ('jim', 'jim', 'USER');

INSERT INTO games (title, description, category, file_path, developer_id) VALUES
('Pixel Adventure', 'A classic 2D platformer with retro pixel art style.', 'PLATFORMER', '/games/pixel-adventure/game.zip', 'henk'),
('Space Strategy Command', 'Build your galactic empire in this turn-based strategy game.', 'STRATEGY', '/games/space-strategy/game.zip', 'henk');

INSERT INTO game_reviews (rating, comment, game_id, user_id, upvotes, downvotes) VALUES
(5, 'Amazing retro platformer! Brings back memories of classic games.', 1, 'jim', 3, 0),
(4, 'Great gameplay mechanics, but could use more levels.', 1, 'henk', 2, 1),
(5, 'Excellent strategy game with deep gameplay.', 2, 'jim', 4, 0);

INSERT INTO game_jams (name, description, rules, theme, game_jam_image_url, start_date, end_date, created_at, max_participants, current_participants, is_active) VALUES
('Summer Indie Game Jam 2025', 'Join us for an exciting 48-hour game development challenge! Create amazing indie games with fellow developers from around the world.', 'Games must be created within the 48-hour timeframe. Teams of up to 4 people allowed. All game engines and programming languages permitted. Submit your game before the deadline!', 'Time Travel', 'https://example.com/images/summer-jam-2025.jpg', '2025-08-15 18:00:00', '2025-08-17 18:00:00', '2025-07-01 10:00:00', 100, 2, true),
('Horror Game Challenge', 'Create spine-chilling horror games in this week-long game jam. Show us your scariest ideas and most atmospheric gameplay!', 'Games must contain horror elements. No excessive gore or inappropriate content. Original assets preferred but asset store items allowed if credited. Submit with gameplay video.', 'Haunted Memories', 'https://example.com/images/horror-jam.jpg', '2025-10-25 12:00:00', '2025-11-01 12:00:00', '2025-07-05 14:00:00', 50, 0, true),
('Retro Arcade Fest', 'Bring back the golden age of gaming! Create games inspired by classic arcade titles from the 80s and 90s.', 'Games must have retro/arcade aesthetics. Pixel art encouraged. Classic gameplay mechanics preferred. Keep it simple and fun!', 'Neon Nights', 'https://example.com/images/retro-jam.jpg', '2025-06-01 09:00:00', '2025-06-07 21:00:00', '2025-05-15 08:00:00', 75, 12, false);

INSERT INTO game_jam_participants (game_jam_id, user_id, joined_at, submission_id, submission_date) VALUES
(1, 'henk', '2025-07-10 15:30:00', null, null),
(1, 'jim', '2025-07-12 09:15:00', null, null),
(3, 'henk', '2025-05-20 11:00:00', 1, '2025-06-06 18:30:00'),
(3, 'jim', '2025-05-22 14:45:00', null, null);
