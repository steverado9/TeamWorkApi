package com.steverado.TeamWorkApi.controller;

import com.steverado.TeamWorkApi.dtos.LoginUserDto;
import com.steverado.TeamWorkApi.dtos.RegisterUserDto;
import com.steverado.TeamWorkApi.entity.User;
import com.steverado.TeamWorkApi.response.ApiResponse;
import com.steverado.TeamWorkApi.response.LoginData;
import com.steverado.TeamWorkApi.response.LoginResponse;
import com.steverado.TeamWorkApi.service.AuthenticationService;
import com.steverado.TeamWorkApi.service.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/auth")
@RestController
public class AuthenticationController {
    private final JwtService jwtService;

    private final AuthenticationService authenticationService;

    public AuthenticationController(JwtService jwtService, AuthenticationService authenticationService) {
        this.jwtService = jwtService;
        this.authenticationService = authenticationService;
    }

    @PostMapping("/signup")
    public ResponseEntity<User> register (@RequestBody RegisterUserDto registerUserDto) {
        User registeredUser = authenticationService.signup(registerUserDto);

        return ResponseEntity.ok(registeredUser);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginData>> authenticate(@RequestBody LoginUserDto loginUserDto) {
        User authenticatedUser = authenticationService.authenticate(loginUserDto);

        String jwtToken = jwtService.generateToken(authenticatedUser);

        LoginData data = new LoginData();
        data.setToken(jwtToken);
        data.setExpiresIn(jwtService.getExpirationTime());
        data.setUserId(authenticatedUser.getId());

        ApiResponse<LoginData> response = new ApiResponse<>("success", data);

        return ResponseEntity.ok(response);
    }
}
