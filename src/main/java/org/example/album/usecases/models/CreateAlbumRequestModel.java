package org.example.album.usecases.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Setter
@Getter
@AllArgsConstructor
public class CreateAlbumRequestModel {
    @NotBlank
    private String title;
}
