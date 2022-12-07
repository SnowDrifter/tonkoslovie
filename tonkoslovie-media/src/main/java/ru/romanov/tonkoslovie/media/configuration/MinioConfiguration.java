package ru.romanov.tonkoslovie.media.configuration;

import io.minio.MinioClient;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties("minio")
public class MinioConfiguration {

    private String url;
    private String accessKey;
    private String secretKey;

    @Bean
    public MinioClient minioClient() {
        return new MinioClient.Builder()
                .credentials(accessKey, secretKey)
                .endpoint(url)
                .build();
    }

}
