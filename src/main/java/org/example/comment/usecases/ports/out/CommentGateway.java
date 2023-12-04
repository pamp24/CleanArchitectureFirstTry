package org.example.comment.usecases.ports.out;


import org.example.comment.adapters.out.gateways.ds.entities.CommentEntity;
import org.example.comment.usecases.dtos.CommentDto;
import org.example.comment.usecases.dtos.CreateCommentDto;

import java.util.Optional;

public interface CommentGateway {
    boolean existsByName(String Name);
    Optional<CommentEntity> getById(long id);
    CommentDto getByName(String name);
    void save(CreateCommentDto requestModel);
}
