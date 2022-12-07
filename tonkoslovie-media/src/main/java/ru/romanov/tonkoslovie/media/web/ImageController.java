package ru.romanov.tonkoslovie.media.web;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.romanov.tonkoslovie.media.image.ImageService;
import ru.romanov.tonkoslovie.media.web.response.UploadResponse;

import java.io.InputStream;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/media/image")
public class ImageController {

    private final ImageService imageService;

    @SneakyThrows
    @GetMapping("/{filePrefix}/{size}")
    public ResponseEntity<InputStreamResource> getImage(@PathVariable String filePrefix, @PathVariable String size) {
        InputStream inputStream = imageService.getImage(filePrefix, size);

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(new InputStreamResource(inputStream));
    }

    @PostMapping
    public ResponseEntity<UploadResponse> uploadImage(@RequestParam MultipartFile file) {
        String fileName = imageService.saveImage(file);
        return ResponseEntity.ok(new UploadResponse(fileName));
    }

    @DeleteMapping
    public void deleteImage(@RequestParam String fileName) {
        imageService.deleteImage(fileName);
    }

}
