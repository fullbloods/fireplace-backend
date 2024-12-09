package com.fullbloods.fireplace.fire.controller;

import com.fullbloods.fireplace.common.response.CommonResponse;
import com.fullbloods.fireplace.fire.dto.FireCreateDto;
import com.fullbloods.fireplace.fire.dto.FireDto;
import com.fullbloods.fireplace.fire.dto.response.FireUUIDResponse;
import com.fullbloods.fireplace.fire.service.FireService;
import com.fullbloods.fireplace.letter.dto.LetterCreateDto;
import com.fullbloods.fireplace.letter.dto.response.LetterListResponse;
import com.fullbloods.fireplace.letter.service.LetterService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/fire")
@RequiredArgsConstructor
public class FireController {
    private final FireService service;
    private final LetterService letterService;

    @GetMapping("/{uuid}")
    public CommonResponse<FireDto> get(@PathVariable("uuid") UUID uuid) {
        FireDto fire = service.findDtoByUUID(uuid);

        return new CommonResponse<>(HttpStatus.OK.value(), fire, true, null);
    }

    @PostMapping("")
    public CommonResponse<FireUUIDResponse> create(@RequestBody FireCreateDto dto, HttpServletRequest request) {
        UUID uuid = service.create(dto, request);

        FireUUIDResponse response = FireUUIDResponse.builder()
                .uuid(uuid)
                .build();

        return new CommonResponse<>(HttpStatus.OK.value(), response, true, null);
    }

    @PostMapping("/{uuid}/letter")
    public CommonResponse<Void> createLetter(@PathVariable("uuid") String uuid, @RequestBody LetterCreateDto dto, HttpServletRequest request) {
        letterService.create(UUID.fromString(uuid), dto, request);

        return new CommonResponse<>(HttpStatus.OK.value(), null, true, null);
    }

    @GetMapping("/{uuid}/letter")
    public CommonResponse<List<LetterListResponse>> getLetterListByFire(@PathVariable("uuid") String uuid) {
        List<LetterListResponse> letters = letterService.getLetterListByFire(UUID.fromString(uuid));

        return new CommonResponse<>(HttpStatus.OK.value(), letters, true, null);
    }
}
