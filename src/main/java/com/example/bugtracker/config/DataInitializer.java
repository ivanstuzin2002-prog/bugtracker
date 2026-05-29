package com.example.bugtracker.config;

import com.example.bugtracker.entity.Role;
import com.example.bugtracker.entity.User;
import com.example.bugtracker.entity.enums.RoleName;
import com.example.bugtracker.repository.RoleRepository;
import com.example.bugtracker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;

    @Override
    public void run(String... args) {
        // Создаём все недостающие роли
        for (RoleName roleName : RoleName.values()) {
            if (roleRepository.findByName(roleName).isEmpty()) {
                roleRepository.save(new Role(null, roleName));
                System.out.println("Created role: " + roleName);
            }
        }

        // Создаём администратора, если его нет
        if (userRepository.findByEmail("admin@example.com").isEmpty()) {
            Role adminRole = roleRepository.findByName(RoleName.ADMIN)
                    .orElseThrow(() -> new RuntimeException("Admin role not found (should have been created)"));
            User admin = new User();
            admin.setEmail("admin@example.com");
            admin.setPasswordHash(encoder.encode("admin"));
            admin.setRole(adminRole);
            admin.setFirstName("Admin");
            admin.setLastName("User");
            admin.setCreatedAt(LocalDateTime.now());
            userRepository.save(admin);
            System.out.println("Created admin user: admin@example.com / admin");
        }
    }
}
