package ru.romanov.tonkoslovie.media.sound;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.romanov.tonkoslovie.media.exception.FileUploadException;
import ru.romanov.tonkoslovie.media.storage.StorageService;

import java.io.InputStream;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class SoundServiceImpl implements SoundService {

    private final StorageService storageService;

    @Value("${sounds.folder:sounds/}")
    private String folder;

    @Override
    public InputStream getSound(String filename) {
        return storageService.getFile(folder + filename);
    }

    @Override
    public String saveSound(MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("file is empty");
        }

        try {
            String filename = UUID.randomUUID().toString().replace("-", "") + ".mp3";
            storageService.uploadFile(folder + filename, file.getInputStream());
            return filename;
        } catch (Exception e) {
            log.error("Error save sound. {}: {}", e.getClass().getSimpleName(), e.getMessage());
            throw new FileUploadException(e);
        }
    }

    @Override
    public void deleteSound(String filename) {
        storageService.deleteFile(folder + filename);
    }

}
