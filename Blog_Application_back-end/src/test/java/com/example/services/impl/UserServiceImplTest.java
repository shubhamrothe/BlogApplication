package com.example.services.impl;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import com.example.entities.User;
import com.example.payloads.UserDto;
import com.example.repositories.UserRepository;
import com.example.repositories.RoleRepository;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    private UserServiceImpl userService;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @BeforeEach
    void setUp() {
        userService = new UserServiceImpl();
        ReflectionTestUtils.setField(userService, "userRepository", userRepository);
        ReflectionTestUtils.setField(userService, "modelMapper", new ModelMapper());
        ReflectionTestUtils.setField(userService, "passwordEncoder", passwordEncoder);
        ReflectionTestUtils.setField(userService, "roleRepository", roleRepository);
        when(roleRepository.findByRoleName("ROLE_USER")).thenReturn(java.util.Optional.of(new com.example.entities.Role(1, "ROLE_USER")));
    }

    @Test
    void createUser_shouldHashPasswordBeforeSaving() {
        UserDto dto = new UserDto();
        dto.setUserName("shubham");
        dto.setEmail("shubham@test.com");
        dto.setPassword("Shubham@");
        dto.setAbout("test user");

        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        userService.createUser(dto);

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(captor.capture());

        User savedUser = captor.getValue();
        assertTrue(savedUser.getUserId() == null);
        assertNotEquals("Shubham@", savedUser.getPassword());
        assertTrue(passwordEncoder.matches("Shubham@", savedUser.getPassword()));
    }
}
