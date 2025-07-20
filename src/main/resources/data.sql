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
