package it.unibo.sap.ass02.demo.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.models.annotations.OpenAPI30;
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

    @Operation(summary = "Get all the ebike stored inside the service.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found all the ebikes stored.")
    })
    @GetMapping("/all")
    public Iterable<EBikeImpl> getAllEBikes() {
        return this.eBikeService.getAllEBike();
    }

    @Operation(summary = "Get a specific ebike using Its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Get the founded ebike, It can also empty because of the ID")
    })
    @GetMapping("/{id}")
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
    @PostMapping("/create")
    public EBikeImpl postEBike(@Valid @RequestBody EBikeImpl ebike) {
        return this.eBikeService.createNewEBike(ebike);
    }

    @Operation(summary = "Update an existing ebike by using Its ID, if It does not exist we try to create a new ebike.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Everything went ok, all the input values were good"),
            @ApiResponse(responseCode = "400", description = "If one of the parameters Its not correct")
    })
    @PutMapping("/update/{id}")
    public ResponseEntity<EBikeImpl> putEBike(final @PathVariable String id, final @RequestBody EBikeImpl ebike) {
        return this.eBikeService.existsById(id) ?
                new ResponseEntity<EBikeImpl>(this.eBikeService.createNewEBike(ebike), HttpStatus.OK) :
                new ResponseEntity<EBikeImpl>(this.postEBike(ebike), HttpStatus.CREATED);
    }

    @PutMapping("/update/location/{id}")
    public ResponseEntity<Void> updateLocation(final @PathVariable String id,
                                                    final @RequestParam Double x,
                                                    final @RequestParam Double y) {
        this.eBikeService.updateLocation(id, x, y);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }
}
