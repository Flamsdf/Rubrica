package com.example.rubrica.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import com.example.rubrica.dto.ContattoRequest;
import com.example.rubrica.dto.ContattoResponse;
import com.example.rubrica.mapper.ContattoMapper;
import com.example.rubrica.model.Contatto;
import com.example.rubrica.service.ContattoService;

@Controller
public class PageController {

    private static final Logger log = LoggerFactory.getLogger(PageController.class);
    private final ContattoService service;
    private final ContattoMapper mapper;

    public PageController(ContattoService service, ContattoMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @GetMapping("/")
    public String index(Model model) {
        log.info("[GET /] Richiesta lista contatti");
        try {
            List<ContattoResponse> contatti = service.findAll()
                    .stream()
                    .map(mapper::toResponse)
                    .toList();
            model.addAttribute("contatti", contatti);
            return "index";
        } catch (Exception e) {
            log.error("Errore durante il caricamento della lista contatti: {}", e.getMessage(), e);
            model.addAttribute("errorMessage", "Errore durante il caricamento dei contatti.");
            return "index";
        }
    }

    @GetMapping("/contatti/{id}")
    public String dettaglio(@PathVariable Long id, Model model) {
        log.info("[GET /contatti/{}] Richiesta dettagli contatto", id);
        try {
            return service.findById(id)
                    .map(mapper::toResponse)
                    .map(cr -> {
                        model.addAttribute("contatto", cr);
                        log.info("Contatto id={} trovato", id);
                        return "contatto";
                    })
                    .orElseGet(() -> {
                        log.warn("Contatto id={} non trovato", id);
                        model.addAttribute("errorMessage", "Contatto non trovato");
                        return "not-found";
                    });
        } catch (Exception e) {
            log.error("Errore durante il caricamento del contatto id={}: {}", id, e.getMessage(), e);
            model.addAttribute("errorMessage", "Errore durante il caricamento del contatto.");
            return "error";
        }
    }

    @PostMapping("/contatti")
    public String inserisci(@ModelAttribute ContattoRequest form, Model model) {
        log.info("[POST /contatti] Inserimento nuovo contatto: {}", form);
        try {
            Contatto entity = mapper.toEntity(form);
            Contatto salvato = service.insertContatto(entity);
            log.info("Contatto creato con id={}", salvato.getId());
            return "redirect:/";
        } catch (Exception e) {
            log.error("Errore durante l'inserimento del contatto: {}", e.getMessage(), e);
            model.addAttribute("errorMessage", "Errore durante l'inserimento del contatto.");
            return "index";
        }
    }

    @DeleteMapping("/contatti/{id}")
    public String elimina(@PathVariable Long id, Model model) {
        log.info("[DELETE /contatti/{}] Richiesta eliminazione contatto", id);
        try {
            service.deleteContatto(id);
            log.info("Contatto id={} eliminato", id);
            return "redirect:/";
        } catch (Exception e) {
            log.error("Errore durante l'eliminazione del contatto id={}: {}", id, e.getMessage(), e);
            model.addAttribute("errorMessage", "Errore durante l'eliminazione del contatto.");
            return "index";
        }
    }

    @PutMapping("/contatti/{id}")
    public String aggiorna(@PathVariable Long id, @ModelAttribute ContattoRequest form, Model model) {
        log.info("[PUT /contatti/{}] Aggiornamento contatto", id);
        try {
            Contatto salvato = service.updateContatto(id, form);
            log.info("Contatto id={} aggiornato", id);
            model.addAttribute("contatto", mapper.toResponse(salvato));
            return "redirect:/";
        } catch (Exception e) {
            log.error("Errore durante l'aggiornamento del contatto id={}: {}", id, e.getMessage(), e);
            model.addAttribute("errorMessage", "Errore durante l'aggiornamento del contatto.");
            return "index";
        }
    }

    @GetMapping("/contatti/{id}/edit")
    public String mostraFormModifica(@PathVariable Long id, Model model) {
        log.info("[GET /contatti/{}/edit] Richiesta form modifica contatto", id);
        try {
            Contatto contatto = service.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Contatto non trovato: " + id));
            model.addAttribute("contatto", mapper.toResponse(contatto));
            return "edit";
        } catch (IllegalArgumentException e) {
            log.warn("Contatto id={} non trovato per modifica", id);
            model.addAttribute("errorMessage", "Contatto non trovato.");
            return "not-found";
        } catch (Exception e) {
            log.error("Errore durante il caricamento del form di modifica id={}: {}", id, e.getMessage(), e);
            model.addAttribute("errorMessage", "Errore durante il caricamento del form.");
            return "error";
        }
    }
}
