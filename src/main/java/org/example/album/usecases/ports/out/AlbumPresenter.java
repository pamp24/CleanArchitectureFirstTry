package org.example.album.usecases.ports.out;

import org.example.album.adapters.out.presenters.view.resources.CreateAlbumResponseResources;
import org.example.album.usecases.exceptions.AlbumDeleteException;
import org.example.album.usecases.exceptions.AlbumUpdateException;
import org.example.album.usecases.models.CreateAlbumResponseModel;

public interface AlbumPresenter {
    CreateAlbumResponseResources successResponse(CreateAlbumResponseResources createAlbumResponseResources);

    CreateAlbumResponseResources albumDeleteException()throws AlbumDeleteException;

    CreateAlbumResponseResources albumUpdateException()throws AlbumUpdateException;

}
