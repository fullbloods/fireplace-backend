package com.fullbloods.fireplace.letter.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fullbloods.fireplace.fire.dto.FireDto;
import com.fullbloods.fireplace.letter.entity.Letter;
import com.fullbloods.fireplace.letter.entity.LetterStatus;
import com.fullbloods.fireplace.letter.entity.LetterType;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LetterDto {
    private UUID uuid;

    private FireDto fire;

    private String name;

    private String content;

    private String music;

    private LetterType type;

    @JsonIgnore
    private String password;

    private String reply;

    private LocalDateTime replyAt;

    private String replyMusic;

    private LocalDateTime createdAt;

    private LocalDate openAt;

    private boolean isRead;

    private LetterStatus status;

    public Letter toEntity() {
        return Letter.builder()
                .uuid(uuid)
                .fire(fire.toEntity())
                .name(name)
                .content(content)
                .music(music)
                .type(type)
                .password(password)
                .reply(reply)
                .replyAt(replyAt)
                .replyMusic(replyMusic)
                .createdAt(createdAt)
                .openAt(openAt)
                .isRead(isRead)
                .build();
    }
}
