package com.github.gannimatt.ecommerce.config;

import com.github.gannimatt.ecommerce.entity.Role;
import com.github.gannimatt.ecommerce.entity.User;
import com.github.gannimatt.ecommerce.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@Profile("dev")
public class DevDataSeeder implements CommandLineRunner {

    private final UserRepository users;
    private final PasswordEncoder encoder;

    public DevDataSeeder(UserRepository users, PasswordEncoder encoder) {
        this.users = users;
        this.encoder = encoder;
    }

    @Override
    public void run(String... args) {
        String adminEmail = "admin@local";
        if (users.findByEmail(adminEmail).isEmpty()) {
            User admin = new User();
            admin.setEmail(adminEmail);
            admin.setPassword(encoder.encode("admin123"));
            admin.setRoles(Set.of(Role.ROLE_ADMIN));
            users.save(admin);
        }
    }
}
