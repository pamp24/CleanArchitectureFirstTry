package org.example.photo.usecases.ports.out;

import org.example.photo.adapters.out.gateways.ds.entities.PhotoEntity;
import org.example.photo.usecases.dtos.CreatePhotoDto;
import org.example.photo.usecases.dtos.PhotoDto;

import java.util.Optional;

public interface PhotoGateway {
    boolean existsByName(String Name);
    Optional<PhotoEntity> getById(long id);
    PhotoDto getByName(String name);
    void save(CreatePhotoDto requestModel);
}
