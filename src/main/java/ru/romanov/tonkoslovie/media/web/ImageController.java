package ru.romanov.tonkoslovie.media.web;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.romanov.tonkoslovie.media.MediaService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/media/image")
public class ImageController {

    private final MediaService mediaService;

    @PostMapping
    public ResponseEntity uploadImage(@RequestParam MultipartFile file) {
        return mediaService.saveImage(file);
    }

    @DeleteMapping
    public ResponseEntity deleteImage(@RequestParam String fileName) {
        return mediaService.deleteImage(fileName);
    }

}
