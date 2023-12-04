package org.example.todo.adapters.in.controllers;

import org.example.todo.adapters.out.gateways.ds.entities.TodoEntity;
import org.example.shared.ApiResponse;
import org.example.auth.usecases.models.PagedResponse;
import org.example.shared.models.utils.AppConstants;
import org.example.shared.security.CurrentUser;
import org.example.shared.security.UserPrincipal;
import org.example.todo.usecases.ports.in.TodoRestUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/todos")
public class TodoController {

	@Autowired
	private TodoRestUseCase todoService;

	@GetMapping
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<PagedResponse<TodoEntity>> getAllTodos(
			@CurrentUser UserPrincipal currentUser,
			@RequestParam(value = "page", required = false, defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) Integer page,
			@RequestParam(name = "size", required = false, defaultValue = AppConstants.DEFAULT_PAGE_SIZE) Integer size) {

		PagedResponse<TodoEntity> response = todoService.getAllTodos(currentUser, page, size);

		return new ResponseEntity< >(response, HttpStatus.OK);
	}

	@PostMapping
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<TodoEntity> addTodo(@Valid @RequestBody TodoEntity todo, @CurrentUser UserPrincipal currentUser) {
		TodoEntity newTodo = todoService.addTodo(todo, currentUser);

		return new ResponseEntity< >(newTodo, HttpStatus.CREATED);
	}

	@GetMapping("/{id}")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<TodoEntity> getTodo(@PathVariable(value = "id") Long id, @CurrentUser UserPrincipal currentUser) {
		TodoEntity todo = todoService.getTodo(id, currentUser);

		return new ResponseEntity< >(todo, HttpStatus.OK);
	}

	@PutMapping("/{id}")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<TodoEntity> updateTodo(@PathVariable(value = "id") Long id, @Valid @RequestBody TodoEntity newTodo,
			@CurrentUser UserPrincipal currentUser) {
		TodoEntity updatedTodo = todoService.updateTodo(id, newTodo, currentUser);

		return new ResponseEntity< >(updatedTodo, HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<ApiResponse> deleteTodo(@PathVariable(value = "id") Long id, @CurrentUser UserPrincipal currentUser) {
		ApiResponse apiResponse = todoService.deleteTodo(id, currentUser);

		return new ResponseEntity<>(apiResponse, HttpStatus.OK);
	}

	@PutMapping("/{id}/complete")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<TodoEntity> completeTodo(@PathVariable(value = "id") Long id, @CurrentUser UserPrincipal currentUser) {

		TodoEntity todo = todoService.completeTodo(id, currentUser);

		return new ResponseEntity< >(todo, HttpStatus.OK);
	}

	@PutMapping("/{id}/unComplete")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<TodoEntity> unCompleteTodo(@PathVariable(value = "id") Long id, @CurrentUser UserPrincipal currentUser) {

		TodoEntity todo = todoService.unCompleteTodo(id, currentUser);

		return new ResponseEntity< >(todo, HttpStatus.OK);
	}
}
