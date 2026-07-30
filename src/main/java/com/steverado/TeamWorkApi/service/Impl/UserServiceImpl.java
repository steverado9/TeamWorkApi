package com.steverado.TeamWorkApi.service.Impl;

import com.steverado.TeamWorkApi.entity.User;
import com.steverado.TeamWorkApi.repository.UserRepository;
import com.steverado.TeamWorkApi.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<User> allUsers() {
        logger.info("Get all users");

        List<User> users = new ArrayList<>();

        userRepository.findAll().forEach(users::add);
        logger.info("users {}", users);

        return users;
    }

    @Override
    public Optional<User> findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
