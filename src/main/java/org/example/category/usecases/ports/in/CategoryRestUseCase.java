package org.example.category.usecases.ports.in;


import org.example.auth.usecases.models.PagedResponse;

import org.example.category.adapters.out.gateways.ds.entities.CategoryEntity;
import org.example.category.adapters.out.presenters.view.resources.CreateCategoryResponseResources;
import org.example.category.usecases.models.CreateCategoryRequestModel;
import org.example.shared.ApiResponse;
import org.example.shared.exceptions.UnauthorizedException;
import org.example.shared.security.UserPrincipal;
import org.springframework.http.ResponseEntity;

public interface CategoryRestUseCase {

	PagedResponse<CategoryEntity> getAllCategories(int page, int size);

	ResponseEntity<CategoryEntity> getCategory(Long id);

	ResponseEntity<CategoryEntity> addCategory(CategoryEntity category, UserPrincipal currentUser);

	ResponseEntity<CategoryEntity> updateCategory(Long id, CategoryEntity newCategory, UserPrincipal currentUser)
			throws UnauthorizedException;

	ResponseEntity<ApiResponse> deleteCategory(Long id, UserPrincipal currentUser)
			throws UnauthorizedException;

	CreateCategoryResponseResources create(CreateCategoryRequestModel createCategoryRequestModel);
}
