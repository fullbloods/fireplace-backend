package com.fullbloods.fireplace.letter.service;

import com.fullbloods.fireplace.fire.dto.FireDto;
import com.fullbloods.fireplace.fire.repository.FireRepository;
import com.fullbloods.fireplace.fire.service.FireService;
import com.fullbloods.fireplace.letter.dto.*;
import com.fullbloods.fireplace.letter.dto.response.LetterListResponse;
import com.fullbloods.fireplace.letter.entity.Letter;
import com.fullbloods.fireplace.letter.entity.LetterStatus;
import com.fullbloods.fireplace.letter.entity.LetterType;
import com.fullbloods.fireplace.letter.repository.LetterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LetterService {
    private final LetterRepository repository;
    private final FireService fireService;
    private final PasswordEncoder passwordEncoder;

    public LetterDto findDtoByUUID(UUID uuid) {
        return repository.findById(uuid).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 편지입니다.")).toDto();
    }

    public LetterDto get(UUID uuid, LetterPasswordDto dto) {
        LetterDto letter = this.findDtoByUUID(uuid);

        if (letter.getStatus().equals(LetterStatus.CLOSED)) {
            throw new IllegalStateException("아직 잠겨있는 편지입니다.");
        }

        if (letter.getType().equals(LetterType.PRIVATE)) {

            if (dto == null || dto.getPassword() == null) {
                throw new IllegalArgumentException("비밀번호를 입력해주세요.");
            }

            if (!passwordEncoder.matches(dto.getPassword(), letter.getPassword()) || !passwordEncoder.matches(dto.getPassword(), letter.getFire().getPassword())) {
                throw new IllegalStateException("잘못된 비밀번호입니다.");
            }
        }

        return letter;
    }

    public void create(UUID fireUUID, LetterCreateDto dto) {
        FireDto fire = fireService.findDtoByUUID(fireUUID);

        UUID uuid = UUID.randomUUID();

        while (repository.existsById(uuid)) {
            uuid = UUID.randomUUID();
        }

        Letter letter = Letter.builder()
                .uuid(uuid)
                .fire(fire.toEntity())
                .name(dto.getName())
                .content(dto.getContent())
                .music(dto.getMusic())
                .type(dto.isPrivate() ? LetterType.PRIVATE : LetterType.PUBLIC)
                .password(passwordEncoder.encode(dto.getPassword()))
                .createdAt(LocalDateTime.now())
                .openAt(dto.getOpenAt())
                .isRead(false)
                .build();

        repository.save(letter);
    }

    public List<LetterListResponse> getLetterListByFire(UUID uuid) {
        FireDto fire = fireService.findDtoByUUID(uuid);

        List<LetterListResponse> letters = repository.findByFire(fire.toEntity()).stream().map((e) -> {

            return LetterListResponse.builder()
                    .uuid(e.getUuid())
                    .name(e.getName())
                    .diffDate(e.getType().equals(LetterType.PUBLIC) ? 0 : (int) ChronoUnit.DAYS.between(e.getOpenAt(), LocalDate.now()))
                    .type(e.getType())
                    .status(e.getStatus())
                    .build();
        }).toList();

        return letters;
    }

    public void reply(UUID uuid, ReplyDto dto) {
        LetterDto letter = this.findDtoByUUID(uuid);

        if (!passwordEncoder.matches(dto.getPassword(), letter.getFire().getPassword())) {
            throw new IllegalArgumentException("편지 주인만 답장할 수 있습니다.");
        }

        letter.setReply(dto.getMessage());
        letter.setReplyAt(LocalDateTime.now());
        letter.setReplyMusic(dto.getMusic());

        repository.save(letter.toEntity());
    }

    public void cert(UUID uuid, LetterPasswordDto dto) {
        LetterDto letter = this.findDtoByUUID(uuid);

        if (letter.getType().equals(LetterType.PUBLIC)) {
            throw new IllegalStateException("이 편지는 잠긴 편지가 아닙니다.");
        }

        if (dto == null || dto.getPassword() == null) {
            throw new IllegalArgumentException("비밀번호를 입력해주세요.");
        }

        if (!passwordEncoder.matches(dto.getPassword(), letter.getPassword()) || !passwordEncoder.matches(dto.getPassword(), letter.getFire().getPassword())) {
            throw new IllegalStateException("잘못된 비밀번호입니다.");
        }
    }

    public void certOwner(UUID uuid, LetterPasswordDto dto) {
        LetterDto letter = this.findDtoByUUID(uuid);

        if (letter.getType().equals(LetterType.PUBLIC)) {
            throw new IllegalStateException("이 편지는 잠긴 편지가 아닙니다.");
        }

        if (dto == null || dto.getPassword() == null) {
            throw new IllegalArgumentException("비밀번호를 입력해주세요.");
        }

        if (!passwordEncoder.matches(dto.getPassword(), letter.getFire().getPassword())) {
            throw new IllegalStateException("잘못된 비밀번호입니다.");
        }
    }
}
