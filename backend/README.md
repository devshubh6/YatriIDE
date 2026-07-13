# Yatri IDE Backend

Complete backend API for Yatri IDE - Android Code Editor & Compiler

## Features

✅ User Authentication (JWT)
✅ Code Compilation (Java, Python, C++)
✅ Code Storage & Management
✅ Secure API Endpoints
✅ MongoDB Integration

## Installation

```bash
cd backend
npm install
```

## Environment Setup

Create `.env` file:

```
MONGODB_URI=mongodb+srv://username:password@cluster.mongodb.net/yatri-ide
JWT_SECRET=your_super_secret_jwt_key_change_this
PORT=5000
NODE_ENV=development
CLIENT_URL=http://localhost:3000
```

## Running the Server

```bash
# Development
npm run dev

# Production
npm start
```

## API Endpoints

### Authentication
- `POST /api/auth/register` - Register new user
- `POST /api/auth/login` - Login user
- `GET /api/auth/me` - Get current user (Protected)

### Code Management
- `POST /api/code/save` - Save code (Protected)
- `GET /api/code/all` - Get all user codes (Protected)
- `GET /api/code/:id` - Get specific code (Protected)
- `DELETE /api/code/:id` - Delete code (Protected)
- `POST /api/code/compile` - Compile code (Protected)

## Tech Stack

- Node.js
- Express.js
- MongoDB
- JWT
- bcryptjs

## Author

Shubh - devshubh6
