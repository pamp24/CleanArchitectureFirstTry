package org.example.user.adapters.out.gateways.ds.repositories;


import org.example.shared.exceptions.ResourceNotFoundException;
import org.example.shared.security.UserPrincipal;
import org.example.user.adapters.out.gateways.ds.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;
import javax.validation.constraints.NotBlank;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
	Optional<UserEntity> findByUsername(@NotBlank String username);

	Optional<UserEntity> findByEmail(@NotBlank String email);

	Boolean existsByUsername(@NotBlank String username);

	Boolean existsByEmail(@NotBlank String email);

	Optional<UserEntity> findByUsernameOrEmail(String username, String email);

	default org.example.user.adapters.out.gateways.ds.entities.UserEntity getUser(UserPrincipal currentUser) {
		return getUserByName(currentUser.getUsername());
	}

	default org.example.user.adapters.out.gateways.ds.entities.UserEntity getUserByName(String username) {
		return findByUsername(username)
				.orElseThrow(() -> new ResourceNotFoundException("User", "username", username));
	}
}
