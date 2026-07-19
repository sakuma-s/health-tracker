package com.github.sakumas.healthtracker.service;

import com.github.sakumas.healthtracker.entity.User;
import com.github.sakumas.healthtracker.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.springframework.security.core.userdetails.UserDetailsService;
@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserDetailsServiceImpl userDetailsService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;



    @Test
    void 新規ユーザーの登録が成功する() {
        // Arrange
        User newUser = new User();
        newUser.setUsername("testuser");
        newUser.setPassword("password");

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("password")).thenReturn("hashedpassword");
        when(userRepository.save(any(User.class))).thenReturn(newUser);
        // when()の追加
        UserDetails mockUserDetails = mock(UserDetails.class);
        when(userDetailsService.loadUserByUsername("testuser")).thenReturn(mockUserDetails);
        when(mockUserDetails.getAuthorities()).thenReturn(java.util.Collections.emptyList());
        // Act
        boolean result = userService.register(newUser, request, response);

        // Assert
        assertThat(result).isTrue();
    }

    @Test
    void 重複ユーザー名の場合は登録が失敗する() {
        // Arrange
        User existingUser = new User();
        existingUser.setUsername("testuser");

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(existingUser));

        User newUser = new User();
        newUser.setUsername("testuser");
        newUser.setPassword("password");

        // Act
        boolean duplicateResult = userService.register(newUser, request, response);

        // Assert
        assertThat(duplicateResult).isFalse();
    }
}
