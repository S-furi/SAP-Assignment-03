package it.unibo.sap.ass02.demo.controller;

import it.unibo.sap.ass02.demo.domain.EBikeImpl;
import it.unibo.sap.ass02.demo.repositories.EBikeRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Objects;
import java.util.Optional;

@RequestMapping("/ebike")
public class EBikeController {
    private final EBikeRepository repository;
    public EBikeController(final EBikeRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/all")
    public Iterable<EBikeImpl> getAllEBikes() {
        return this.repository.findAll();
    }

    @GetMapping("/{id}")
    public Optional<EBikeImpl> getEBikeByID(@PathVariable String id) {
        if (Objects.nonNull(id)) {
            return this.repository.findById(id);
        }
        return Optional.empty();
    }
}
