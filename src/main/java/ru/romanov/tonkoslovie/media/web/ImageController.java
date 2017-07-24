package ru.romanov.tonkoslovie.media.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.romanov.tonkoslovie.media.MediaService;

import java.io.IOException;

@RestController
@RequestMapping("/api/media/image")
public class ImageController {

    private final MediaService mediaService;

    @Autowired
    public ImageController(MediaService mediaService) {
        this.mediaService = mediaService;
    }

    @PostMapping
    public ResponseEntity uploadImage(@RequestParam MultipartFile file) throws IOException {
        return mediaService.saveImage(file);
    }

    @DeleteMapping
    public ResponseEntity deleteImage(@RequestParam String fileName) throws IOException {
        return mediaService.deleteImage(fileName);
    }

}
