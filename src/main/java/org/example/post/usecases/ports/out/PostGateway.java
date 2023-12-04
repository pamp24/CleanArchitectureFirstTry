package org.example.post.usecases.ports.out;


import org.example.post.adapters.out.gateways.ds.entities.PostEntity;
import org.example.post.usecases.dtos.CreatePostDto;
import org.example.post.usecases.dtos.PostDto;

import java.util.Optional;

public interface PostGateway {
    boolean existsByName(String Name);
    Optional<PostEntity> getById(long id);
    PostDto getByName(String name);
    void save(CreatePostDto requestModel);
}
