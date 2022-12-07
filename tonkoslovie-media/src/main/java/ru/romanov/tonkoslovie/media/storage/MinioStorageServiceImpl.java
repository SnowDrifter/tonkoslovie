package ru.romanov.tonkoslovie.media.storage;

import io.minio.*;
import io.minio.messages.DeleteError;
import io.minio.messages.DeleteObject;
import io.minio.messages.Item;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.romanov.tonkoslovie.media.exception.FileDeleteException;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MinioStorageServiceImpl implements StorageService {

    @Value("${minio.bucket}")
    private String bucket;

    private final MinioClient minioClient;

    @Override
    @SneakyThrows
    public void uploadFile(String filename, InputStream stream) {
        minioClient.putObject(PutObjectArgs.builder()
                .bucket(bucket)
                .object(filename)
                .stream(stream, stream.available(), -1)
                .build());
    }

    @SneakyThrows
    public InputStream getFile(String filename) {
        return minioClient.getObject(GetObjectArgs.builder()
                .bucket(bucket)
                .object(filename)
                .build());
    }

    @Override
    @SneakyThrows
    public void deleteFilesByFolder(String folder) {
        Iterable<Result<Item>> results = minioClient.listObjects(ListObjectsArgs.builder()
                .bucket(bucket)
                .prefix(folder)
                .recursive(true)
                .build());

        List<DeleteObject> deleteObjects = new ArrayList<>();
        for (Result<Item> result : results) {
            deleteObjects.add(new DeleteObject(result.get().objectName()));
        }

        Iterable<Result<DeleteError>> errors = minioClient.removeObjects(RemoveObjectsArgs.builder()
                .bucket(bucket)
                .objects(deleteObjects)
                .build());

        if (errors.iterator().hasNext()) {
            throw new FileDeleteException("Can't delete files with prefix " + folder);
        }
    }

    @Override
    @SneakyThrows
    public void deleteFile(String filename) {
        minioClient.removeObject(RemoveObjectArgs.builder()
                .bucket(bucket)
                .object(filename)
                .build());
    }

}
