package org.example.user.adapters.in.controllers;

import org.example.album.adapters.out.gateways.ds.entities.AlbumEntity;
import org.example.album.usecases.ports.in.AlbumRestUseCase;
import org.example.auth.usecases.models.PagedResponse;
import org.example.post.adapters.out.gateways.ds.entities.PostEntity;
import org.example.post.usecases.ports.in.PostRestUseCase;
import org.example.shared.ApiResponse;
import org.example.shared.InfoRequest;
import org.example.shared.models.utils.AppConstants;
import org.example.shared.security.CurrentUser;
import org.example.shared.security.UserPrincipal;
import org.example.user.adapters.out.gateways.ds.entities.UserEntity;
import org.example.user.usecases.ports.in.UserRestUseCase;
import org.example.user.adapters.out.gateways.ds.entities.UserIdentityAvailability;
import org.example.user.adapters.out.gateways.ds.entities.UserProfile;
import org.example.user.adapters.out.gateways.ds.entities.UserSummary;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;

import org.springframework.web.bind.annotation.*;


import javax.validation.Valid;

@RestController
@RequestMapping("/api/users")
public class UserController {
	@Autowired
	private UserRestUseCase userRestUseCase;

	@Autowired
	private PostRestUseCase postRestUseCase;

	@Autowired
	private AlbumRestUseCase albumRestUseCase;

	@GetMapping("/me")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<UserSummary> getCurrentUser(@CurrentUser UserPrincipal currentUser) {
		UserSummary userSummary = userRestUseCase.getCurrentUser(currentUser);

		return new ResponseEntity< >(userSummary, HttpStatus.OK);
	}

	@GetMapping("/checkUsernameAvailability")
	public ResponseEntity<UserIdentityAvailability> checkUsernameAvailability(@RequestParam(value = "username") String username) {
		UserIdentityAvailability userIdentityAvailability = userRestUseCase.checkUsernameAvailability(username);

		return new ResponseEntity< >(userIdentityAvailability, HttpStatus.OK);
	}

	@GetMapping("/checkEmailAvailability")
	public ResponseEntity<UserIdentityAvailability> checkEmailAvailability(@RequestParam(value = "email") String email) {
		UserIdentityAvailability userIdentityAvailability = userRestUseCase.checkEmailAvailability(email);
		return new ResponseEntity< >(userIdentityAvailability, HttpStatus.OK);
	}

	@GetMapping("/{username}/profile")
	public ResponseEntity<UserProfile> getUSerProfile(@PathVariable(value = "username") String username) {
		UserProfile userProfile = userRestUseCase.getUserProfile(username);

		return new ResponseEntity< >(userProfile, HttpStatus.OK);
	}

	@GetMapping("/{username}/posts")
	public ResponseEntity<PagedResponse<PostEntity>> getPostsCreatedBy(@PathVariable(value = "username") String username,
																 @RequestParam(value = "page", required = false, defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) Integer page,
																 @RequestParam(value = "size", required = false, defaultValue = AppConstants.DEFAULT_PAGE_SIZE) Integer size) {
		PagedResponse<PostEntity> response = postRestUseCase.getPostsByCreatedBy(username, page, size);

		return new ResponseEntity<  >(response, HttpStatus.OK);
	}

	@GetMapping("/{username}/albums")
	public ResponseEntity<PagedResponse<AlbumEntity>> getUserAlbums(@PathVariable(name = "username") String username,
															  @RequestParam(name = "page", required = false, defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) Integer page,
															  @RequestParam(name = "size", required = false, defaultValue = AppConstants.DEFAULT_PAGE_SIZE) Integer size) {

		PagedResponse<AlbumEntity> response = albumRestUseCase.getUserAlbums(username, page, size);

		return new ResponseEntity<  >(response, HttpStatus.OK);
	}

	@PostMapping
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<UserEntity> addUser(@Valid @RequestBody UserEntity user) {
		UserEntity newUser = userRestUseCase.addUser(user);

		return new ResponseEntity< >(newUser, HttpStatus.CREATED);
	}

	@PutMapping("/{username}")
	@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
	public ResponseEntity<UserEntity> updateUser(@Valid @RequestBody UserEntity newUser,
			@PathVariable(value = "username") String username, @CurrentUser UserPrincipal currentUser) {
		UserEntity updatedUSer = userRestUseCase.updateUser(newUser, username, currentUser);

		return new ResponseEntity< >(updatedUSer, HttpStatus.CREATED);
	}

	@DeleteMapping("/{username}")
	@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
	public ResponseEntity<ApiResponse> deleteUser(@PathVariable(value = "username") String username,
			@CurrentUser UserPrincipal currentUser) {
		ApiResponse apiResponse = userRestUseCase.deleteUser(username, currentUser);

		return new ResponseEntity< >(apiResponse, HttpStatus.OK);
	}

	@PutMapping("/{username}/giveAdmin")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponse> giveAdmin(@PathVariable(name = "username") String username) {
		ApiResponse apiResponse = userRestUseCase.giveAdmin(username);

		return new ResponseEntity< >(apiResponse, HttpStatus.OK);
	}

	@PutMapping("/{username}/takeAdmin")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponse> takeAdmin(@PathVariable(name = "username") String username) {
		ApiResponse apiResponse = userRestUseCase.removeAdmin(username);

		return new ResponseEntity< >(apiResponse, HttpStatus.OK);
	}

	@PutMapping("/setOrUpdateInfo")
	@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
	public ResponseEntity<UserProfile> setAddress(@CurrentUser UserPrincipal currentUser,
			@Valid @RequestBody InfoRequest infoRequest) {
		UserProfile userProfile = userRestUseCase.setOrUpdateInfo(currentUser, infoRequest);

		return new ResponseEntity< >(userProfile, HttpStatus.OK);
	}

}
