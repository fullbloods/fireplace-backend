package com.fullbloods.fireplace.fire.dto.response;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FireUUIDResponse {
    private UUID uuid;
}
