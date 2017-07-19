package ru.romanov.tonkoslovie.media.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
@RestController
@RequestMapping("/api/media/upload")
public class UploadController {

    @Value("${media.imageDirectory}")
    private String imageDirectory;

    @PostMapping("/image")
    public ResponseEntity uploadImage(@RequestParam MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        try {
            Path directory = Paths.get(imageDirectory);
            if (Files.notExists(directory)) {
                Files.createDirectories(directory);
            }

            Files.write(Paths.get(imageDirectory, file.getOriginalFilename()), file.getBytes());
            return ResponseEntity.ok().build();
        } catch (IOException e) {
            log.error("Error save file, message={}", e);
            return ResponseEntity.badRequest().build();
        }
    }

}
