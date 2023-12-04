package org.example.category.adapters.out.presenters.view;


import org.example.category.adapters.out.presenters.view.resources.CreateCategoryResponseResources;
import org.example.category.usecases.exceptions.CategoryDeleteException;
import org.example.category.usecases.exceptions.CategoryUpdateException;
import org.example.category.usecases.models.CategoryResponse;
import org.example.category.usecases.ports.out.CategoryPresenter;
import org.springframework.http.HttpStatus;
import sun.tools.jconsole.Messages;


public class CategoryViewPresenter implements CategoryPresenter {

    @Override
    public CreateCategoryResponseResources categoryUpdateException(){
        throw new CategoryUpdateException(HttpStatus.UNAUTHORIZED, Messages.MESSAGE);
    }

    @Override
    public CreateCategoryResponseResources successResponse(CreateCategoryResponseResources createCategoryResponseResources) {
        return CategoryResponse.builder().build();
    }

    @Override
    public CreateCategoryResponseResources categoryDeleteException(){
        throw new CategoryDeleteException(HttpStatus.UNAUTHORIZED, Messages.MESSAGE);
    }
}
