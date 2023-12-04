package org.example.post.usecases.ports.in;



import org.example.post.adapters.out.gateways.ds.entities.PostEntity;
import org.example.post.usecases.models.PostRequest;
import org.example.post.usecases.models.PostResponse;
import org.example.shared.ApiResponse;
import org.example.auth.usecases.models.PagedResponse;
import org.example.shared.security.UserPrincipal;

public interface PostRestUseCase {

	PagedResponse<PostEntity> getAllPosts(int page, int size);

	PagedResponse<PostEntity> getPostsByCreatedBy(String username, int page, int size);

	PagedResponse<PostEntity> getPostsByCategory(Long id, int page, int size);

	PagedResponse<PostEntity> getPostsByTag(Long id, int page, int size);

	PostEntity updatePost(Long id, PostRequest newPostRequest, UserPrincipal currentUser);

	ApiResponse deletePost(Long id, UserPrincipal currentUser);

	PostResponse addPost(PostRequest postRequest, UserPrincipal currentUser);

	PostEntity getPost(Long id);

}
