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
import com.fullbloods.fireplace.utils.AESCrypt;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${aes.key}")
    private String key;

    public LetterDto findDtoByUUID(UUID uuid) {
        return repository.findById(uuid).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 편지입니다 :(")).toDto();
    }

    public LetterDto get(UUID uuid, LetterPasswordDto dto) {
        LetterDto letter = this.findDtoByUUID(uuid);

        if (letter.getStatus().equals(LetterStatus.CLOSED)) {
            throw new IllegalStateException("편지가 아직 도착하지 않았어요 :(");
        }

        if (letter.getType().equals(LetterType.PRIVATE)) {
            if (dto == null || dto.getPassword() == null) {
                throw new IllegalArgumentException("비밀번호를 입력해주세요!");
            }

            if (!passwordEncoder.matches(dto.getPassword(), letter.getPassword()) && !passwordEncoder.matches(dto.getPassword(), letter.getFire().getPassword())) {
                throw new IllegalStateException("잘못된 비밀번호입니다 :(");
            }
        }

        String content = "";
        String reply = null;

        try {
            if (letter.getType().equals(LetterType.PRIVATE)) {
                content = AESCrypt.decrypt(letter.getContent(), key);

                if (letter.getReply() != null) {
                    reply = AESCrypt.decrypt(letter.getReply(), key);
                }

            } else {
                content = letter.getContent();

                reply = letter.getReply();
            }
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }

        letter.setContent(content);
        letter.setReply(reply);

        return letter;
    }

    public void create(UUID fireUUID, LetterCreateDto dto, HttpServletRequest request) {
        FireDto fire = fireService.findDtoByUUID(fireUUID);

        UUID uuid = UUID.randomUUID();

        while (repository.existsById(uuid)) {
            uuid = UUID.randomUUID();
        }

        String content = "";

        try {
            if (dto.isPrivate()) {
                content = AESCrypt.encrypt(dto.getContent(), key);
            } else {
                content = dto.getContent();
            }
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }

        Letter letter = Letter.builder()
                .uuid(uuid)
                .fire(fire.toEntity())
                .name(dto.getName())
                .content(content)
                .music(dto.getMusic())
                .type(dto.isPrivate() ? LetterType.PRIVATE : LetterType.PUBLIC)
                .password(passwordEncoder.encode(dto.getPassword()))
                .createdAt(LocalDateTime.now())
                .openAt(dto.getOpenAt())
                .isRead(false)
                .ip(request.getRemoteAddr())
                .build();

        repository.save(letter);
    }

    public List<LetterListResponse> getLetterListByFire(UUID uuid) {
        FireDto fire = fireService.findDtoByUUID(uuid);

        List<LetterListResponse> letters = repository.findByFire(fire.toEntity()).stream().map((e) -> {

            return LetterListResponse.builder()
                    .uuid(e.getUuid())
                    .name(e.getName())
                    .diffDate((int) ChronoUnit.DAYS.between(e.getOpenAt(), LocalDate.now()))
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

        String message = "";

        try {

            if (letter.getType().equals(LetterType.PRIVATE)) {
                message = AESCrypt.encrypt(dto.getMessage(), key);
            } else {
                message = dto.getMessage();
            }

        } catch (Exception e) {
            throw new IllegalStateException(e);
        }

        letter.setReply(message);
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

        if (!passwordEncoder.matches(dto.getPassword(), letter.getPassword()) && !passwordEncoder.matches(dto.getPassword(), letter.getFire().getPassword())) {
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
