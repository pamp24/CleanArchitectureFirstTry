package org.example.todo.usecases.ports.in;


import org.example.shared.ApiResponse;
import org.example.auth.usecases.models.PagedResponse;
import org.example.shared.security.UserPrincipal;
import org.example.todo.adapters.out.gateways.ds.entities.TodoEntity;

public interface TodoRestUseCase {

	TodoEntity completeTodo(Long id, UserPrincipal currentUser);

	TodoEntity unCompleteTodo(Long id, UserPrincipal currentUser);

	PagedResponse<TodoEntity> getAllTodos(UserPrincipal currentUser, int page, int size);

	TodoEntity addTodo(TodoEntity todo, UserPrincipal currentUser);

	TodoEntity getTodo(Long id, UserPrincipal currentUser);

	TodoEntity updateTodo(Long id, TodoEntity newTodo, UserPrincipal currentUser);

	ApiResponse deleteTodo(Long id, UserPrincipal currentUser);

}
