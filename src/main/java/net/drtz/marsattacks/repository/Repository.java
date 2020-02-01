package net.drtz.marsattacks.repository;

import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import com.google.common.hash.Hashing;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;

public class Repository {

    private Path cacheBase;
    private ResourceLoader resourceLoader;

    public Repository(ResourceLoader resourceLoader) throws IOException {
        this.cacheBase = Paths.get("image-cache");
        this.resourceLoader = resourceLoader;
        this.initializeCache();
    }

    public Resource getImage(String url) throws IOException {
        Path cachePath = this.getCachePath(url);
        if (!Files.exists(cachePath)) {
            this.cacheRemoteImage(url, cachePath);
        }
        return this.resourceLoader.getResource("file:" + cachePath.toString());
    }

    private Path getCachePath(String url) {
        String sha256hex = Hashing.sha256()
                .hashString(url, StandardCharsets.UTF_8)
                .toString();
        return Paths.get(this.cacheBase.toString(), sha256hex);
    }

    private void cacheRemoteImage(String url, Path cachePath) throws IOException {
        List<HttpMessageConverter<?>> converters = List.of(new ByteArrayHttpMessageConverter());
        RestTemplate imageRestTemplate = new RestTemplate(converters);
        byte[] imageBytes = imageRestTemplate.getForObject(url, byte[].class);

        Files.write(cachePath, imageBytes, StandardOpenOption.CREATE);
    }

    private void initializeCache() throws IOException {
        if (!Files.exists(this.cacheBase)) {
            Files.createDirectory(this.cacheBase);
        }
    }

}
