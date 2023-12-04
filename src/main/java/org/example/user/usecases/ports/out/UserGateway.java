package org.example.user.usecases.ports.out;

import org.example.post.usecases.dtos.CreatePostDto;
import org.example.post.usecases.dtos.PostDto;
import org.example.user.adapters.out.gateways.ds.entities.UserEntity;
import org.example.user.usecases.dtos.CreateUserDto;
import org.example.user.usecases.dtos.UserDto;

import java.util.Optional;

public interface UserGateway {
    boolean existsByName(String Name);
    Optional<UserEntity> getById(long id);
    UserDto getByName(String name);
    void save(CreateUserDto requestModel);
}
