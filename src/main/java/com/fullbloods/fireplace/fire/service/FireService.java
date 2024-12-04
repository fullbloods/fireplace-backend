package com.fullbloods.fireplace.fire.service;

import com.fullbloods.fireplace.fire.dto.FireCreateDto;
import com.fullbloods.fireplace.fire.dto.FireDto;
import com.fullbloods.fireplace.fire.entity.Fire;
import com.fullbloods.fireplace.fire.repository.FireRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FireService {
    private final FireRepository repository;
    private final PasswordEncoder passwordEncoder;

    public FireDto findDtoByUUID(UUID uuid) {
        return repository.findById(uuid).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 벽난로입니다.")).toDto();
    }

    public UUID create(FireCreateDto dto, HttpServletRequest request) {
        if (repository.existsByName(dto.getName())) {
            throw new IllegalArgumentException("이미 존재한 이름입니다.");
        }

        UUID uuid = UUID.randomUUID();

        while (repository.existsById(uuid)) {
            uuid = UUID.randomUUID();
        }

        Fire fire = Fire.builder()
                .uuid(uuid)
                .name(dto.getName())
                .password(passwordEncoder.encode(dto.getPassword()))
                .ip(request.getRemoteAddr())
                .build();

        repository.save(fire);

        return uuid;
    }
}
