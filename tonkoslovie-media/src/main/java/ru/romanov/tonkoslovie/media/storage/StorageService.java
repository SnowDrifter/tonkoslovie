package ru.romanov.tonkoslovie.media.storage;

import java.io.InputStream;


public interface StorageService {

    void uploadFile(String filename, InputStream inputStream);

    InputStream getFile(String filename);

    void deleteFilesByFolder(String folder);

    void deleteFile(String filename);

}
