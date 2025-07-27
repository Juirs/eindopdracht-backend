INSERT INTO users (username, password, enabled, apikey, email) VALUES ('henk', '$2a$12$tJ1OH9MNbJVoMn53ZeDXQeN1vNK/tN0zO530T0dExTY1y6guOoNni', true, '7847493', 'test@testy.tst');
INSERT INTO users (username, password, enabled, apikey, email) VALUES ('jim', '$2a$12$tJ1OH9MNbJVoMn53ZeDXQeN1vNK/tN0zO530T0dExTY1y6guOoNni', true, '7847494', 'test@testy.test');
INSERT INTO users (username, password, enabled, apikey, email) VALUES ('tim', '$2a$12$tJ1OH9MNbJVoMn53ZeDXQeN1vNK/tN0zO530T0dExTY1y6guOoNni', true, '7847495', 'testy@testy.test');

INSERT INTO user_profiles (avatar, bio) VALUES (null, 'Admin user for testing');
INSERT INTO user_profiles (avatar, bio) VALUES (null, 'Developer user for testing');
INSERT INTO user_profiles (avatar, bio) VALUES (null, 'Regular user for testing');


UPDATE users SET user_profile_id = 1 WHERE username = 'henk';
UPDATE users SET user_profile_id = 2 WHERE username = 'jim';
UPDATE users SET user_profile_id = 3 WHERE username = 'tim';

INSERT INTO roles (username, role) VALUES ('henk', 'USER');
INSERT INTO roles (username, role) VALUES ('henk', 'ADMIN');
INSERT INTO roles (username, role) VALUES ('henk', 'DEVELOPER');
INSERT INTO roles (username, role) VALUES ('jim', 'USER');
INSERT INTO roles (username, role) VALUES ('jim', 'DEVELOPER');
INSERT INTO roles (username, role) VALUES ('tim', 'USER');


INSERT INTO users_roles (user_id, role_username, role_name) VALUES ('henk', 'henk', 'USER');
INSERT INTO users_roles (user_id, role_username, role_name) VALUES ('henk', 'henk', 'ADMIN');
INSERT INTO users_roles (user_id, role_username, role_name) VALUES ('henk', 'henk', 'DEVELOPER');
INSERT INTO users_roles (user_id, role_username, role_name) VALUES ('jim', 'jim', 'USER');
INSERT INTO users_roles (user_id, role_username, role_name) VALUES ('jim', 'jim', 'DEVELOPER');
INSERT INTO users_roles (user_id, role_username, role_name) VALUES ('tim', 'tim', 'USER');

-- Insert games with online placeholder images for easier demo
INSERT INTO games (title, description, category, game_file_path, image_url, trailer_url, developer_id) VALUES
('Pixel Adventure', 'A classic 2D platformer with retro pixel art style. Jump through challenging levels, collect power-ups, and defeat enemies in this nostalgic gaming experience.', 'PLATFORMER', null, 'https://images.nintendolife.com/d44e40b6e0a3d/1280x720.jpg', 'https://www.youtube.com/embed/dQw4w9WgXcQ', 'henk'),
('Star Citizen', 'Build your galactic empire in this turn-based strategy game. Research new technologies, manage resources, and command fleets in epic space battles.', 'STRATEGY', null, 'https://eu-images.contentstack.com/v3/assets/bltbc1876152fcd9f07/blte715551c07483dda/648aef89f2c16960d58ce7c4/StarCitizenNew.jpg', 'https://www.youtube.com/embed/dQw4w9WgXcQ', 'henk'),
('Mystery Manor', 'Solve puzzles and uncover secrets in this atmospheric adventure game. Explore a haunted mansion filled with riddles and supernatural mysteries.', 'ADVENTURE', null, 'https://shared.fastly.steamstatic.com/store_item_assets/steam/apps/3077660/capsule_616x353.jpg?t=1733235661', 'https://www.youtube.com/embed/dQw4w9WgXcQ', 'jim'),
('Racing Thunder', 'High-speed racing action with customizable cars and dynamic weather systems. Race through city streets and mountain passes in this adrenaline-pumping experience.', 'RACING', null, 'https://m.media-amazon.com/images/I/91xG+iA5q3L.png', 'https://www.youtube.com/embed/dQw4w9WgXcQ', 'jim');

