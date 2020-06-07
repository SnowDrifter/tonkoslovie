package ru.romanov.tonkoslovie.media;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.romanov.tonkoslovie.exception.FileUploadException;
import ru.romanov.tonkoslovie.utils.MediaUtils;

import javax.annotation.PostConstruct;
import javax.imageio.ImageIO;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@Slf4j
@Service
public class MediaServiceImpl implements MediaService {

    @Value("${media.imageDirectory}")
    private String imageDirectory;
    @Value("${media.soundDirectory}")
    private String soundDirectory;

    @PostConstruct
    public void init() {
        checkDirectory(imageDirectory);
        checkDirectory(soundDirectory);
    }

    @Override
    public String saveImage(MultipartFile file) {
        try {
            String fileName = UUID.randomUUID().toString().substring(0, 8) + "_" + file.getOriginalFilename();

            Files.write(Paths.get(imageDirectory, fileName), file.getBytes());

            for (String resize : MediaUtils.getAvailableSizes()) {
                int height = Integer.parseInt(resize.split("_")[0]);
                int weight = Integer.parseInt(resize.split("_")[1]);

                Thumbnails.of(ImageIO.read(new ByteArrayInputStream(file.getBytes())))
                        .size(height, weight)
                        .toFile(imageDirectory + "/" + resize + "-" + fileName);
            }

            return fileName;
        } catch (Exception e) {
            log.error("Error save image. {}: {}", e.getClass().getSimpleName(), e.getMessage());
            throw new FileUploadException(e);
        }
    }

    @Override
    public String saveSound(MultipartFile file) {
        try {
            String fileName = UUID.randomUUID().toString().substring(0, 8) + "_" + file.getOriginalFilename();

            Files.write(Paths.get(soundDirectory, fileName), file.getBytes());
            return fileName;
        } catch (IOException e) {
            log.error("Error save sound. {}: {}", e.getClass().getSimpleName(), e.getMessage());
            throw new FileUploadException(e);
        }
    }

    @Override
    public void deleteImage(String fileName) {
        deleteFile(imageDirectory, fileName);
        for (String resize : MediaUtils.getAvailableSizes()) {
            deleteFile(imageDirectory, resize + "-" + fileName);
        }
    }

    @Override
    public void deleteSound(String fileName) {
        deleteFile(soundDirectory, fileName);
    }

    @SneakyThrows
    private void deleteFile(String directory, String name) {
        Files.deleteIfExists(Paths.get(directory, name));
    }

    @SneakyThrows
    private void checkDirectory(String directory) {
        if (Files.notExists(Paths.get(directory))) {
            Files.createDirectory(Paths.get(directory));
        }
    }
}
