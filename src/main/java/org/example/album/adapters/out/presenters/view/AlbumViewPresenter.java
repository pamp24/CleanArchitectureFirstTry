package org.example.album.adapters.out.presenters.view;

import org.example.album.adapters.out.presenters.view.resources.CreateAlbumResponseResources;
import org.example.album.usecases.exceptions.AlbumDeleteException;
import org.example.album.usecases.exceptions.AlbumUpdateException;
import org.example.album.usecases.ports.out.AlbumPresenter;
import org.springframework.http.HttpStatus;
import sun.tools.jconsole.Messages;


public class AlbumViewPresenter implements AlbumPresenter {

    @Override
    public CreateAlbumResponseResources successResponse(CreateAlbumResponseResources createAlbumResponseResources) {
        return CreateAlbumResponseResources.builder().build();
    }
    @Override
    public CreateAlbumResponseResources albumDeleteException(){
        throw new AlbumDeleteException(HttpStatus.UNAUTHORIZED, Messages.MESSAGE);
    }
    @Override
    public CreateAlbumResponseResources albumUpdateException(){
        throw new AlbumUpdateException(HttpStatus.UNAUTHORIZED, Messages.MESSAGE);
    }
}
