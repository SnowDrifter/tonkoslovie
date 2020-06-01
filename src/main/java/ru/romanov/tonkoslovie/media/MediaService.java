package ru.romanov.tonkoslovie.media;


import org.springframework.web.multipart.MultipartFile;

public interface MediaService {

    String saveImage(MultipartFile file);

    String saveSound(MultipartFile file);

    void deleteImage(String fileName);

    void deleteSound(String fileName);

}
