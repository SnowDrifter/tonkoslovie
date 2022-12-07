package ru.romanov.tonkoslovie.media.image;

import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

public interface ImageService {

    InputStream getImage(String filename, String size);

    String saveImage(MultipartFile file);

    void deleteImage(String fileName);

}
