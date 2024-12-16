package it.unibo.sap.ass02.demo.controller.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import it.unibo.sap.ass02.demo.domain.EBikeImpl;
import it.unibo.sap.ass02.demo.service.EBikeService;
import it.unibo.sap.ass02.demo.service.routes.EBikeRoutes;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping(EBikeRoutes.EBIKE_ROUTE)
public class EBikeController implements EBikeRESTController {
    @NotNull
    private final EBikeService eBikeService;
    public EBikeController(final EBikeService eBikeService) {
        this.eBikeService = eBikeService;
    }

    @Override
    public Iterable<EBikeImpl> getAllEBikes() {
        return this.eBikeService.getAllEBike();
    }

    @Override
    public Optional<EBikeImpl> getEBikeById(@PathVariable String id) {
        if (Objects.nonNull(id)) {
            return this.eBikeService.getEBikeById(id);
        }
        return Optional.empty();
    }

    @Override
    public ResponseEntity<EBikeImpl> postEBike(@Valid @RequestBody EBikeImpl ebike) {
        return new ResponseEntity<>(this.eBikeService.createNewEBike(ebike), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<EBikeImpl> putEBike(final @PathVariable String id, final @RequestBody EBikeImpl ebike) {
        return this.eBikeService.existsById(id) ?
                new ResponseEntity<EBikeImpl>(this.eBikeService.createNewEBike(ebike), HttpStatus.OK) :
                this.postEBike(ebike);
    }

    @Override
    public ResponseEntity<Void> updateLocation(final @PathVariable String id,
                                                    final @RequestParam Double x,
                                                    final @RequestParam Double y) {
        this.eBikeService.updateLocation(id, x, y);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }


    @Override
    public ResponseEntity<Void> updateBattery(final @PathVariable String id,
                                              final @RequestParam Integer delta) {
        this.eBikeService.updateBattery(id, delta);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> updateEbikeState(final  @PathVariable String id,
                                                   final @RequestParam String state) {
        this.eBikeService.updateState(id, state);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
