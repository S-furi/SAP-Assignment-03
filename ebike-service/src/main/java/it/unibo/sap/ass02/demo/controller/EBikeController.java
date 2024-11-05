package it.unibo.sap.ass02.demo.controller;

import it.unibo.sap.ass02.demo.domain.EBikeImpl;
import it.unibo.sap.ass02.demo.service.EBikeService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/ebike")
public class EBikeController {
    @NotNull
    private final EBikeService eBikeService;
    public EBikeController(final EBikeService eBikeService) {
        this.eBikeService = eBikeService;
    }

    @GetMapping("/all")
    public Iterable<EBikeImpl> getAllEBikes() {
        return this.eBikeService.getAllEBike();
    }

    @GetMapping("/{id}")
    public Optional<EBikeImpl> getEBikeById(@PathVariable String id) {
        if (Objects.nonNull(id)) {
            return this.eBikeService.getEBikeById(id);
        }
        return Optional.empty();
    }

    @PostMapping("/create")
    public EBikeImpl postEBike(@Valid @RequestBody EBikeImpl ebike) {
        return this.eBikeService.createNewEBike(ebike);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EBikeImpl> putEBike(final @PathVariable String id, final @RequestBody EBikeImpl ebike) {
        return this.eBikeService.existsById(id) ?
                new ResponseEntity<EBikeImpl>(this.eBikeService.createNewEBike(ebike), HttpStatus.OK) :
                new ResponseEntity<EBikeImpl>(this.postEBike(ebike), HttpStatus.CREATED);
    }
}
