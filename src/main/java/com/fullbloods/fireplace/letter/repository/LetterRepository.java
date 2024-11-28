package com.fullbloods.fireplace.letter.repository;

import com.fullbloods.fireplace.fire.entity.Fire;
import com.fullbloods.fireplace.letter.dto.LetterDto;
import com.fullbloods.fireplace.letter.entity.Letter;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LetterRepository extends JpaRepository<Letter, UUID> {
    Optional<Letter> findByUuidAndFire(UUID uuid, Fire fire);

    List<Letter> findByFire(Fire fire);
}
