package com.example.rubrica.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.rubrica.dto.ContattoRequest;
import com.example.rubrica.mapper.ContattoMapper;
import com.example.rubrica.model.Contatto;
import com.example.rubrica.repository.ContattoRepository;

@Service
public class ContattoService {

    private final ContattoRepository repo;
    private final ContattoMapper mapper;

    public ContattoService(ContattoRepository repo, ContattoMapper mapper) {
        this.repo = repo;
        this.mapper = mapper;
    }

    public List<Contatto> findAll() {

        return repo.findAll();
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "contatti", key = "#id")
    public Optional<Contatto> findById(Long id) {

        return repo.findById(id);

    }

    @Transactional
    public Contatto insertContatto(Contatto contatto) {

        return repo.save(contatto);
    }

    public Contatto updateContatto(Long id, ContattoRequest contatto) {

        Contatto entity = repo.findById(id).orElseThrow(() -> new NoSuchElementException("Contatto non trovato"));
        mapper.updateEntity(entity, contatto);
        return repo.save(entity);

    };

    @Transactional
    public void deleteContatto(Long id) {

        repo.deleteById(id);
    }

    public List<Contatto> findByNomeAndCognome(String nome, String cognome) {

        return repo.findByNomeAndCognome(nome, cognome);

    }

    @Transactional(readOnly = true)
    public List<Contatto> findByNome(String nome) {

        return repo.findByNome(nome);

    }

    @Transactional(readOnly = true)
    public List<Contatto> findByCognome(String cognome) {

        return repo.findByCognome(cognome);
    }

    @Transactional(readOnly = true)
    public List<Contatto> findByNameLike(String nome) {

        return repo.findByNomeLike(nome);
    }

    @Transactional(readOnly = true)
    public Page<Contatto> findAll(Pageable pageable) {
        return repo.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Page<Contatto> searchByNome(String nome, Pageable pageable) {
        return repo.findByNomeContainingIgnoreCase(nome, pageable);
    }

    @Transactional(readOnly = true)
    public Page<Contatto> searchByCognome(String cognome, Pageable pageable) {
        return repo.findByCognomeContainingIgnoreCase(cognome, pageable);
    }
}
