package com.fullbloods.fireplace.letter.dto;

import com.fullbloods.fireplace.letter.entity.LetterType;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LetterCreateDto {
    private String name;

    private String content;

    private String music;

    private boolean isPrivate;

    private String password;

    private LocalDate openAt;
}
