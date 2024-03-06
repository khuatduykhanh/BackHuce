package com.example.HuceBack.dto.user;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginReq {
    @NotEmpty(message = "username not empty")
    private String email;
    @NotEmpty
    @Size(min = 8, message ="description should have at least 10 characters")
    private String password;
}
