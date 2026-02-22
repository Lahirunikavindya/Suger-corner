# Contact & Inquiry Management System

A customer feedback and inquiry management system for Brownie Delights, built with **Java Spring Boot** (backend) and **HTML/CSS/JavaScript** (frontend).

## Features

### Customer
- Submit feedback about brownies or services
- Send inquiries (questions or issue reports)
- Receive confirmation message after submission
- Switch between Feedback and Inquiry tabs

### Admin
- View all customer messages in one place
- Filter by: All, Unresponded, Responded
- Update message status (New, Pending, Resolved)
- Respond to inquiries directly
- View feedback trends and statistics

### System
- Logs date and time of each message
- Securely stores messages in H2 database
- REST API for all operations

## Tech Stack

- **Backend:** Java 21, Spring Boot 3.4, Spring Data JPA, H2 Database
- **Frontend:** HTML5, CSS3, JavaScript (vanilla)

## How to Run

### 1. Open in IntelliJ IDEA
- File → Open → Select the project folder
- IntelliJ will detect `pom.xml` and import as Maven project
- **Important:** Right-click on `pom.xml` → **Maven** → **Reload Project** (or click the Maven refresh icon in the Maven tool window)
- Wait for Maven dependencies to download

### Fix "package org.springframework.http does not exist" error
If you see this error, IntelliJ hasn't loaded Maven dependencies yet:
1. Open the **Maven** tool window (View → Tool Windows → Maven)
2. Click the **Reload All Maven Projects** button (circular arrow icon)
3. Wait for dependencies to download
4. File → Invalidate Caches → Invalidate and Restart (if needed)

### 2. Run the Application
- Open `ContactInquiryApplication.java`
- Right-click → Run 'ContactInquiryApplication'
- Or use Maven: `mvn spring-boot:run`

### 3. Access the Website
- **Customer form:** http://localhost:8080/
- **Admin dashboard:** http://localhost:8080/admin
- **H2 Console (optional):** http://localhost:8080/h2-console

## Project Structure

```
src/
├── main/
│   ├── java/com/isp/
│   │   ├── ContactInquiryApplication.java
│   │   ├── config/
│   │   ├── controller/
│   │   ├── entity/
│   │   ├── repository/
│   │   └── service/
│   └── resources/
│       ├── static/          # Frontend files
│       │   ├── index.html   # Customer contact form
│       │   ├── admin.html   # Admin dashboard
│       │   ├── styles.css
│       │   ├── app.js
│       │   └── admin.js
│       └── application.properties
```

## API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | /api/messages/feedback | Submit feedback |
| POST | /api/messages/inquiry | Submit inquiry |
| GET | /api/messages | Get all messages |
| GET | /api/messages/trends | Get statistics |
| PATCH | /api/messages/{id}/status | Update status |
| POST | /api/messages/{id}/respond | Add admin response |
