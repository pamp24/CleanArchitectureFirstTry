package org.example.user.usecases;


import org.example.post.adapters.out.gateways.ds.repositories.PostRepository;
import org.example.role.adapters.out.gateways.ds.entities.RoleEntity;
import org.example.role.adapters.out.gateways.ds.repositories.RoleRepository;
import org.example.role.domains.RoleName;
import org.example.shared.ApiResponse;
import org.example.shared.InfoRequest;
import org.example.shared.exceptions.*;
import org.example.user.adapters.out.gateways.ds.entities.*;
import org.example.user.usecases.ports.in.UserRestUseCase;
import org.example.user.adapters.out.gateways.ds.repositories.UserRepository;
import org.example.shared.security.UserPrincipal;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserInteractor implements UserRestUseCase {
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PostRepository postRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public UserSummary getCurrentUser(UserPrincipal currentUser) {
		return new UserSummary(currentUser.getId(), currentUser.getUsername(), currentUser.getFirstName(),
				currentUser.getLastName());
	}

	@Override
	public UserIdentityAvailability checkUsernameAvailability(String username) {
		Boolean isAvailable = !userRepository.existsByUsername(username);
		return new UserIdentityAvailability(isAvailable);
	}

	@Override
	public UserIdentityAvailability checkEmailAvailability(String email) {
		Boolean isAvailable = !userRepository.existsByEmail(email);
		return new UserIdentityAvailability(isAvailable);
	}

	@Override
	public UserProfile getUserProfile(String username) {
		UserEntity user = userRepository.getUserByName(username);

		Long postCount = postRepository.countByCreatedBy(user.getId());

		return new UserProfile(user.getId(), user.getUsername(), user.getFirstName(), user.getLastName(),
				user.getCreatedAt(), user.getEmail(), user.getAddress(), user.getPhone(), user.getWebsite(),
				user.getCompany(), postCount);
	}

	@Override
	public UserEntity addUser(UserEntity user) {
		if (userRepository.existsByUsername(user.getUsername())) {
			ApiResponse apiResponse = new ApiResponse(Boolean.FALSE, "Username is already taken");
			throw new BadRequestException(apiResponse);
		}

		if (userRepository.existsByEmail(user.getEmail())) {
			ApiResponse apiResponse = new ApiResponse(Boolean.FALSE, "Email is already taken");
			throw new BadRequestException(apiResponse);
		}

		List<RoleEntity> roles = new ArrayList<>();
		roles.add(roleRepository.findByName(RoleName.ROLE_USER).orElseThrow(() -> new AppException("User role not set")));
		user.setRoles(roles);

		user.setPassword(passwordEncoder.encode(user.getPassword()));
		return userRepository.save(user);
	}

	@Override
	public UserEntity updateUser(UserEntity newUser, String username, UserPrincipal currentUser) {
		UserEntity user = userRepository.getUserByName(username);
		if (user.getId().equals(currentUser.getId())
				|| currentUser.getAuthorities().contains(new SimpleGrantedAuthority(RoleName.ROLE_ADMIN.toString()))) {
			user.setFirstName(newUser.getFirstName());
			user.setLastName(newUser.getLastName());
			user.setPassword(passwordEncoder.encode(newUser.getPassword()));
			user.setAddress(newUser.getAddress());
			user.setPhone(newUser.getPhone());
			user.setWebsite(newUser.getWebsite());
			user.setCompany(newUser.getCompany());

			return userRepository.save(user);

		}

		ApiResponse apiResponse = new ApiResponse(Boolean.FALSE, "You don't have permission to update profile of: " + username);
		throw new UnauthorizedException(apiResponse);

	}

	@Override
	public ApiResponse deleteUser(String username, UserPrincipal currentUser) {
		UserEntity user = userRepository.findByUsername(username)
				.orElseThrow(() -> new ResourceNotFoundException("User", "id", username));
		if (!user.getId().equals(currentUser.getId()) || !currentUser.getAuthorities()
				.contains(new SimpleGrantedAuthority(RoleName.ROLE_ADMIN.toString()))) {
			ApiResponse apiResponse = new ApiResponse(Boolean.FALSE, "You don't have permission to delete profile of: " + username);
			throw new AccessDeniedException(apiResponse);
		}

		userRepository.deleteById(user.getId());

		return new ApiResponse(Boolean.TRUE, "You successfully deleted profile of: " + username);
	}

	@Override
	public ApiResponse giveAdmin(String username) {
		UserEntity user = userRepository.getUserByName(username);
		List<RoleEntity> roles = new ArrayList<>();
		roles.add(roleRepository.findByName(RoleName.ROLE_ADMIN)
				.orElseThrow(() -> new AppException("User role not set")));
		roles.add(
				roleRepository.findByName(RoleName.ROLE_USER).orElseThrow(() -> new AppException("User role not set")));
		user.setRoles(roles);
		userRepository.save(user);
		return new ApiResponse(Boolean.TRUE, "You gave ADMIN role to user: " + username);
	}

	@Override
	public ApiResponse removeAdmin(String username) {
		UserEntity user = userRepository.getUserByName(username);
		List<RoleEntity> roles = new ArrayList<>();
		roles.add(
				roleRepository.findByName(RoleName.ROLE_USER).orElseThrow(() -> new AppException("User role not set")));
		user.setRoles(roles);
		userRepository.save(user);
		return new ApiResponse(Boolean.TRUE, "You took ADMIN role from user: " + username);
	}

	@Override
	public UserProfile setOrUpdateInfo(UserPrincipal currentUser, InfoRequest infoRequest) {
		UserEntity user = userRepository.findByUsername(currentUser.getUsername())
				.orElseThrow(() -> new ResourceNotFoundException("User", "username", currentUser.getUsername()));
		GeoEntity geo = new GeoEntity(infoRequest.getLat(), infoRequest.getLng());
		AddressEntity address = new AddressEntity(infoRequest.getStreet(), infoRequest.getSuite(), infoRequest.getCity(),
				infoRequest.getZipcode(), geo);
		CompanyEntity company = new CompanyEntity(infoRequest.getCompanyName(), infoRequest.getCatchPhrase(), infoRequest.getBs());
		if (user.getId().equals(currentUser.getId())
				|| currentUser.getAuthorities().contains(new SimpleGrantedAuthority(RoleName.ROLE_ADMIN.toString()))) {
			user.setAddress(address);
			user.setCompany(company);
			user.setWebsite(infoRequest.getWebsite());
			user.setPhone(infoRequest.getPhone());
			UserEntity updatedUser = userRepository.save(user);

			Long postCount = postRepository.countByCreatedBy(updatedUser.getId());

			return new UserProfile(updatedUser.getId(), updatedUser.getUsername(),
					updatedUser.getFirstName(), updatedUser.getLastName(), updatedUser.getCreatedAt(),
					updatedUser.getEmail(), updatedUser.getAddress(), updatedUser.getPhone(), updatedUser.getWebsite(),
					updatedUser.getCompany(), postCount);
		}

		ApiResponse apiResponse = new ApiResponse(Boolean.FALSE, "You don't have permission to update users profile", HttpStatus.FORBIDDEN);
		throw new AccessDeniedException(apiResponse);
	}
}
