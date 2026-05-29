package com.example.bugtracker.service;

import com.example.bugtracker.entity.Role;
import com.example.bugtracker.entity.User;
import com.example.bugtracker.entity.enums.RoleName;
import com.example.bugtracker.repository.RoleRepository;
import com.example.bugtracker.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private User testUser;
    private Role adminRole;

    @BeforeEach
    void setUp() {
        adminRole = new Role(1L, RoleName.ADMIN);
        testUser = new User();
        testUser.setId(1L);
        testUser.setEmail("admin@example.com");
        testUser.setPasswordHash("encoded");
        testUser.setRole(adminRole);
        testUser.setFirstName("Admin");
        testUser.setLastName("User");
        testUser.setCreatedAt(LocalDateTime.now());
    }

    @Test
    void findByEmail_ShouldReturnUser() {
        when(userRepository.findByEmail("admin@example.com")).thenReturn(Optional.of(testUser));
        Optional<User> found = userService.findByEmail("admin@example.com");
        assertThat(found).isPresent();
        assertThat(found.get().getEmail()).isEqualTo("admin@example.com");
    }

    @Test
    void createUser_ShouldSaveAndReturnUser() {
        when(roleRepository.findByName(RoleName.ADMIN)).thenReturn(Optional.of(adminRole));
        when(passwordEncoder.encode("pass")).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User u = invocation.getArgument(0);
            u.setId(2L);
            return u;
        });

        User created = userService.createUser("Doe", "John", null, "john@example.com", "pass", RoleName.ADMIN);
        assertThat(created.getId()).isEqualTo(2L);
        assertThat(created.getEmail()).isEqualTo("john@example.com");
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void deleteUser_ShouldCallRepositoryDelete() {
        userService.deleteUser(1L);
        verify(userRepository, times(1)).deleteById(1L);
    }
}