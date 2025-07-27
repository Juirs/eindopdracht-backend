# IndieVerse Backend - Installation Manual

## Overview
IndieVerse is a platform that connects indie game developers with gamers, creating a space where developers can showcase their work, get feedback, and build a community.

## Installation Steps

### 1. Clone the Repository

### 2. Install Dependencies with maven

### 3. Database Configuration
Ensure PostgreSQL is installed and running on your system, then create a database with the following name:
- `"eindopdracht-backend"`

### 4. Environment Configuration
Set the following environment variable for email functionality (replace with the gmail app password I provided in the .env file):

- `MAIL_PASSWORD=your-gmail-app-password`

### 5. Start the Application with Maven

The backend will be available at `http://localhost:8080`

## API Endpoints
The backend provides these main REST endpoints:

### Authentication
- `POST /authenticate` - User login (returns JWT token)
- `GET /authenticated` - Get current user info

### User Management
- `POST /users/register` - User registration
- `GET /users/{username}` - Get user details
- `PUT /users/{username}/profile` - Update user profile
- `PUT /users/{username}/change-password` - Change password

### Games
- `GET /games` - Fetch all games (optional category filter)
- `GET /games/{id}` - Get specific game details
- `POST /games` - Create new game (JSON)
- `POST /games/with-files` - Create game with file uploads
- `PUT /games/{id}` - Update game
- `DELETE /games/{id}` - Delete game
- `GET /games/{id}/download` - Download game file

### File Management
- `POST /games/{id}/image` - Upload game image
- `POST /games/{id}/game-file` - Upload game file
- `POST /games/{id}/screenshots` - Upload screenshots
- `GET /games/{id}/image` - Get game image
- `GET /games/{id}/screenshot/{index}` - Get screenshot

### Reviews & Ratings
- `GET /games/{gameId}/reviews` - Get game reviews
- `POST /games/{gameId}/reviews` - Create review
- `DELETE /games/reviews/{reviewId}` - Delete review
- `POST /games/reviews/{reviewId}/upvote` - Upvote review
- `POST /games/reviews/{reviewId}/downvote` - Downvote review

### Game Jams
- `GET /gamejams` - Get all game jams
- `GET /gamejams/{id}` - Get specific game jam
- `POST /gamejams` - Create game jam (admin only)
- `POST /gamejams/{id}/join` - Join game jam

### Admin Endpoints
- `GET /users` - Get all users (admin only)
- `POST /users/{username}/authorities` - Add user role (admin only)
- `DELETE /users/{username}/authorities/{authority}` - Remove user role (admin only)
- `POST /send` - Send test email (admin only)

### Roles
- **USER** - Basic user permissions (create reviews, join game jams)
- **DEVELOPER** - Can create and manage games
- **ADMIN** - Full system access

### Test Users
The application comes with pre-configured test users:
- **henk** (password: `password`) - ADMIN, DEVELOPER, USER
- **jim** (password: `password`) - DEVELOPER, USER  
- **tim** (password: `password`) - USER

## Troubleshooting

- Ensure PostgreSQL is up and running
- Check `MAIL_PASSWORD` environment variable is set