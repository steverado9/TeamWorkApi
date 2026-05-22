package com.steverado.TeamWorkApi.service;

import com.steverado.TeamWorkApi.dtos.LoginUserDto;
import com.steverado.TeamWorkApi.dtos.RegisterUserDto;
import com.steverado.TeamWorkApi.entity.User;

public interface AuthenticationService {

    User signup(RegisterUserDto input);

    User authenticate(LoginUserDto input);
}
