package it.unibo.sap.ass02.demo.repositories;

import it.unibo.sap.ass02.demo.domain.P2d;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface P2dRepository extends JpaRepository<P2d, Long> {
    Optional<P2d> findByXAndY(final double x, final double y);
}
