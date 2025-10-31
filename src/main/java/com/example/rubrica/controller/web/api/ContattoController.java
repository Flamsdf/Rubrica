package com.example.rubrica.controller.web.api;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.rubrica.dto.ContattoRequest;
import com.example.rubrica.dto.ContattoResponse;
import com.example.rubrica.mapper.ContattoMapper;
import com.example.rubrica.model.Contatto;
import com.example.rubrica.service.ContattoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping(ContattoController.BASE_PATH)
public class ContattoController {
    public static final String BASE_PATH = "/api/contatti";
    private final ContattoService service;
    private final ContattoMapper mapper;

    private static final Logger log = LoggerFactory.getLogger(ContattoController.class);

    public ContattoController(ContattoService service, ContattoMapper mapper) {
        this.service = service;
        this.mapper = mapper;

    }

    @GetMapping
    public ResponseEntity<List<ContattoResponse>> findAll() {
        try {
            log.info("Request [GET /contatti]");
            List<ContattoResponse> response = service.findAll().stream().map(mapper::toResponse).toList();
            if (response.isEmpty()) {
                log.info("[GET /contatti] Nessun contatto trovato (200 OK, lista vuota)");
            }
            log.info("[GET /contatti] Restituiti {} contatti (200 OK)", response.size());
            log.debug("Response body: {}", response);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Errore {} durante Fla chiamata a [GET /contatti]: {}", e.getClass().getSimpleName(),
                    e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping
    public ResponseEntity<ContattoResponse> insertContatto(@Valid @RequestBody ContattoRequest contatto) {
        log.info("[POST /contatti] Richiesta creazione contatto");
        log.debug("Request body: {}", contatto);
        try {
            Contatto entity = mapper.toEntity(contatto);

            Contatto saved = service.insertContatto(entity);

            ContattoResponse response = mapper.toResponse(saved);
            log.info("[POST /contatti] Restituiti {} contatti (201 CREATED)");
            log.debug("Response POST /contatti: {}", response);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            log.error("Errore {} durante la chiamata a [POST /contatti]: {}", e.getClass().getSimpleName(),
                    e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ContattoResponse> findById(@PathVariable Long id) {
        try {
            log.info(" [GET /contatti/{}] Richiesta dettaglio contatto", id);

            return service.findById(id)
                    .map(mapper::toResponse)
                    .map(ResponseEntity::ok)
                    .orElseGet(() -> {
                        log.warn("[GET /contatti/{}] Contatto non trovato (404)", id);
                        return ResponseEntity.notFound().build();
                    });

        } catch (Exception e) {
            log.error("Errore {} durante la chiamata a [GET /contatti/{}]: {}", e.getClass().getSimpleName(), id,
                    e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ContattoResponse> updateContatto(@PathVariable Long id,
            @Valid @RequestBody ContattoRequest contatto) {
        log.info("[PUT /contatti/{}]", id);
        log.debug("[PUT /contatti/{}] Request body: {}", id, contatto);
        try {
            Contatto response = service.updateContatto(id, contatto);

            log.info("[PUT /contatti/{}] Modifica del contatto (200)", id);
            log.debug("[PUT /contatti/{}] response body: {}", id, response);

            return ResponseEntity.ok(mapper.toResponse(response));
        } catch (Exception e) {
            log.error("Errore {} durante la chiamata a [PUT /contatti/{}]: {}", e.getClass().getSimpleName(), id,
                    e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteContatto(@PathVariable Long id) {
        log.info("[DELETE /contatti/{}] Richiesta eliminazione contatto", id);
        try {
            service.deleteContatto(id);
            log.info("[DELETE /contatti/{}] Cancellazione completata (204 No Content)", id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            log.error("Errore {} durante la chiamata a [DELETE /contatti/{}]: {}", e.getClass().getSimpleName(), id,
                    e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/search")
    public ResponseEntity<List<ContattoResponse>> findByNomeAndCognomeOrCognomeOrNome(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) String cognome) {

        List<Contatto> results;

        log.info("[GET /contatti/search?nome={}&cognome={}]", nome, cognome);

        try {
            if (nome != null && cognome != null) {

                results = service.findByNomeAndCognome(nome, cognome);

            } else if (nome != null) {

                results = service.findByNome(nome);
            } else if (cognome != null) {

                results = service.findByCognome(cognome);
            } else {
                results = List.of();
            }

            List<ContattoResponse> response = results.stream().map(mapper::toResponse).toList();
            log.info("GET /contatti/search Ricerca avvenuta con successo (200)");
            log.debug("(GET /contatti/search Body response: {}", response);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Errore {} durante la chiamata a [GET /contatti/search?nome={}&cognome={}]\"]: {}",
                    e.getClass().getSimpleName(), nome, cognome, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/searchlike")
    public ResponseEntity<List<ContattoResponse>> findByNameLike(@RequestParam String nome) {
        log.info("[GET /contatti/searchlike?nome={}] Ricerca parziale contatti", nome);
        try {
            List<ContattoResponse> response = service.findByNameLike(nome)
                    .stream()
                    .map(mapper::toResponse)
                    .toList();
            log.debug("[GET /contatti/searchlike] Response body: {}", response);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Errore {} durante la chiamata a [GET /contatti/searchlike?nome={}\"]: {}",
                    e.getClass().getSimpleName(), nome, e.getMessage(), e);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/searchsort")
    public ResponseEntity<Page<ContattoResponse>> list(
            @PageableDefault(size = 10, sort = "cognome", direction = Sort.Direction.ASC) Pageable pageable,
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) String cognome) {

        log.info("[GET /contatti/searchsort] Ricerca con paginazione (nome='{}', cognome='{}')", nome, cognome);
        try {
            Page<Contatto> page;

            if (nome != null && !nome.isBlank()) {
                log.debug("[GET /contatti/searchsort] Filtro applicato: nome='{}'", nome);
                page = service.searchByNome(nome, pageable);
            } else if (cognome != null && !cognome.isBlank()) {
                log.debug("[GET /contatti/searchsort] Filtro applicato: cognome='{}'", cognome);
                page = service.searchByCognome(cognome, pageable);
            } else {
                log.debug("[GET /contatti/searchsort] Nessun filtro specificato: restituisco tutti i contatti");
                page = service.findAll(pageable);
            }

            Page<ContattoResponse> result = page.map(mapper::toResponse);

            log.info("[GET /contatti/searchsort] Ricerca completata (200 OK) - Pagine: {}, Totale elementi: {}",
                    result.getTotalPages(), result.getTotalElements());
            log.debug("[GET /contatti/searchsort] Response body: {}", result.getContent());

            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error("Errore {} durante la chiamata a [GET /contatti/searchsort?nome={}&cognome={}\"]: {}",
                    e.getClass().getSimpleName(), nome, cognome, e.getMessage(), e);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}