-- Insert screenshots using online placeholder images
INSERT INTO game_screenshots (game_id, screenshot_url, screenshot_order) VALUES
(1, 'https://picsum.photos/800/600?random=1', 0),
(1, 'https://picsum.photos/800/600?random=2', 1),
(1, 'https://picsum.photos/800/600?random=3', 2),
(2, 'https://picsum.photos/800/600?random=4', 0),
(2, 'https://picsum.photos/800/600?random=5', 1),
(3, 'https://picsum.photos/800/600?random=6', 0),
(3, 'https://picsum.photos/800/600?random=7', 1),
(3, 'https://picsum.photos/800/600?random=8', 2),
(4, 'https://picsum.photos/800/600?random=9', 0),
(4, 'https://picsum.photos/800/600?random=10', 1);

INSERT INTO game_reviews (rating, comment, game_id, user_id, upvotes, downvotes) VALUES
(5, 'Amazing retro platformer! Brings back memories of classic games. The pixel art is beautiful and the gameplay is tight.', 1, 'jim', 3, 0),
(4, 'Great gameplay mechanics, but could use more levels. Really enjoying the nostalgic feel though!', 1, 'henk', 2, 1),
(5, 'Excellent strategy game with deep gameplay. The space battles are epic and the tech tree is well balanced.', 2, 'jim', 4, 0),
(4, 'Love the atmosphere in this adventure game. The puzzles are challenging but fair.', 3, 'henk', 1, 0),
(5, 'Best racing game I''ve played in years! The car customization is fantastic.', 4, 'henk', 2, 0);

INSERT INTO game_jams (name, description, rules, theme, game_jam_image_url, start_date, end_date, created_at, max_participants, current_participants, is_active) VALUES
('Summer Indie Game Jam 2025', 'Join us for an exciting 48-hour game development challenge! Create amazing indie games with fellow developers from around the world.', 'Games must be created within the 48-hour timeframe. Teams of up to 4 people allowed. All game engines and programming languages permitted. Submit your game before the deadline!', 'Time Travel', 'https://img.itch.zone/aW1hZ2UyL2phbS8zMjIxMTYvODk3NjY2OS5wbmc=/original/l0Qa4G.png', '2025-09-15 18:00:00', '2025-09-17 18:00:00', '2025-07-01 10:00:00', 100, 2, true),
('Horror Game Challenge', 'Create spine-chilling horror games in this week-long game jam. Show us your scariest ideas and most atmospheric gameplay!', 'Games must contain horror elements. No excessive gore or inappropriate content. Original assets preferred but asset store items allowed if credited. Submit with gameplay video.', 'Haunted Memories', 'https://img.itch.zone/aW1hZ2UyL2phbS8zODM4ODYvMTQzMTk3NTYucG5n/original/m%2FL6C6.png', '2025-10-25 12:00:00', '2025-11-01 12:00:00', '2025-07-05 14:00:00', 50, 0, true),
('Retro Arcade Fest', 'Bring back the golden age of gaming! Create games inspired by classic arcade titles from the 80s and 90s.', 'Games must have retro/arcade aesthetics. Pixel art encouraged. Classic gameplay mechanics preferred. Keep it simple and fun!', 'Neon Nights', 'https://img.itch.zone/aW1nLzE1NzMyMzM3LmpwZw==/original/BJz83D.jpg', '2025-12-01 09:00:00', '2025-12-07 21:00:00', '2025-07-15 08:00:00', 75, 12, false);

INSERT INTO game_jam_participants (game_jam_id, user_id, joined_at, submission_id, submission_date) VALUES
(1, 'henk', '2025-07-10 15:30:00', null, null),
(1, 'jim', '2025-07-12 09:15:00', null, null),
(3, 'henk', '2025-05-20 11:00:00', 1, '2025-06-06 18:30:00'),
(3, 'jim', '2025-05-22 14:45:00', null, null);
