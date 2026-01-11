# FileSystem_TypeFace_Assignment

# 📁 File System Application (Full Stack)

A simplified Dropbox-like file system application that allows users to **upload, list, and download files** via a web interface.

Built as a **full-stack project** using **Spring Boot (Java)** for the backend and **React** for the frontend.

---

## ✨ Features

- Upload files (txt, png, jpg, json)
- List all uploaded files
- Download files
- File metadata persisted in PostgreSQL
- Actual file contents stored on local filesystem
- Clean REST API design
- Modern React frontend with Zustand state management

---

## 🧱 Tech Stack

### Backend
- Java 17
- Spring Boot
- Spring Data JPA
- PostgreSQL (Docker)
- Local filesystem storage

### Frontend
- React (Vite)
- Zustand (state management)
- Axios
- Tailwind CSS

---

## 📂 Project Structure
## 👨‍💻 Development

### Project Structure

```
file-system-app/
├── demo/                          # Spring Boot backend
│   ├── src/
│   │   └── main/
│   │       ├── java/             # Java source code
│   │       └── resources/        # Configuration files
│   ├── docker-compose.yaml       # PostgreSQL setup
│   ├── pom.xml                   # Maven dependencies
│   └── uploads/                  # File storage directory
│
└── frontend/                     # React frontend
    ├── src/
    │   ├── components/           # React components
    │   ├── store/                # Zustand state management
    │   └── App.jsx               # Main app component
    ├── package.json              # npm dependencies
    └── vite.config.js            # Vite configuration
```

---

## 🚀 Getting Started (Run Locally)

### ✅ Prerequisites

Make sure the following are installed on your system:

- Java **17+**
- Node.js **20.19+** or **22+**
- Docker & Docker Compose
- Git

---

## 🔧 Backend Setup

### 1️⃣ Navigate to backend folder

```bash
cd demo
```

### 2️⃣ Start PostgreSQL Database

Start the PostgreSQL database using Docker Compose:

```bash
docker-compose up -d
```

This will start a PostgreSQL container with the following configuration:
- **Database**: `file_storage`
- **Username**: `app_user`
- **Password**: `app_password`
- **Port**: `5432`

### 3️⃣ Verify Database is Running

```bash
docker ps
```

You should see a container named `file-storage-postgres` running.

### 4️⃣ Start the Spring Boot Backend

Run the backend using Maven wrapper:

```bash
./mvnw spring-boot:run
```

Or if you're on Windows:

```bash
mvnw.cmd spring-boot:run
```

The backend will start on **http://localhost:8080**

**Note**: The first run may take a few minutes as Maven downloads dependencies.

### 5️⃣ Verify Backend is Running

You should see output similar to:
```
Started DemoApplication in X.XXX seconds
```

The backend will automatically:
- Create the `uploads` directory for file storage
- Initialize the database schema using JPA/Hibernate
- Start listening on port 8080

---

## 🎨 Frontend Setup

### 1️⃣ Navigate to frontend folder

Open a **new terminal** and run:

```bash
cd frontend
```

### 2️⃣ Install Dependencies

```bash
npm install
```

### 3️⃣ Start the Development Server

```bash
npm run dev
```

The frontend will start on **http://localhost:5173**

### 4️⃣ Open in Browser

Open your browser and navigate to:
```
http://localhost:5173
```

You should see the file upload interface!

---

## ✅ Verification

Once both backend and frontend are running, you can:

1. **Upload a file**: Click the upload button and select a file (txt, png, jpg, or json)
2. **View files**: See all uploaded files in the list
3. **Download files**: Click on any file to download it

### Test the API Directly

You can also test the backend API using curl:

**Upload a file:**
```bash
curl -X POST http://localhost:8080/api/files/upload \
  -F "file=@/path/to/your/file.txt"
```

**List all files:**
```bash
curl http://localhost:8080/api/files
```

**Download a file:**
```bash
curl -O http://localhost:8080/api/files/download/{fileId}
```

---

## 🛑 Stopping the Application

### Stop Frontend
Press `Ctrl + C` in the terminal running the frontend

### Stop Backend
Press `Ctrl + C` in the terminal running the backend

### Stop PostgreSQL
```bash
cd demo
docker-compose down
```

To also remove the database volume (⚠️ this will delete all data):
```bash
docker-compose down -v
```

---

## 🐛 Troubleshooting

### Backend won't start
- **Check if port 8080 is already in use:**
  ```bash
  lsof -i :8080
  ```
  Kill the process if needed: `kill -9 <PID>`

- **Database connection issues:**
  - Ensure Docker is running: `docker ps`
  - Verify PostgreSQL container is up: `docker logs file-storage-postgres`
  - Check database credentials in `demo/src/main/resources/application.properties`

### Frontend won't start
- **Check if port 5173 is already in use:**
  ```bash
  lsof -i :5173
  ```
  Kill the process if needed: `kill -9 <PID>`

- **Dependencies not installed:**
  ```bash
  cd frontend
  rm -rf node_modules package-lock.json
  npm install
  ```

### File upload fails
- Ensure the `uploads` directory exists in the `demo` folder
- Check backend logs for errors
- Verify file type is supported (txt, png, jpg, json)

---

## 📚 API Documentation

### Base URL
```
http://localhost:8080/api/files
```

### Endpoints

| Method | Endpoint | Description | Request | Response |
|--------|----------|-------------|---------|----------|
| POST | `/upload` | Upload a file | `multipart/form-data` with `file` field | File metadata |
| GET | `/` | List all files | - | Array of file metadata |
| GET | `/download/{id}` | Download a file | File ID in path | File content |

---

## 📝 Notes

- Files are stored in the `demo/uploads` directory
- File metadata is persisted in PostgreSQL
- The application supports txt, png, jpg, and json file types
- Maximum file size is determined by Spring Boot defaults (1MB by default)

---

## 🚀 Built With

- **Backend**: Spring Boot, Spring Data JPA, PostgreSQL
- **Frontend**: React, Vite, Zustand, Tailwind CSS, Axios
- **Database**: PostgreSQL (Docker)
- **Build Tools**: Maven (backend), npm (frontend)

