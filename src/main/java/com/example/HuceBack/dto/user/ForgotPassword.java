package com.example.HuceBack.dto.user;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ForgotPassword {
    @NotEmpty(message = "password is not empty")
    @Size( min = 8, message = "password should have at least 8 characters")
    private String passwordNew;
}
