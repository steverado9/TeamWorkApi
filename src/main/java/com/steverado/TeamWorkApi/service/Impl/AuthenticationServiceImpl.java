package com.steverado.TeamWorkApi.service.Impl;

import com.steverado.TeamWorkApi.dtos.LoginUserDto;
import com.steverado.TeamWorkApi.dtos.RegisterUserDto;
import com.steverado.TeamWorkApi.entity.User;
import com.steverado.TeamWorkApi.mappers.UserMapper;
import com.steverado.TeamWorkApi.repository.UserRepository;
import com.steverado.TeamWorkApi.service.ArticleCommentService;
import com.steverado.TeamWorkApi.service.AuthenticationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    private final UserMapper userMapper;

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationServiceImpl.class);


    public AuthenticationServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.userMapper = userMapper;
    }

    @Override
    public User signup(RegisterUserDto input) {
        logger.info("Received request to register a user with name: {}", input.getFirstName() + " " + input.getLastName());

        //used usermapper to map the content of each user input to the user entity
        User user = userMapper.toUserEntity(input);
        //manually encoded and added the password
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        userRepository.saveUser(
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getPassword(),
                user.getGender(),
                user.getRole().name(),
                user.getDepartment(),
                user.getAddress()
        );
        User savedUser = userRepository.findByEmail(user.getEmail()).get();
        logger.info("Returning registered user wirth id '{}'", savedUser.getId());

        return savedUser;
    }

    @Override
    public User authenticate(LoginUserDto input) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        input.getEmail(),
                        input.getPassword()
                )
        );
        return userRepository.findByEmail(input.getEmail()).orElseThrow();
    }
}
