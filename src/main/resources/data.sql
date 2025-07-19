INSERT INTO users (username, password, enabled, apikey, email) VALUES ('henk', '$2a$12$tJ1OH9MNbJVoMn53ZeDXQeN1vNK/tN0zO530T0dExTY1y6guOoNni', true, '7847493', 'test@testy.tst');
INSERT INTO users (username, password, enabled, apikey, email) VALUES ('Jim', '$2a$12$tJ1OH9MNbJVoMn53ZeDXQeN1vNK/tN0zO530T0dExTY1y6guOoNni', true, '7847494', 'test@testy.test');

INSERT INTO roles (username, role) VALUES ('henk', 'USER');
INSERT INTO roles (username, role) VALUES ('henk', 'ADMIN');
INSERT INTO roles (username, role) VALUES ('Jim', 'USER');

INSERT INTO users_roles (user_id, role_username, role_name) VALUES ('henk', 'henk', 'USER');
INSERT INTO users_roles (user_id, role_username, role_name) VALUES ('henk', 'henk', 'ADMIN');
INSERT INTO users_roles (user_id, role_username, role_name) VALUES ('Jim', 'Jim', 'USER');

INSERT INTO games (title, description, category, file_path, developer_id) VALUES
('Pixel Adventure', 'A classic 2D platformer with retro pixel art style.', 'PLATFORMER', '/games/pixel-adventure/game.zip', 'henk'),
('Space Strategy Command', 'Build your galactic empire in this turn-based strategy game.', 'STRATEGY', '/games/space-strategy/game.zip', 'henk');
