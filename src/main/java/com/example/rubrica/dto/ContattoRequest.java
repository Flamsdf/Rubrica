package com.example.rubrica.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ContattoRequest {

  @NotBlank
  private String nome;
  @NotBlank
  private String cognome;
  @Pattern(regexp = "^[+0-9 ()-]{6,20}$")
  private String telefono;
  @Email
  @NotBlank
  @Size(max = 120)
  private String email;

}