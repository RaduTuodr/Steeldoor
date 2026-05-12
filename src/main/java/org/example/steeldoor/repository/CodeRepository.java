package org.example.steeldoor.repository;

import jakarta.validation.constraints.NotNull;
import org.example.steeldoor.model.Code;
import org.example.steeldoor.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CodeRepository extends JpaRepository<Code, Integer> {

    Optional<Code> findTopByUserOrderByExpirationDateDesc(@NotNull User user);
}
