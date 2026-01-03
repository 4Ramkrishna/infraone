package io.infraone.backend_core.repository;

import io.infraone.backend_core.models.Seller;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SellerRepository extends JpaRepository<Seller, Long> {

    Optional<Seller> findByCode(String code);

    boolean existsByCode(String code);

    boolean existsByEmail(String email);

}