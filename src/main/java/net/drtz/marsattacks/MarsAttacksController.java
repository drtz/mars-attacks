package net.drtz.marsattacks;

import net.drtz.marsattacks.nasa.ApiResponse;
import net.drtz.marsattacks.nasa.Client;
import net.drtz.marsattacks.nasa.Photo;
import net.drtz.marsattacks.repository.Repository;

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

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.List;
import java.io.IOException;

@Controller
public class MarsAttacksController {

    private Client nasaClient;
    private Repository repository;

    private static List<DateTimeFormatter> dateFormatters = List.of(
            DateTimeFormatter.ofPattern("MM/dd/uu").withResolverStyle(ResolverStyle.STRICT),
            DateTimeFormatter.ofPattern("MMMM d, u").withResolverStyle(ResolverStyle.STRICT),
            DateTimeFormatter.ofPattern("MMM-d-uuuu").withResolverStyle(ResolverStyle.STRICT),
            DateTimeFormatter.ofPattern("u-M-d").withResolverStyle(ResolverStyle.STRICT)
    );

    @Autowired
    public MarsAttacksController(ResourceLoader resourceLoader) throws IOException {
        this.nasaClient = new Client(System.getenv("NASA_API_KEY"));
        this.repository = new Repository(resourceLoader);
    }

    @GetMapping("/photos/")
    @ResponseBody
    public ResponseEntity<Resource> getPhoto(@RequestParam(name="date", required=true) String date) throws IOException {
        ApiResponse response = this.nasaClient.getPhotos(normalizeDateString(date));

        if (!response.hasPhotos()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "mars photo not found");
        }
        Photo photo = response.getPhotos()[0];

        Resource file = this.repository.getImage(photo.getImageSource());

        return ResponseEntity
            .ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "filename=\"" + file.getFilename() + "\"")
            .header(HttpHeaders.CONTENT_TYPE, "image/jpeg")
            .body(file);
    }

    public static String normalizeDateString(String dateString) {
        final DateTimeFormatter normalizedFormatter = DateTimeFormatter.ISO_LOCAL_DATE;
        for (DateTimeFormatter dateFormatter : dateFormatters) {
            try {
                LocalDate date = LocalDate.parse(dateString, dateFormatter);
                return normalizedFormatter.format(date);
            } catch (DateTimeParseException e) {}
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "invalid date: " + dateString);
    }

}
