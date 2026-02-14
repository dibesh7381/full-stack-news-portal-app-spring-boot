# 📰 News Portal

A full-stack news web application where reporters can publish news and users can read, react, and comment on articles.
Built using **React** for the frontend and **Spring Boot** for the backend.

---

## 🚀 Features

### User

* View all news articles
* Like and dislike news
* Comment on articles
* Edit delete comments
* Real-time timestamp display

### Reporter

* Secure login with JWT authentication
* Add news with image
* Edit existing news
* Delete news
* Manage published articles

---

## 🛠 Tech Stack

### Frontend

* React
* Tailwind CSS
* Axios
* JWT Decode
* React Router 

### Backend

* Spring Boot
* Spring Security
* Spring Data JPA
* PostgreSQL
* Flyway Schema Migration
* JWT Authentication

### Cloud Services

* Cloudinary (Image Upload)

---

## ⚙️ Installation & Setup

### 1. Clone the repository

```bash
git clone https://github.com/dibesh7381/full-stack-news-portal-app-spring-boot
cd full-stack-news-portal-app-spring-boot
```

---

### 2. Backend Setup (Spring Boot)

Navigate to backend folder:

```bash
cd NewsPortalApp
```

Update `application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/news_portal
spring.datasource.username=your_username
spring.datasource.password=your_password

jwt.secret=your_jwt_secret

cloudinary.cloud-name=your_cloud_name
cloudinary.api-key=your_api_key
cloudinary.api-secret=your_api_secret
```

Run the backend:

```bash
mvn spring-boot:run
```

Backend runs on:

```
http://localhost:8080
```

---

### 3. Frontend Setup (React)

Navigate to frontend folder:

```bash
cd frontend/pj
```

Install dependencies:

```bash
npm install
```

Start development server:

```bash
npm run dev
```

Frontend runs on:

```
http://localhost:5173
```

---

## 🔐 Authentication

* JWT-based authentication
* Role-based access:

  * USER
  * REPORTER

---

## 📸 Screenshots

Add screenshots inside a `screenshots` folder.

Example:

```
screenshots/
├── home.png
├── login.png
└── reporter-dashboard.png
```

And reference them like:

```md
![Home](screenshots/home.png)
```

---

## 📌 Future Improvements

* Search functionality
* Category-based news
* Bookmark system
* Admin dashboard

---

## 👨‍💻 Author

**Dibesh Ranjan Das**

* GitHub: https://github.com/dibesh7381
* LinkedIn: https://www.linkedin.com/in/dibesh-ranjan-das-97a00b2ba/

