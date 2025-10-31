package com.example.rubrica.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import com.example.rubrica.model.Contatto;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@ActiveProfiles("test")
public class ContattoRepositoryTest {

    @Autowired
    private  ContattoRepository repo;



    @BeforeEach
    void setup() {
        repo.saveAll(List.of(
            new Contatto( null, "Mario",  "Rossi", "111", "mario@ex.com"),
            new Contatto( null, "Marina",  "Verdi", "222", "marina@ex.com")
        ));
        repo.flush();
    }

    @Test
    void findNomeLista() {
        List<Contatto> result = repo.findByNome("Mario");
        assertFalse(result.isEmpty());
        assertEquals("Mario", result.get(0).getNome());

    }

    @Test
    void searchByNomeLike_caseInsensitive() {
        List<Contatto> result = repo.findByNomeLike("mar");
        assertEquals(2, result.size()); 
    }
}
