package ru.romanov.tonkoslovie.media;

import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.romanov.tonkoslovie.media.web.response.SoundResponse;

import javax.imageio.ImageIO;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Slf4j
@Service
public class MediaServiceImpl implements MediaService {

    @Value("${media.imageDirectory}")
    private String imageDirectory;
    @Value("${media.soundDirectory}")
    private String soundDirectory;

    @Override
    public ResponseEntity saveImage(MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        try {
            Path directory = Paths.get(imageDirectory);
            if (Files.notExists(directory)) {
                Files.createDirectories(directory);
            }

            Files.write(Paths.get(imageDirectory, file.getOriginalFilename()), file.getBytes());

            Thumbnails.of(ImageIO.read(new ByteArrayInputStream(file.getBytes())))
                    .size(150, 150)
                    .toFile(imageDirectory + "\\preview-" + file.getOriginalFilename());

            return ResponseEntity.ok().build();
        } catch (IOException e) {
            log.error("Error save file, message={}", e);
            return ResponseEntity.badRequest().build();
        }
    }

    @Override
    public ResponseEntity saveSound(MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        try {
            Path directory = Paths.get(soundDirectory);
            if (Files.notExists(directory)) {
                Files.createDirectories(directory);
            }

            String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();

            Files.write(Paths.get(soundDirectory, fileName), file.getBytes());

            return ResponseEntity.ok(new SoundResponse(fileName));
        } catch (IOException e) {
            log.error("Error save file, message={}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @Override
    public ResponseEntity deleteImage(String fileName) {
        File file = new File(imageDirectory + "/" + fileName);
        if (file.delete()) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @Override
    public ResponseEntity deleteSound(String fileName) {
        System.out.println(soundDirectory + "/" + fileName);
        File file = new File(soundDirectory + "/" + fileName);
        if (file.delete()) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().build();
        }
    }
}
