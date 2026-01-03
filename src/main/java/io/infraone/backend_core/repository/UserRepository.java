package io.infraone.backend_core.repository;

import io.infraone.backend_core.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByName(String name);

    boolean existsByName(String name);

    boolean existsByEmail(String email);

    List<User> findBySellerId(Long sellerId);

    Optional<User> findByIdAndSellerId(Long userId, Long sellerId);

}