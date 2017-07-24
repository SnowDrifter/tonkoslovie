package ru.romanov.tonkoslovie.media.web;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.romanov.tonkoslovie.media.MediaService;

import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("/api/media/sound")
public class SoundController {

    private final MediaService mediaService;

    @Autowired
    public SoundController(MediaService mediaService) {
        this.mediaService = mediaService;
    }

    @PostMapping
    public ResponseEntity uploadSound(@RequestParam MultipartFile file) throws IOException {
        return mediaService.saveSound(file);
    }

    @DeleteMapping
    public ResponseEntity deleteSound(@RequestParam String fileName) throws IOException {
        return mediaService.deleteSound(fileName);
    }

}
