package com.example.rubrica.mapper;

import org.springframework.stereotype.Component;

import com.example.rubrica.dto.ContattoRequest;
import com.example.rubrica.dto.ContattoResponse;
import com.example.rubrica.model.Contatto;

@Component
public class ContattoMapper {

    public Contatto toEntity(ContattoRequest dto) {

        if (dto == null) {
            return null;
        } else {
            return new Contatto(null, dto.getNome(), dto.getCognome(),
                    dto.getTelefono(),
                    dto.getEmail());
        }

    }

    public ContattoResponse toResponse(Contatto entity) {
        if (entity == null) {
            return null;
        } else {
            return new ContattoResponse(entity.getId(), entity.getNome(), entity.getCognome(), entity.getTelefono(),
                    entity.getEmail());
        }
    }

    public void updateEntity(Contatto entity, ContattoRequest dto) {
        entity.setNome(dto.getNome());
        entity.setCognome(dto.getCognome());
        entity.setTelefono(dto.getTelefono());
        entity.setEmail(dto.getEmail());

    }

}
