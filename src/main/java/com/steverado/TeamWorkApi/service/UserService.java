package com.steverado.TeamWorkApi.service;

import com.steverado.TeamWorkApi.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<User> allUsers();

    Optional<User> findUserByEmail(String email);
}
