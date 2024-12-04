package com.fullbloods.fireplace.fire.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fullbloods.fireplace.fire.dto.FireDto;
import com.fullbloods.fireplace.letter.entity.Letter;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Fire {
    @Id
    private UUID uuid;

    private String name;

    @JsonIgnore
    @Column(columnDefinition = "TEXT")
    private String password;

    @JsonIgnore
    @OneToMany(mappedBy = "fire", cascade = {CascadeType.REMOVE}, orphanRemoval = true)
    private List<Letter> letters;

    @JsonIgnore
    private String ip;

    public FireDto toDto() {
        return FireDto.builder()
                .uuid(uuid)
                .name(name)
                .password(password)
                .build();
    }
}
