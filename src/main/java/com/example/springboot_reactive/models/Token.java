package com.example.springboot_reactive.models;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "tokens")
public class Token {

  @Column("id_token")
  private String idToken;

  @Column("id_user")
  private String idUser;

  @Column("refresh_token")
  private String refreshToken;

  @Column("devices")
  private String deviceJson;

  @Transient
  private List<String> devices = new ArrayList<>();

  public Token(String idToken, String idUser, String refreshToken, List<String> devices) {
    this.idToken = idToken;
    this.idUser = idUser;
    this.refreshToken = refreshToken;
    try {
      this.deviceJson = new ObjectMapper().writeValueAsString(devices);
    } catch (Exception e) {
      this.deviceJson = "[]";
    }
  }
}
