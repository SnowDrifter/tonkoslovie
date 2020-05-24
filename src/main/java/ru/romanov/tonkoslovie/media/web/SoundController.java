package ru.romanov.tonkoslovie.media.web;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.romanov.tonkoslovie.media.MediaService;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/media/sound")
public class SoundController {

    private final MediaService mediaService;

    @PostMapping
    public ResponseEntity uploadSound(@RequestParam MultipartFile file) {
        return mediaService.saveSound(file);
    }

    @DeleteMapping
    public ResponseEntity deleteSound(@RequestParam String fileName) {
        return mediaService.deleteSound(fileName);
    }

}
