package com.fullbloods.fireplace.fire.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fullbloods.fireplace.fire.entity.Fire;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FireDto {
    private UUID uuid;

    private String name;

    @JsonIgnore
    private String password;

    public Fire toEntity() {
        return Fire.builder()
                .uuid(uuid)
                .name(name)
                .password(password)
                .build();
    }
}
