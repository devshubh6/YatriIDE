# 🚀 Yatri IDE - Android Code Editor & Compiler

**Yatri IDE** is a mobile-first code editor and compiler for Android devices. Write, compile, and execute code directly from your smartphone!

## 🎯 Features

✅ **User Authentication** - Secure JWT-based login/registration  
✅ **Code Editor** - Full-featured code editor with syntax support  
✅ **Live Compilation** - Compile Java, Python, C++ code in real-time  
✅ **Code Storage** - Save and manage your code snippets  
✅ **Execution Results** - View compilation output and errors  
✅ **Offline Support** - Work offline, sync when connected  

## 📱 App Architecture

### Frontend (Android)
- **Activities**: Login, Register, Editor, My Codes
- **API Client**: Retrofit2 for REST calls
- **Storage**: SharedPreferences for user data
- **UI**: Material Design components

### Backend (Node.js)
- **Authentication**: JWT tokens
- **Database**: MongoDB
- **Compilation**: Docker containers for safe execution
- **API**: RESTful endpoints

## 🛠️ Tech Stack

**Android:**
- Java 8+
- Retrofit2 (HTTP client)
- Gson (JSON parsing)
- Material Design
- AndroidX libraries

**Backend:**
- Node.js & Express.js
- MongoDB
- JWT authentication
- bcryptjs
- Docker (for compilation)

## 📋 Project Structure

```
YatriIDE/
├── app/
│   ├── src/main/
│   │   ├── java/com/shubh/yatri/
│   │   │   ├── activities/
│   │   │   │   ├── MainActivity.java
│   │   │   │   ├── LoginActivity.java
│   │   │   │   ├── RegisterActivity.java
│   │   │   │   ├── EditorActivity.java
│   │   │   │   └── MyCodesActivity.java
│   │   │   ├── adapters/
│   │   │   │   └── CodeAdapter.java
│   │   │   ├── api/
│   │   │   │   ├── ApiClient.java
│   │   │   │   └── ApiService.java
│   │   │   └── utils/
│   │   │       ├── SharedPrefsHelper.java
│   │   │       └── ToastHelper.java
│   │   └── res/
│   │       ├── layout/ (5 XML layouts)
│   │       ├── values/ (colors, strings)
│   │       └── AndroidManifest.xml
│   └── build.gradle
├── backend/
│   ├── models/
│   ├── controllers/
│   ├── routes/
│   ├── middleware/
│   ├── config/
│   ├── server.js
│   ├── package.json
│   └── README.md
└── README.md
```

## 🚀 Getting Started

### Android App

1. **Clone Repository**
   ```bash
   git clone https://github.com/devshubh6/YatriIDE.git
   cd YatriIDE
   ```

2. **Open in Android Studio**
   - File → Open → Select project folder
   - Wait for Gradle sync

3. **Configure API URL**
   - Edit `ApiClient.java`
   - Update `BASE_URL` to your backend server

4. **Build & Run**
   ```bash
   gradle build
   gradle installDebug
   ```

### Backend Server

1. **Install Dependencies**
   ```bash
   cd backend
   npm install
   ```

2. **Setup Environment**
   ```bash
   cp .env.example .env
   # Edit .env with your MongoDB URI and JWT secret
   ```

3. **Run Server**
   ```bash
   npm run dev
   ```

## 📡 API Endpoints

### Authentication
- `POST /api/auth/register` - Register new user
- `POST /api/auth/login` - Login user
- `GET /api/auth/me` - Get current user info

### Code Management
- `POST /api/code/save` - Save code snippet
- `GET /api/code/all` - Get all user codes
- `GET /api/code/:id` - Get specific code
- `DELETE /api/code/:id` - Delete code

### Compilation
- `POST /api/code/compile` - Compile and run code

## 📝 Usage

1. **Register/Login** - Create account or login
2. **Write Code** - Use the code editor
3. **Compile** - Click compile button to test
4. **Save** - Store your code for later
5. **Manage** - View all saved codes in "My Codes"

## 🔒 Security

- JWT token-based authentication
- Password hashing with bcryptjs
- HTTPS recommended for production
- Input validation on backend
- CORS protection

## 🐛 Troubleshooting

**Connection Error?**
- Check API URL in ApiClient.java
- Ensure backend is running
- Check firewall settings

**Compilation Failed?**
- Verify code syntax
- Check server logs
- Ensure Docker is running on backend

**Login Issues?**
- Clear app cache
- Verify credentials
- Check backend database connection

## 📦 Dependencies

See `build.gradle` and `backend/package.json` for full dependency list.

## 🤝 Contributing

Contributions welcome! Please:
1. Fork repository
2. Create feature branch
3. Commit changes
4. Push to branch
5. Create Pull Request

## 📄 License

MIT License - See LICENSE file for details

## 👨‍💻 Author

**Shubham Singh Yadav** (@devshubh6)
- GitHub: [@devshubh6](https://github.com/devshubh6)
- Email: shubhamsinghyadav374@gmail.com

## 🙏 Support

If you find this project helpful, please give it a ⭐ on GitHub!

---

**Made with ❤️ by Shubh**