📄 Resume Builder Frontend (React + Spring Boot)

A modern, responsive, production-ready Resume Builder Frontend built using React + Vite + Tailwind CSS, designed to work seamlessly with a Java Spring Boot backend.
This application supports JWT authentication, resume management, template browsing, profile management, and payment history.

✨ Features

🔐 JWT Authentication (Login / Register / Logout)

🛡️ Protected Routes for authenticated users

👤 User Profile Management

📄 Create, Edit, View, Delete Resumes

🎨 Browse Resume Templates

💳 View Payment History

📤 Image Upload Support

⚡ Fast and modern UI with React + Vite

📱 Fully Responsive (Desktop + Mobile)

🎯 Clean project structure and scalable architecture

🔄 Axios API integration with Spring Boot backend

⏳ Loading states, error handling & form validation

🛠️ Tech Stack

Frontend:

React 18

Vite

Tailwind CSS

React Router DOM

Axios

Backend (Required):

Java Spring Boot


📁 Project Structure

resume-builder-ui/
 ├─ package.json
 ├─ vite.config.js
 ├─ index.html
 └─ src/
    ├─ main.jsx
    ├─ App.jsx
    ├─ api/
    │   └─ axios.js
    ├─ auth/
    │   ├─ AuthContext.jsx
    │   └─ ProtectedRoute.jsx
    ├─ pages/
    │   ├─ Login.jsx
    │   ├─ Register.jsx
    │   ├─ Dashboard.jsx
    │   ├─ Profile.jsx
    │   ├─ Resumes.jsx
    │   ├─ EditResume.jsx
    │   └─ Payments.jsx
    └─ components/
        ├─ Navbar.jsx
        ├─ Loader.jsx
        └─ Toast.jsx

JWT Authentication

REST APIs
