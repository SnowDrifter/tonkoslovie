package ru.romanov.tonkoslovie.media.sound;

import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

public interface SoundService {

    InputStream getSound(String filename);

    String saveSound(MultipartFile file);

    void deleteSound(String fileName);

}
