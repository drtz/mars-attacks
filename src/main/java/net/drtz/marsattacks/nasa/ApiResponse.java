package net.drtz.marsattacks.nasa;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ApiResponse {

    @JsonProperty("photos")
    private Photo[] photos;

    private boolean hasPhotos() {
        return this.photos != null && this.photos.length >= 1;
    }

    public Photo getFirstPhoto() throws PhotosNotFoundException {
        if (!this.hasPhotos()) {
            throw new PhotosNotFoundException("this response doesn't contain any photos");
        }
        return this.photos[0];
    }

}
