package org.example.photo.usecases.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@Builder
public class PhotoDto {
    private long id;
    private String title;
    private String url;
    private String thumbnailUrl;
}
