package com.example.HuceBack.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Signup {
    @NotEmpty(message = "name is not empty")
    private String name;
    @NotEmpty(message = "email is not empty")
    @Email(message = "not email")
    private String email;
    @NotEmpty(message = "password is not empty")
    @Size( min = 8, message = "password should have at least 8 characters")
    private String password;
}
