package org.example.album.usecases.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
@Setter
@Getter
@AllArgsConstructor
@Builder
public class CreateAlbumResponseModel {
        private String title;
}
