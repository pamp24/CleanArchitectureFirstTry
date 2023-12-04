package org.example.category.usecases.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import javax.validation.constraints.NotBlank;
@Setter
@Getter
@AllArgsConstructor
public class CreateCategoryRequestModel {

    @NotBlank
    private String name;

}
