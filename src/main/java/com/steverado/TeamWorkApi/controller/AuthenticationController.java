package com.steverado.TeamWorkApi.controller;

import com.steverado.TeamWorkApi.dtos.LoginUserDto;
import com.steverado.TeamWorkApi.dtos.RegisterUserDto;
import com.steverado.TeamWorkApi.entity.User;
import com.steverado.TeamWorkApi.enums.Role;
import com.steverado.TeamWorkApi.response.ApiResponse;
import com.steverado.TeamWorkApi.response.DataCreateUserResponse;
import com.steverado.TeamWorkApi.response.DataLoginResponse;
import com.steverado.TeamWorkApi.service.AuthenticationService;
import com.steverado.TeamWorkApi.service.JwtService;
import com.steverado.TeamWorkApi.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@Tag(name = "User Management", description = "Api for authenticating users")
public class AuthenticationController {
    private final JwtService jwtService;

    private final UserService userService;

    private final AuthenticationService authenticationService;

    public AuthenticationController(JwtService jwtService, UserService userService, AuthenticationService authenticationService) {
        this.jwtService = jwtService;
        this.userService = userService;
        this.authenticationService = authenticationService;
    }

    @Operation(summary = "register an Employee", description = "Add a new employee to the system")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Employee created successfully",
            content = @Content(schema = @Schema(implementation = RegisterUserDto.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid response data",
            content = @Content(schema = @Schema()))
    })
    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<DataCreateUserResponse>> register(@RequestBody RegisterUserDto registerUserDto) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        User currentUser = userService.findUserByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));

        if (currentUser.getRole() != Role.ADMIN) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        User registeredUser = authenticationService.signup(registerUserDto);

        DataCreateUserResponse data = new DataCreateUserResponse();
        data.setMessage("User account successfully created");
        data.setUserId(registeredUser.getId());
        data.setExpiresIn(jwtService.getExpirationTime());

        ApiResponse<DataCreateUserResponse> response = new ApiResponse<>("Success", data);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    @Operation(summary = "Sign in a user", description = "Login as an admin or employee")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "User logged in successfully",
                content = @Content(schema = @Schema(implementation = LoginUserDto.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Incorrect Username or password",
                content = @Content(schema = @Schema()))
    })
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<DataLoginResponse>> authenticate(@RequestBody LoginUserDto loginUserDto) {
        User authenticatedUser = authenticationService.authenticate(loginUserDto);

        String jwtToken = jwtService.generateToken(authenticatedUser);

        DataLoginResponse data = new DataLoginResponse();
        data.setToken(jwtToken);
        data.setUserId(authenticatedUser.getId());
        data.setExpiresIn(jwtService.getExpirationTime());

        ApiResponse<DataLoginResponse> response = new ApiResponse<>("success", data);

        return ResponseEntity.ok(response);
    }
}
