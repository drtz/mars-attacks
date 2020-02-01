package net.drtz.marsattacks.nasa;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ApiResponse {
    @JsonProperty("photos")
    private Photo[] photos;

    public boolean hasPhotos() {
        return this.photos != null && this.photos.length >= 1;
    }

    public Photo[] getPhotos() {
        return this.photos;
    }
}
