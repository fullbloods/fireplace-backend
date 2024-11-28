package com.fullbloods.fireplace.img.controller;

import com.fullbloods.fireplace.img.service.ImgService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/img")
@RequiredArgsConstructor
public class ImgController {
    private final ImgService service;

    @GetMapping(path = "/**", produces = {MediaType.IMAGE_PNG_VALUE})
    public Resource getImg(HttpServletRequest request) {
        byte[] img = service.getImg(request.getRequestURI());

        return new ByteArrayResource(img);
    }
}
