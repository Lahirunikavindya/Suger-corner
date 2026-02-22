# Sugar Corner - Brownies Sales & Order Management System

A web-based brownies sales and order management system built with Java Spring Boot, MySQL, and Thymeleaf.

## Technology Stack

| Layer | Technology |
|-------|------------|
| Backend | Java 17, Spring Boot 3.2 |
| Architecture | MVC (Model-View-Controller) |
| Database | MySQL 8.x |
| ORM | Spring Data JPA / Hibernate |
| Frontend | HTML5, CSS3, JavaScript |
| Templates | Thymeleaf |
| Security | Spring Security |
| Build Tool | Maven |

## Project Structure

```
sugar-corner/
├── pom.xml
├── README.md
└── src/
    ├── main/
    │   ├── java/com/sugarcorner/
    │   │   ├── SugarCornerApplication.java
    │   │   ├── config/
    │   │   │   ├── DataInitializer.java
    │   │   │   ├── SecurityConfig.java
    │   │   │   └── WebConfig.java
    │   │   ├── controller/
    │   │   │   ├── HomeController.java
    │   │   │   ├── AuthController.java
    │   │   │   ├── UserController.java
    │   │   │   ├── ProductController.java
    │   │   │   ├── CartController.java
    │   │   │   ├── OrderController.java
    │   │   │   ├── FeedbackController.java
    │   │   │   ├── AdminProductController.java
    │   │   │   ├── AdminOrderController.java
    │   │   │   ├── AdminPaymentController.java
    │   │   │   └── AdminFeedbackController.java
    │   │   ├── dto/
    │   │   │   ├── RegisterRequest.java
    │   │   │   ├── ProfileUpdateRequest.java
    │   │   │   ├── OrderRequest.java
    │   │   │   ├── OrderItemRequest.java
    │   │   │   ├── FeedbackRequest.java
    │   │   │   └── CartItemDto.java
    │   │   ├── model/entity/
    │   │   │   ├── User.java
    │   │   │   ├── Product.java
    │   │   │   ├── Order.java
    │   │   │   ├── OrderItem.java
    │   │   │   ├── Payment.java
    │   │   │   └── Feedback.java
    │   │   ├── repository/
    │   │   │   ├── UserRepository.java
    │   │   │   ├── ProductRepository.java
    │   │   │   ├── OrderRepository.java
    │   │   │   ├── OrderItemRepository.java
    │   │   │   ├── PaymentRepository.java
    │   │   │   └── FeedbackRepository.java
    │   │   └── service/
    │   │       ├── CustomUserDetailsService.java
    │   │       ├── UserService.java
    │   │       ├── ProductService.java
    │   │       ├── CartService.java
    │   │       ├── OrderService.java
    │   │       ├── PaymentService.java
    │   │       └── FeedbackService.java
    │   └── resources/
    │       ├── application.properties
    │       ├── static/
    │       │   ├── css/
    │       │   │   └── styles.css
    │       │   ├── js/
    │       │   │   ├── app.js
    │       │   │   └── cart.js
    │       │   └── images/
    │       └── templates/
    │           ├── fragments/
    │           │   └── layout.html
    │           ├── home.html
    │           ├── auth/
    │           │   ├── login.html
    │           │   └── register.html
    │           ├── user/
    │           │   └── profile.html
    │           ├── products/
    │           │   ├── list.html
    │           │   └── detail.html
    │           ├── cart/
    │           │   └── view.html
    │           ├── orders/
    │           │   ├── history.html
    │           │   └── detail.html
    │           ├── feedback/
    │           │   ├── form.html
    │           │   └── history.html
    │           └── admin/
    │               ├── layout.html
    │               ├── products/
    │               │   ├── list.html
    │               │   └── form.html
    │               ├── orders/
    │               │   ├── list.html
    │               │   └── detail.html
    │               ├── payments/
    │               │   └── list.html
    │               └── feedback/
    │                   ├── list.html
    │                   └── detail.html
    └── test/
        └── java/com/sugarcorner/
```

## Modules

### 1. Customer Management
- **Registration** – New customers can register with email, password, and profile details
- **Login/Logout** – Spring Security-based authentication
- **Profile Update** – Customers can update their profile information
- **Role-based Access** – Admin and Customer roles with different permissions

### 2. Product Management
- **Admin**: Add, edit, delete products (soft delete)
- **Customers**: View products, search and filter
- **Product Details**: Name, description, price, stock, image URL

### 3. Order Management
- **Place Order** – Customers add products to cart and checkout
- **Order Status**: Pending → Confirmed → Preparing → Delivered / Cancelled
- **Admin**: Update order status
- **Order History**: Customers see their orders; admins see all orders

### 4. Payment Handling
- **Cash on Delivery** – Default payment method
- **Payment Status**: Pending, Paid, Failed, Refunded
- **Admin**: Mark payments as paid, filter by status

### 5. Feedback & Inquiry Management
- **Customers**: Submit feedback with subject and message
- **Admin**: Respond to feedback
- **Message Status**: New, Pending, Resolved
- **Timestamps**: Submitted date/time, responded date/time

## Prerequisites

- Java 17+
- Maven 3.8+
- MySQL 8.x

## Setup

1. **Clone and navigate to the project**
   ```bash
   cd sugar-corner
   ```

2. **Configure MySQL**
   - Create database `sugar_corner_db` (or update `application.properties`)
   - Update `spring.datasource.username` and `spring.datasource.password` in `src/main/resources/application.properties`

3. **Build and run**
   ```bash
   mvn spring-boot:run
   ```

4. **Access the application**
   - URL: http://localhost:8080
   - **Admin**: admin@sugarcorner.com / admin123
   - **Customer**: Register a new account

## Default Data

On first run, the application creates:
- **Admin user**: admin@sugarcorner.com / admin123
- **Sample products**: Classic Chocolate Brownie, Blondie, Salted Caramel Brownie

## API Endpoints (Web)

| Path | Description | Access |
|------|-------------|--------|
| / | Home page | Public |
| /products | Product listing | Public |
| /products/{id} | Product detail | Public |
| /auth/register | Registration | Public |
| /login | Login | Public |
| /cart | Cart view | Public |
| /cart/add | Add to cart | Authenticated |
| /orders | Order history | Customer |
| /profile | Profile | Customer |
| /feedback | Submit feedback | Public |
| /admin/products | Manage products | Admin |
| /admin/orders | Manage orders | Admin |
| /admin/payments | Manage payments | Admin |
| /admin/feedback | Manage feedback | Admin |

## License

MIT License - Educational/Project Use
