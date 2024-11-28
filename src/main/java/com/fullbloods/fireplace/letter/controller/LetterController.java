package com.fullbloods.fireplace.letter.controller;

import com.fullbloods.fireplace.common.response.CommonResponse;
import com.fullbloods.fireplace.letter.dto.LetterDto;
import com.fullbloods.fireplace.letter.dto.LetterPasswordDto;
import com.fullbloods.fireplace.letter.dto.ReplyDto;
import com.fullbloods.fireplace.letter.service.LetterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/letter")
@RequiredArgsConstructor
public class LetterController {
    private final LetterService service;

    @GetMapping("/{uuid}")
    public CommonResponse<LetterDto> get(@PathVariable("uuid") String uuid, @RequestBody(required = false) LetterPasswordDto dto) {
        LetterDto letter = service.get(UUID.fromString(uuid), dto);

        return new CommonResponse<>(HttpStatus.OK.value(), letter, true, null);
    }

    @PostMapping("/{uuid}/reply")
    public CommonResponse<Void> reply(@PathVariable("uuid") String uuid, @RequestBody ReplyDto dto) {
        service.reply(UUID.fromString(uuid), dto);

        return new CommonResponse<>(HttpStatus.OK.value(), null, true, null);
    }

    @PostMapping("/{uuid}/cert")
    public CommonResponse<Void> cert(@PathVariable("uuid") String uuid, @RequestBody LetterPasswordDto dto) {
        service.cert(UUID.fromString(uuid), dto);

        return new CommonResponse<>(HttpStatus.OK.value(), null, true, null);
    }

    @PostMapping("/{uuid}/cert/admin")
    public CommonResponse<Void> certAdmin(@PathVariable("uuid") String uuid, @RequestBody LetterPasswordDto dto) {
        service.certAdmin(UUID.fromString(uuid), dto);

        return new CommonResponse<>(HttpStatus.OK.value(), null, true, null);
    }

}
