package org.example.tag.usecases.ports.out;


import org.example.tag.adapters.out.gateways.ds.entities.TagEntity;
import org.example.tag.usecases.dtos.CreateTagDto;
import org.example.tag.usecases.dtos.TagDto;

import java.util.Optional;

public interface TagGateway {
    boolean existsByName(String Name);
    Optional<TagEntity> getById(long id);
    TagDto getByName(String name);
    void save(CreateTagDto requestModel);
}
