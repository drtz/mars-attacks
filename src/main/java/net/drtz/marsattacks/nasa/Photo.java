package net.drtz.marsattacks.nasa;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Photo {
    @JsonProperty("img_src")
    private String imageSource;

    public String getImageSource() {
        return imageSource;
    }

    public String toString() {
        return this.imageSource;
    }
}
