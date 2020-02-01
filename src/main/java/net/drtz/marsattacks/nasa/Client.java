package net.drtz.marsattacks.nasa;

import org.springframework.web.client.RestTemplate;

public class Client {

    private String apiKey;

    public Client(String apiKey) {
        this.apiKey = apiKey;
    }

    public ApiResponse getPhotos(String date) {
        // RestTemplate requests are blocking. For a production application I would use a
        // Flux WebRequest

        // example URL:
        // https://api.nasa.gov/mars-photos/api/v1/rovers/curiosity/photos?api_key=&earth_date=2015-6-3
        final String baseUrl = "https://api.nasa.gov/mars-photos/api/v1/rovers/curiosity/photos?api_key=" + this.apiKey;
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForObject(baseUrl + "&earth_date=" + date, ApiResponse.class);
    }

}
