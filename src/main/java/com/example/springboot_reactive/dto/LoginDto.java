package com.example.springboot_reactive.dto;

import lombok.Data;

@Data
public class LoginDto {
    private String email;
    private String password;
}