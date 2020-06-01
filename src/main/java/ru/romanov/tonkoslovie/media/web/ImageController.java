package ru.romanov.tonkoslovie.media.web;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.romanov.tonkoslovie.media.MediaService;
import ru.romanov.tonkoslovie.media.web.response.UploadResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/media/image")
public class ImageController {

    private final MediaService mediaService;

    @PostMapping
    public ResponseEntity uploadImage(@RequestParam MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        String fileName = mediaService.saveImage(file);
        return ResponseEntity.ok(new UploadResponse(fileName));
    }

    @DeleteMapping
    public ResponseEntity deleteImage(@RequestParam String fileName) {
        mediaService.deleteImage(fileName);
        return ResponseEntity.ok().build();
    }

}
