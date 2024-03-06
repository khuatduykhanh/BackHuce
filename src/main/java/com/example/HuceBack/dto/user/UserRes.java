package com.example.HuceBack.dto.user;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRes {
    private String id;
    private String name;
    private String email;
    private String department;
    private String lop;
}
