package com.github.sakumas.healthtracker.service;

import com.github.sakumas.healthtracker.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Optional;

public interface UserService {

    void save(User user);

    Optional<User> findByUsername(String username);

    boolean register(User user, HttpServletRequest request, HttpServletResponse response);
}
