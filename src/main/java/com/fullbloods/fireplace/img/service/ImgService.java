package com.fullbloods.fireplace.img.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
@RequiredArgsConstructor
@Slf4j
public class ImgService {

    @Value("${file.path}")
    private String filePath;

    public byte[] getImg(String filename) {

        filename = filename.replaceAll("/img/", "");

        if (filename.contains("/..")) {
            throw new IllegalArgumentException("부적절한 접근입니다.");
        }

        int dotIdx = filename.indexOf(".");

        if (dotIdx == -1) {
            throw new IllegalArgumentException("부적절한 접근입니다.");
        }

        String extension = filename.substring(dotIdx);

        if (!extension.equalsIgnoreCase(".png") && !extension.equalsIgnoreCase(".jpg") && !extension.equalsIgnoreCase(".jpeg")) {
            throw new IllegalArgumentException("허용하지 않는 확장자입니다.");
        }

        try {
            Path imgPath = Path.of(filePath, filename);

            return Files.readAllBytes(imgPath);
        } catch (Exception e) {
            log.info(filePath + "" + filename);
            throw new IllegalArgumentException("파일을 불러올 수 없습니다.");
        }
    }

    public String imgSave(MultipartFile img, String folderPath, String filename) {
        String _filename = img.getOriginalFilename();
        int dotIdx = _filename.indexOf(".");
        String extension = _filename.substring(dotIdx);

        _filename = filename + extension;

        String backImgPath = filePath + folderPath + _filename;

        try {

            File destinationDir = new File(filePath + folderPath);
            if (!destinationDir.exists()) {
                destinationDir.mkdirs();
            }

            img.transferTo(new File(backImgPath));

            return folderPath + _filename;
        } catch (Exception e) {
            throw new IllegalStateException("업로드 실패");
        }
    }
}
