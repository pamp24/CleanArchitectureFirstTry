package org.example.photo.usecases.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor

public class CreatePhotoDto {
    private long id;
    private String title;
    private String url;
    private String thumbnailUrl;
}
