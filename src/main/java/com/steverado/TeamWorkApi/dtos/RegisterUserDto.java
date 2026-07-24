package com.steverado.TeamWorkApi.dtos;

import com.steverado.TeamWorkApi.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class RegisterUserDto {

    @NotBlank(message = "firstname field should not be empty")
    private String firstName;

    @NotBlank(message = "lastname field should not be empty")
    private String lastName;

    @NotBlank(message = "email field should not be empty")
    @Email(message = "invalid email format")
    private String email;

    @NotBlank(message = "password field should not be empty")
    private String password;

    @NotBlank(message = "gender field should not be empty")
    private String gender;

    @NotBlank(message = "role field should not be empty")
    private Role role;

    @NotBlank(message = "department field should not be empty")
    private String department;

    @NotBlank(message = "address field should not be empty")
    private String address;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
