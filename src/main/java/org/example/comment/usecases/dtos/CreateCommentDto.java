package org.example.comment.usecases.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class CreateCommentDto {
    private long id;
    private String name;
    private String email;
    private String body;

}