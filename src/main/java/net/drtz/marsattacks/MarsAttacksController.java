package net.drtz.marsattacks;

import net.drtz.marsattacks.nasa.Client;
import net.drtz.marsattacks.nasa.Photo;
import net.drtz.marsattacks.nasa.PhotosNotFoundException;
import net.drtz.marsattacks.repository.Repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import java.io.*;
import java.time.format.DateTimeFormatter;

@Controller
public class MarsAttacksController {

    private Client nasaClient;
    private Repository repository;
    private ResourceLoader resourceLoader;
    private Logger logger;
    private DateNormalizer dateNormalizer;

    @Autowired
    public MarsAttacksController(ResourceLoader resourceLoader) throws IOException {
        this.resourceLoader = resourceLoader;
        this.nasaClient = new Client(System.getenv("NASA_API_KEY"));
        this.repository = new Repository(resourceLoader);
        this.dateNormalizer = new DateNormalizer(DateTimeFormatter.ISO_LOCAL_DATE);
        this.logger = LoggerFactory.getLogger(MarsAttacksController.class);

        try {
            this.preFetchImages();
        } catch (Exception ex) {
            this.logger.error("failed pre-fetching images", ex);
        }
    }

    @GetMapping("/photos/")
    @ResponseBody
    public ResponseEntity<Resource> getPhoto(@RequestParam(name="date", required=true) String date) throws IOException {
        Resource imageFile;
        try {
            imageFile = this.getImageForDate(date);
        } catch (DateParseException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "invalid date: " + date);
        } catch (PhotosNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "mars photo not found for this date");
        }

        return ResponseEntity
            .ok()
            .header(HttpHeaders.CONTENT_TYPE, "image/jpeg")
            .body(imageFile);
    }

    private Resource getImageForDate(String date) throws DateParseException, PhotosNotFoundException, IOException {
        String normalizedDate = this.dateNormalizer.normalizeDateString(date);
        Photo photo = this.nasaClient.getPhotos(normalizedDate).getFirstPhoto();
        return this.repository.getImage(photo.getImageSource());
    }

    private void preFetchImages() throws IOException {
        InputStream fileStream = new ClassPathResource("preload-dates.txt").getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(fileStream, "UTF-8"));

        String line;
        while ((line = br.readLine()) != null) {
            try {
                this.getImageForDate(line);
            } catch (DateParseException ex) {
                this.logger.error("invalid date read from preload dates file: " + line, ex);
            } catch (PhotosNotFoundException ex) {
                this.logger.warn("no photos found for preloaded dates: " + line, ex);
            }
            this.logger.info("pre-cached image for date: " + line);
        }
    }

}
