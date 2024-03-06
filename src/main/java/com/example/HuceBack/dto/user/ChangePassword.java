package com.example.HuceBack.dto.user;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ChangePassword {
    @NotEmpty(message = "password is not empty")
    @Size( min = 8, message = "password should have at least 8 characters")
    private String passwordOld;
    @NotEmpty(message = "password is not empty")
    @Size( min = 8, message = "password should have at least 8 characters")
    private String passwordNew;

}
