package com.example.rubrica.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.rubrica.model.Contatto;

public interface ContattoRepository extends JpaRepository<Contatto, Long> {

    public List<Contatto> findByNome(String nome);

    public List<Contatto> findByCognome(String cognome);

    public List<Contatto> findByNomeAndCognome(String nome, String cognome);

    @Query("SELECT c FROM Contatto c WHERE LOWER(c.nome) LIKE LOWER(CONCAT('%', :nome, '%'))")
    public List<Contatto> findByNomeLike(@Param("nome") String nome);

    Page<Contatto> findByNomeContainingIgnoreCase(String nome, Pageable pageable);

    Page<Contatto> findByCognomeContainingIgnoreCase(String cognome, Pageable pageable);

}
