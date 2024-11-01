package it.unibo.sap.ass02.demo.service;

import it.unibo.sap.ass02.demo.domain.EBikeImpl;
import it.unibo.sap.ass02.demo.domain.P2d;
import it.unibo.sap.ass02.demo.repositories.EBikeRepository;
import it.unibo.sap.ass02.demo.repositories.P2dRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        final P2d location = eBike.getLocation();
        final P2d savedLocation = this.p2dRepository
                .findByXAndY(location.x(), location.y())
                .orElseGet(() -> this.p2dRepository.save(location));
        eBike.updateLocation(savedLocation);
        return this.eBikeRepository.save(eBike);
    }

    public Iterable<EBikeImpl> getAllEBike() {
        return this.eBikeRepository.findAll();
    }
}
