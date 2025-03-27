package com.example.springboot_reactive.dto.response;

import com.example.springboot_reactive.models.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterResponse {
  private String message;
  private User user;
}
