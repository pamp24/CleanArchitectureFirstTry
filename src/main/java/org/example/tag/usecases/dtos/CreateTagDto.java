package org.example.tag.usecases.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class CreateTagDto {
    private long id;
    private String name;

}
