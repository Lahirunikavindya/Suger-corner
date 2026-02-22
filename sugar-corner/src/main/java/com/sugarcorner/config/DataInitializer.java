package com.sugarcorner.config;

import com.sugarcorner.model.entity.Product;
import com.sugarcorner.model.entity.User;
import com.sugarcorner.repository.ProductRepository;
import com.sugarcorner.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * Initializes default admin user and sample products on first run.
 */
@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository, ProductRepository productRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (userRepository.findByEmail("admin@sugarcorner.com").isEmpty()) {
            User admin = new User();
            admin.setFirstName("Admin");
            admin.setLastName("User");
            admin.setEmail("admin@sugarcorner.com");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setPhone("0771234567");
            admin.setAddress("Sugar Corner HQ");
            admin.setRole(User.Role.ADMIN);
            userRepository.save(admin);
        }

        if (productRepository.count() == 0) {
            createProduct("Classic Chocolate Brownie", "Rich chocolate brownie with walnuts", new BigDecimal("450.00"), 50);
            createProduct("Blondie", "Butterscotch blondie with white chocolate", new BigDecimal("420.00"), 40);
            createProduct("Salted Caramel Brownie", "Chocolate brownie with salted caramel swirl", new BigDecimal("480.00"), 35);
        }
    }

    private void createProduct(String name, String desc, BigDecimal price, int stock) {
        Product p = new Product();
        p.setName(name);
        p.setDescription(desc);
        p.setPrice(price);
        p.setStockQuantity(stock);
        productRepository.save(p);
    }
}
