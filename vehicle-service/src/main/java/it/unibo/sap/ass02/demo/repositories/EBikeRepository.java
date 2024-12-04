package it.unibo.sap.ass02.demo.repositories;

import it.unibo.sap.ass02.demo.domain.EBikeImpl;
import org.springframework.data.repository.CrudRepository;

public interface EBikeRepository extends CrudRepository<EBikeImpl, String> {
}
