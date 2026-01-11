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
file-system-app/
├── backend/ # Spring Boot backend
├── frontend/ # React frontend
└── README.md

---


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
cd backend
docker-compose up -d

