package ru.romanov.tonkoslovie.media;


import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface MediaService {

    ResponseEntity saveImage(MultipartFile file);

    ResponseEntity saveSound(MultipartFile file);

    ResponseEntity deleteImage(String fileName);

    ResponseEntity deleteSound(String fileName);

}
