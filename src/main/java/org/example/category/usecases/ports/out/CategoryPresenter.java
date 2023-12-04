package org.example.category.usecases.ports.out;



import org.example.category.adapters.out.presenters.view.resources.CreateCategoryResponseResources;
import org.example.category.usecases.exceptions.CategoryDeleteException;
import org.example.category.usecases.exceptions.CategoryUpdateException;



public interface CategoryPresenter{

    CreateCategoryResponseResources successResponse(CreateCategoryResponseResources createCategoryResponseResources);

    CreateCategoryResponseResources categoryDeleteException()throws CategoryDeleteException;

    CreateCategoryResponseResources categoryUpdateException()throws CategoryUpdateException;

}
