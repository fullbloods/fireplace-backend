package com.fullbloods.fireplace.letter.dto.response;

import com.fullbloods.fireplace.letter.entity.LetterStatus;
import com.fullbloods.fireplace.letter.entity.LetterType;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LetterListResponse {
    private UUID uuid;
    private String name;
    private int diffDate;
    private LetterType type;
    private LetterStatus status;
}
