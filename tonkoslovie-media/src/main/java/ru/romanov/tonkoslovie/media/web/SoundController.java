package ru.romanov.tonkoslovie.media.web;


import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.romanov.tonkoslovie.media.sound.SoundService;
import ru.romanov.tonkoslovie.media.web.response.UploadResponse;

import jakarta.servlet.http.HttpServletResponse;
import java.io.InputStream;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/media/sound")
public class SoundController {

    private final SoundService soundService;

    @SneakyThrows
    @GetMapping("/{filename}")
    public ResponseEntity<InputStreamResource> getSound(@PathVariable String filename, HttpServletResponse response) {
        InputStream inputStream = soundService.getSound(filename);

        return ResponseEntity.ok()
                .contentType(new MediaType("audio", "mpeg"))
                .body(new InputStreamResource(inputStream));
    }

    @PostMapping
    public ResponseEntity<UploadResponse> uploadSound(@RequestParam MultipartFile file) {
        String fileName = soundService.saveSound(file);
        return ResponseEntity.ok(new UploadResponse(fileName));
    }

    @DeleteMapping
    public void deleteSound(@RequestParam String fileName) {
        soundService.deleteSound(fileName);
    }

}
