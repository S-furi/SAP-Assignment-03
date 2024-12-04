package it.unibo.sap.ass02.demo.service;

import it.unibo.sap.ass02.demo.domain.EBike;
import it.unibo.sap.ass02.demo.domain.EBikeImpl;
import it.unibo.sap.ass02.demo.domain.P2d;
import it.unibo.sap.ass02.demo.repositories.EBikeRepository;
import it.unibo.sap.ass02.demo.repositories.P2dRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EBikeService {
    private final EBikeRepository eBikeRepository;
    private final P2dRepository p2dRepository;

    @Autowired
    public EBikeService(final EBikeRepository eBikeRepository, final P2dRepository p2dRepository) {
        this.eBikeRepository = eBikeRepository;
        this.p2dRepository = p2dRepository;
    }

    public EBikeImpl createNewEBike(EBikeImpl eBike) {
        this.validateEBike(eBike);
        if (!this.existsById(eBike.getID())) {
            final P2d location = eBike.getLocation();
            final P2d savedLocation = this.p2dRepository
                    .findByXAndY(location.x(), location.y())
                    .orElseGet(() -> this.p2dRepository.save(location));
            eBike.updateLocation(savedLocation);
            return this.eBikeRepository.save(eBike);
        }else{
            throw new IllegalArgumentException("The input ebike ID already exists");
        }
    }

    public void updateLocation(final String ebikeId, final Double xPos, final Double yPos) {
        this.eBikeRepository.findById(ebikeId)
                .ifPresent((ebike) -> {
                    final P2d newPosition = this.p2dRepository
                                    .findByXAndY(xPos, yPos)
                                            .orElseGet(() -> this.p2dRepository.save(new P2d(xPos, yPos)));
                    ebike.updateLocation(newPosition);
                    this.eBikeRepository.save(ebike);
                });
    }

    public void updateState(final String ebikeId, final String state) {
        this.eBikeRepository.findById(ebikeId)
                .ifPresent((ebike) -> {
                    final EBike.EBikeState newState = EBike.EBikeState.fromValue(state.toUpperCase());
                    ebike.updateState(newState);
                    this.eBikeRepository.save(ebike);
                });
    }

    public void updateBattery(final String ebikeId, final Integer delta) {
        this.eBikeRepository.findById(ebikeId)
                .ifPresent((vehicle) -> {
                    if (delta >= 0 && delta <= 100) {
                        vehicle.decreaseBatteryLevel(delta);
                        this.eBikeRepository.save(vehicle);
                    }
                    else{
                        throw new IllegalArgumentException("The delta is lower than 0 or greater than 100");
                    }
                });
    }

    public Iterable<EBikeImpl> getAllEBike() {
        return this.eBikeRepository.findAll();
    }

    public Optional<EBikeImpl> getEBikeById(final String id) {
        return this.eBikeRepository.findById(id);
    }

    public boolean existsById(final String id) {
        return this.eBikeRepository.existsById(id);
    }

    private void validateEBike(final EBikeImpl inputEBike) {
        if (inputEBike.getBatteryLevel() < 0 || inputEBike.getBatteryLevel() > 100) {
            throw new IllegalArgumentException("The battery level must be positive and lower than 100");
        }
    }
}