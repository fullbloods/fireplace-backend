package com.fullbloods.fireplace.fire.repository;

import com.fullbloods.fireplace.fire.entity.Fire;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface FireRepository extends JpaRepository<Fire, UUID> {
    boolean existsById(UUID id);

    boolean existsByName(String name);
}
