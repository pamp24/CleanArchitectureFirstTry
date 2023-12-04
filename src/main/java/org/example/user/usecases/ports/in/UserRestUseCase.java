package org.example.user.usecases.ports.in;

import org.example.shared.ApiResponse;
import org.example.shared.InfoRequest;
import org.example.shared.security.UserPrincipal;
import org.example.user.adapters.out.gateways.ds.entities.UserEntity;
import org.example.user.adapters.out.gateways.ds.entities.UserIdentityAvailability;
import org.example.user.adapters.out.gateways.ds.entities.UserProfile;
import org.example.user.adapters.out.gateways.ds.entities.UserSummary;

public interface UserRestUseCase {

	UserSummary getCurrentUser(UserPrincipal currentUser);

	UserIdentityAvailability checkUsernameAvailability(String username);

	UserIdentityAvailability checkEmailAvailability(String email);

	UserProfile getUserProfile(String username);

	UserEntity addUser(UserEntity user);

	UserEntity updateUser(UserEntity newUser, String username, UserPrincipal currentUser);

	ApiResponse deleteUser(String username, UserPrincipal currentUser);

	ApiResponse giveAdmin(String username);

	ApiResponse removeAdmin(String username);

	UserProfile setOrUpdateInfo(UserPrincipal currentUser, InfoRequest infoRequest);

}