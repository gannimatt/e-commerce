package com.github.gannimatt.ecommerce.config;

import com.github.gannimatt.ecommerce.entity.Role;
import com.github.gannimatt.ecommerce.entity.User;
import com.github.gannimatt.ecommerce.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Set;

@Component
@ConditionalOnProperty(value = "app.seed.admin.enabled", havingValue = "true")
public class DevDataSeeder implements CommandLineRunner {

    private final UserRepository users;
    private final PasswordEncoder encoder;
    private final String seedEmail;
    private final String seedPassword;

    public DevDataSeeder(UserRepository users,
                         PasswordEncoder encoder,
                         @Value("${app.seed.admin.email:}") String seedEmail,
                         @Value("${app.seed.admin.password:}") String seedPassword) {
        this.users = users;
        this.encoder = encoder;
        this.seedEmail = seedEmail;
        this.seedPassword = seedPassword;
    }

    @Override
    public void run(String... args) {
        if (!StringUtils.hasText(seedEmail) || !StringUtils.hasText(seedPassword)) {
            return;
        }
        if (users.findByEmail(seedEmail).isEmpty()) {
            User admin = new User();
            admin.setEmail(seedEmail);
            admin.setPassword(encoder.encode(seedPassword));
            admin.setRoles(Set.of(Role.ROLE_ADMIN));
            users.save(admin);
        }
    }
}
