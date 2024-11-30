package it.unibo.sap.ass02.demo.controller;

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
public class EBikeController {
    @NotNull
    private final EBikeService eBikeService;
    public EBikeController(final EBikeService eBikeService) {
        this.eBikeService = eBikeService;
    }

    @Operation(summary = "Get all the ebike stored inside the service.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found all the ebikes stored.")
    })
    @GetMapping(EBikeRoutes.ALL_EBIKES)
    public Iterable<EBikeImpl> getAllEBikes() {
        return this.eBikeService.getAllEBike();
    }

    @Operation(summary = "Get a specific ebike using Its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Get the founded ebike, It can also empty because of the ID")
    })
    @GetMapping(EBikeRoutes.SINGLE_EBIKE)
    public Optional<EBikeImpl> getEBikeById(@PathVariable String id) {
        if (Objects.nonNull(id)) {
            return this.eBikeService.getEBikeById(id);
        }
        return Optional.empty();
    }

    @Operation(summary = "Add a new ebike into the service")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Everything went ok, all the input values were good"),
            @ApiResponse(responseCode = "400", description = "If one of the parameters Its not correct")
    })
    @PostMapping(EBikeRoutes.CREATE_EBIKE)
    public ResponseEntity<EBikeImpl> postEBike(@Valid @RequestBody EBikeImpl ebike) {
        return new ResponseEntity<>(this.eBikeService.createNewEBike(ebike), HttpStatus.CREATED);
    }

    @Operation(summary = "Update an existing ebike by using Its ID, if It does not exist we try to create a new ebike.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Everything went ok, all the input values were good"),
            @ApiResponse(responseCode = "400", description = "If one of the parameters Its not correct")
    })
    @PutMapping(EBikeRoutes.UPDATE_EBIKE)
    public ResponseEntity<EBikeImpl> putEBike(final @PathVariable String id, final @RequestBody EBikeImpl ebike) {
        return this.eBikeService.existsById(id) ?
                new ResponseEntity<EBikeImpl>(this.eBikeService.createNewEBike(ebike), HttpStatus.OK) :
                this.postEBike(ebike);
    }

    @PutMapping(EBikeRoutes.UPDATE_EBIKE_LOCATION)
    @Operation(summary = "Update an ebike location using the input parameters.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Everything went ok, all the input values were good"),
            @ApiResponse(responseCode = "400", description = "If one of the parameters Its not correct")
    })
    public ResponseEntity<Void> updateLocation(final @PathVariable String id,
                                                    final @RequestParam Double x,
                                                    final @RequestParam Double y) {
        this.eBikeService.updateLocation(id, x, y);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }


    @PutMapping(EBikeRoutes.UPDATE_EBIKE_BATTERY)
    @Operation(summary = "Update ebike's battery level using the input parameters.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Everything went ok, all the input values were good"),
            @ApiResponse(responseCode = "400", description = "If one of the parameters Its not correct")
    })
    public ResponseEntity<Void> updateBattery(final @PathVariable String id,
                                              final @RequestParam Integer delta) {
        this.eBikeService.updateBattery(id, delta);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @PutMapping(EBikeRoutes.UPDATE_EBIKE_STATE)
    @Operation(summary = "Update ebike's state using the input parameters.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Everything went ok, all the input values were good"),
            @ApiResponse(responseCode = "400", description = "If one of the parameters Its not correct") })
    public ResponseEntity<Void> updateEbikeState(final  @PathVariable String id,
                                                   final @RequestParam String state) {
        this.eBikeService.updateState(id, state);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
