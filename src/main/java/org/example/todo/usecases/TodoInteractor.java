package org.example.todo.usecases;

import org.example.auth.usecases.models.PagedResponse;
import org.example.shared.exceptions.BadRequestException;
import org.example.shared.exceptions.ResourceNotFoundException;
import org.example.shared.exceptions.UnauthorizedException;
import org.example.shared.models.utils.AppConstants;
import org.example.shared.ApiResponse;
import org.example.shared.security.UserPrincipal;
import org.example.todo.adapters.out.gateways.ds.entities.TodoEntity;
import org.example.todo.usecases.ports.in.TodoRestUseCase;
import org.example.todo.adapters.out.gateways.ds.repositories.TodoRepository;


import org.example.user.adapters.out.gateways.ds.entities.UserEntity;
import org.example.user.adapters.out.gateways.ds.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

import static org.example.shared.models.utils.AppConstants.*;


@Service
public class TodoInteractor implements TodoRestUseCase {

	@Autowired
	private TodoRepository todoRepository;

	@Autowired
	private UserRepository userRepository;

	@Override
	public TodoEntity completeTodo(Long id, UserPrincipal currentUser) {
		TodoEntity todo = todoRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(TODO, ID, id));

		UserEntity user = userRepository.getUser(currentUser);

		if (todo.getUser().getId().equals(user.getId())) {
			todo.setCompleted(Boolean.TRUE);
			return todoRepository.save(todo);
		}

		ApiResponse apiResponse = new ApiResponse(Boolean.FALSE, YOU_DON_T_HAVE_PERMISSION_TO_MAKE_THIS_OPERATION);

		throw new UnauthorizedException(apiResponse);
	}

	@Override
	public TodoEntity unCompleteTodo(Long id, UserPrincipal currentUser) {
		TodoEntity todo = todoRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(TODO, ID, id));
		UserEntity user = userRepository.getUser(currentUser);
		if (todo.getUser().getId().equals(user.getId())) {
			todo.setCompleted(Boolean.FALSE);
			return todoRepository.save(todo);
		}

		ApiResponse apiResponse = new ApiResponse(Boolean.FALSE, YOU_DON_T_HAVE_PERMISSION_TO_MAKE_THIS_OPERATION);

		throw new UnauthorizedException(apiResponse);
	}

	@Override
	public PagedResponse<TodoEntity> getAllTodos(UserPrincipal currentUser, int page, int size) {
		validatePageNumberAndSize(page, size);
		Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, CREATED_AT);

		Page<TodoEntity> todos = todoRepository.findByCreatedBy(currentUser.getId(), pageable);

		List<TodoEntity> content = todos.getNumberOfElements() == 0 ? Collections.emptyList() : todos.getContent();

		return new PagedResponse<>(content, todos.getNumber(), todos.getSize(), todos.getTotalElements(),
				todos.getTotalPages(), todos.isLast());
	}

	@Override
	public TodoEntity addTodo(TodoEntity todo, UserPrincipal currentUser) {
		UserEntity user = userRepository.getUser(currentUser);
		todo.setUser(user);
		return todoRepository.save(todo);
	}

	@Override
	public TodoEntity getTodo(Long id, UserPrincipal currentUser) {
		UserEntity user = userRepository.getUser(currentUser);
		TodoEntity todo = todoRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(TODO, ID, id));

		if (todo.getUser().getId().equals(user.getId())) {
			return todo;
		}

		ApiResponse apiResponse = new ApiResponse(Boolean.FALSE, YOU_DON_T_HAVE_PERMISSION_TO_MAKE_THIS_OPERATION);

		throw new UnauthorizedException(apiResponse);
	}

	@Override
	public TodoEntity updateTodo(Long id, TodoEntity newTodo, UserPrincipal currentUser) {
		UserEntity user = userRepository.getUser(currentUser);
		TodoEntity todo = todoRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(TODO, ID, id));
		if (todo.getUser().getId().equals(user.getId())) {
			todo.setTitle(newTodo.getTitle());
			todo.setCompleted(newTodo.getCompleted());
			return todoRepository.save(todo);
		}

		ApiResponse apiResponse = new ApiResponse(Boolean.FALSE, YOU_DON_T_HAVE_PERMISSION_TO_MAKE_THIS_OPERATION);

		throw new UnauthorizedException(apiResponse);
	}

	@Override
	public ApiResponse deleteTodo(Long id, UserPrincipal currentUser) {
		UserEntity user = userRepository.getUser(currentUser);
		TodoEntity todo = todoRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(TODO, ID, id));

		if (todo.getUser().getId().equals(user.getId())) {
			todoRepository.deleteById(id);
			return new ApiResponse(Boolean.TRUE, "You successfully deleted todo");
		}

		ApiResponse apiResponse = new ApiResponse(Boolean.FALSE, YOU_DON_T_HAVE_PERMISSION_TO_MAKE_THIS_OPERATION);

		throw new UnauthorizedException(apiResponse);
	}

	private void validatePageNumberAndSize(int page, int size) {
		if (page < 0) {
			throw new BadRequestException("Page number cannot be less than zero.");
		}

		if (size < 0) {
			throw new BadRequestException("Size number cannot be less than zero.");
		}

		if (size > AppConstants.MAX_PAGE_SIZE) {
			throw new BadRequestException("Page size must not be greater than " + AppConstants.MAX_PAGE_SIZE);
		}
	}
}
