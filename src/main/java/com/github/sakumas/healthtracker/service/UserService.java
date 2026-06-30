package com.github.sakumas.healthtracker.service;

import com.github.sakumas.healthtracker.entity.User;

import java.util.Optional;

public interface UserService {

    void save(User user);

    Optional<User> findByUsername(String username);
}
