package ru.romanov.tonkoslovie.media.web;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.romanov.tonkoslovie.media.MediaService;
import ru.romanov.tonkoslovie.media.web.response.UploadResponse;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/media/sound")
public class SoundController {

    private final MediaService mediaService;

    @PostMapping
    public ResponseEntity<UploadResponse> uploadSound(@RequestParam MultipartFile file) {
        String fileName = mediaService.saveSound(file);
        return ResponseEntity.ok(new UploadResponse(fileName));
    }

    @DeleteMapping
    public void deleteSound(@RequestParam String fileName) {
        mediaService.deleteSound(fileName);
    }

}
