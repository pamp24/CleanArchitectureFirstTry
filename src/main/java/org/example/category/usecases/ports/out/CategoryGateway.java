package org.example.category.usecases.ports.out;


import org.example.auth.usecases.models.PagedResponse;
import org.example.category.adapters.out.gateways.ds.entities.CategoryEntity;
import org.example.category.usecases.dtos.CategoryDto;
import org.example.category.usecases.dtos.CreateCategoryDto;
import org.example.shared.ApiResponse;
import org.example.shared.security.UserPrincipal;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

public interface CategoryGateway {
    boolean existsByName(String Name);
    Optional<CategoryEntity> getById(long id);
    CategoryDto getByName(String name);
    void save(CreateCategoryDto requestModel);

    PagedResponse<CategoryEntity> getAllCategories(int page, int size);

    ResponseEntity<CategoryEntity> getCategory(Long id);

    ResponseEntity<CategoryEntity> addCategory(CategoryEntity category, UserPrincipal currentUser);

    ResponseEntity<CategoryEntity> updateCategory(Long id, CategoryEntity newCategory, UserPrincipal currentUser);

    ResponseEntity<ApiResponse> deleteCategory(Long id, UserPrincipal currentUser);
}
