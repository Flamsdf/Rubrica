package com.example.rubrica.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ContattoResponse {
    private Long id;
    private String nome;
    private String cognome;
    private String telefono;
    private String email;

}
