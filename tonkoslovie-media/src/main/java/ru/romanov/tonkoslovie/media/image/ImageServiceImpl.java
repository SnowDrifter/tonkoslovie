package ru.romanov.tonkoslovie.media.image;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.romanov.tonkoslovie.media.dto.SizeData;
import ru.romanov.tonkoslovie.media.exception.FileUploadException;
import ru.romanov.tonkoslovie.media.exception.ImageSizeException;
import ru.romanov.tonkoslovie.media.storage.StorageService;
import ru.romanov.tonkoslovie.media.utils.MediaUtils;

import javax.imageio.ImageIO;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Slf4j
@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

    private final StorageService storageService;

    @Value("${images.folder:images/}")
    private String folder;
    @Value("${images.available-sizes}")
    private List<String> availableSizes = new ArrayList<>();

    @Override
    public InputStream getImage(String filePrefix, String size) {
        try {
            return storageService.getFile(folder + filePrefix + "/" + size);
        } catch (Exception ignore) {
            return tryGetResizeImage(filePrefix, size);
        }
    }

    @SneakyThrows
    private InputStream tryGetResizeImage(String filePrefix, String size) {
        if (!availableSizes.contains(size)) {
            throw new ImageSizeException("Size '" + size + "' not available");
        }

        SizeData sizeData = MediaUtils.parseSize(size);

        InputStream image = storageService.getFile(folder + filePrefix + "/original.jpg");

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Thumbnails.of(ImageIO.read(image))
                .size(sizeData.getHeight(), sizeData.getWidth())
                .outputFormat(sizeData.getFormat())
                .toOutputStream(outputStream);

        byte[] imageBytes = outputStream.toByteArray();

        storageService.uploadFile(folder + filePrefix + "/" + size, new ByteArrayInputStream(imageBytes));

        return new ByteArrayInputStream(imageBytes);
    }

    @Override
    public String saveImage(MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("file is empty");
        }

        try {
            String filePrefix = UUID.randomUUID().toString().replace("-", "");
            storageService.uploadFile(folder + filePrefix + "/original.jpg", file.getInputStream());
            return filePrefix;
        } catch (Exception e) {
            log.error("Error save image. {}: {}", e.getClass().getSimpleName(), e.getMessage());
            throw new FileUploadException(e);
        }
    }

    @Override
    public void deleteImage(String filePrefix) {
        storageService.deleteFilesByFolder(folder + filePrefix);
    }
}
