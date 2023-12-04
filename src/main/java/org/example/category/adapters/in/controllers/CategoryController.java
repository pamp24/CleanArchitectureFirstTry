package org.example.category.adapters.in.controllers;


import org.example.category.adapters.out.gateways.ds.entities.CategoryEntity;
import org.example.category.usecases.ports.in.CategoryRestUseCase;
import org.example.shared.ApiResponse;
import org.example.auth.usecases.models.PagedResponse;
import org.example.shared.exceptions.UnauthorizedException;
import org.example.shared.models.utils.AppConstants;
import org.example.shared.security.CurrentUser;
import org.example.shared.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {
	@Autowired
	private CategoryRestUseCase categoryService;

	@GetMapping
	public PagedResponse<CategoryEntity> getAllCategories(
			@RequestParam(name = "page", required = false, defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) Integer page,
			@RequestParam(name = "size", required = false, defaultValue = AppConstants.DEFAULT_PAGE_SIZE) Integer size) {
		return categoryService.getAllCategories(page, size);
	}

	@PostMapping
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<CategoryEntity> addCategory(@Valid @RequestBody CategoryEntity category,
			@CurrentUser UserPrincipal currentUser) {

		return categoryService.addCategory(category, currentUser);
	}

	@GetMapping("/{id}")
	public ResponseEntity<CategoryEntity> getCategory(@PathVariable(name = "id") Long id) {
		return categoryService.getCategory(id);
	}

	@PutMapping("/{id}")
	@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
	public ResponseEntity<CategoryEntity> updateCategory(@PathVariable(name = "id") Long id,
			@Valid @RequestBody CategoryEntity category, @CurrentUser UserPrincipal currentUser) throws UnauthorizedException {
		return categoryService.updateCategory(id, category, currentUser);
	}

	@DeleteMapping("/{id}")
	@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
	public ResponseEntity<ApiResponse> deleteCategory(@PathVariable(name = "id") Long id,
													  @CurrentUser UserPrincipal currentUser) throws UnauthorizedException {
		return categoryService.deleteCategory(id, currentUser);
	}

}
