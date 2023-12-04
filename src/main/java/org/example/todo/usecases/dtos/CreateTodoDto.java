package org.example.todo.usecases.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class CreateTodoDto {
    private long id;
    private String title;
    private boolean completed;
}
