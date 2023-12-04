package org.example.category.adapters.out.gateways.ds;

import org.example.auth.usecases.models.PagedResponse;
import org.example.category.adapters.out.gateways.ds.entities.CategoryEntity;
import org.example.category.adapters.out.gateways.ds.repositories.CategoryRepository;
import org.example.category.usecases.dtos.CategoryDto;
import org.example.category.usecases.dtos.CreateCategoryDto;
import org.example.category.usecases.ports.out.CategoryGateway;
import org.example.role.domains.RoleName;
import org.example.shared.ApiResponse;
import org.example.shared.exceptions.ResourceNotFoundException;
import org.example.shared.exceptions.UnauthorizedException;
import org.example.shared.models.utils.AppUtils;
import org.example.shared.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

public class CategoryDsGateway implements CategoryGateway {
    @Autowired
    private CategoryRepository categoryRepository;
    CategoryDsGateway(CategoryRepository repository){
        this.categoryRepository = repository;
    }

    @Override
    public boolean existsByName(String Name) {
        return categoryRepository.existsById(name);
    }

    @Override
    public Optional<CategoryEntity> getById(long id) {
        return categoryRepository.findById(id);
    }

    @Override
    public CategoryDto getByName(String name) {
        CategoryEntity categoryEntity =
                categoryRepository.findByCreatedBy(id).orElseThrow(ResourceNotFoundException::new);
        return CategoryDto.builder()
                .name(categoryEntity.getName())
                .build();
    }

    @Override
    public void save(CreateCategoryDto requestModel) {

    }
    @Override
    public PagedResponse<CategoryEntity> getAllCategories(int page, int size) {
        AppUtils.validatePageNumberAndSize(page, size);

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdAt");

        Page<CategoryEntity> categories = categoryRepository.findAll(pageable);

        List<CategoryEntity> content = categories.getNumberOfElements() == 0 ? Collections.emptyList() : categories.getContent();

        return new PagedResponse<>(content, categories.getNumber(), categories.getSize(), categories.getTotalElements(),
                categories.getTotalPages(), categories.isLast());
    }
    @Override
    public ResponseEntity<CategoryEntity> getCategory(Long id) {
        CategoryEntity category = categoryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Category", "id", id));
        return new ResponseEntity<>(category, HttpStatus.OK);
    }
    @Override
    public ResponseEntity<CategoryEntity> addCategory(CategoryEntity category, UserPrincipal currentUser) {
        CategoryEntity newCategory = categoryRepository.save(category);
        return new ResponseEntity<>(newCategory, HttpStatus.CREATED);
    }
    @Override
    public ResponseEntity<CategoryEntity> updateCategory(Long id, CategoryEntity newCategory, UserPrincipal currentUser) {
        CategoryEntity category = categoryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Category", "id", id));
        if (category.getCreatedBy().equals(currentUser.getId()) || currentUser.getAuthorities()
                .contains(new SimpleGrantedAuthority(RoleName.ROLE_ADMIN.toString()))) {
            category.setName(newCategory.getName());
            CategoryEntity updatedCategory = categoryRepository.save(category);
            return new ResponseEntity<>(updatedCategory, HttpStatus.OK);
        }

        throw new UnauthorizedException("You don't have permission to edit this category");
    }
    @Override
    public ResponseEntity<ApiResponse> deleteCategory(Long id, UserPrincipal currentUser) {
        CategoryEntity category = categoryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("org/example/category", "id", id));
        if (category.getCreatedBy().equals(currentUser.getId()) || currentUser.getAuthorities()
                .contains(new SimpleGrantedAuthority(RoleName.ROLE_ADMIN.toString()))) {
            categoryRepository.deleteById(id);
            return new ResponseEntity<>(new ApiResponse(Boolean.TRUE, "You successfully deleted category"), HttpStatus.OK);
        }
        throw new UnauthorizedException("You don't have permission to delete this category");
    }
}
