package com.fullbloods.fireplace.letter.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fullbloods.fireplace.fire.entity.Fire;
import com.fullbloods.fireplace.letter.dto.LetterDto;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Letter {
    @Id
    private UUID uuid;

    @ManyToOne(fetch = FetchType.LAZY)
    private Fire fire;

    private String name;

    @Column(columnDefinition = "TEXT")
    private String content;

    private String music;

    private LetterType type;

    @JsonIgnore
    @Column(columnDefinition = "TEXT")
    private String password;

    @Column(columnDefinition = "TEXT")
    private String reply;

    private LocalDateTime replyAt;

    private String replyMusic;

    private LocalDateTime createdAt;

    private LocalDate openAt;

    private boolean isRead;

    public LetterDto toDto() {
        return LetterDto.builder()
                .uuid(uuid)
                .fire(fire.toDto())
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
                .status(getStatus())
                .build();
    }

    public LetterStatus getStatus() {
        return openAt.isBefore(LocalDate.now().plusDays(1)) ? LetterStatus.OPEN : LetterStatus.CLOSED;
    }
}
