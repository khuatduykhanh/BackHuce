package com.example.HuceBack.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class JWTAuthRes {
    private String name;
    private Long studentCode;
    private String department;
    private String lop;
    private String accessToken;
    private String refreshToken;
}
