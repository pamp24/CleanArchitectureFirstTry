package org.example.user.adapters.out.gateways.ds.entities;


import org.example.album.adapters.out.gateways.ds.entities.AlbumEntity;
import org.example.comment.adapters.out.gateways.ds.entities.CommentEntity;
import org.example.post.adapters.out.gateways.ds.entities.PostEntity;
import org.example.role.adapters.out.gateways.ds.entities.RoleEntity;
import org.example.shared.models.audit.DateAudit;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.example.todo.adapters.out.gateways.ds.entities.TodoEntity;
import org.hibernate.annotations.NaturalId;
import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@NoArgsConstructor
@Table(name = "users", uniqueConstraints = { @UniqueConstraint(columnNames = { "username" }),
		@UniqueConstraint(columnNames = { "email" }) })
public class UserEntity extends DateAudit {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@NotBlank
	@Column(name = "first_name")
	@Size(max = 40)
	private String firstName;

	@NotBlank
	@Column(name = "last_name")
	@Size(max = 40)
	private String lastName;

	@NotBlank
	@Column(name = "username")
	@Size(max = 15)
	private String username;

	@NotBlank
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	@Size(max = 100)
	@Column(name = "password")
	private String password;

	@NotBlank
	@NaturalId
	@Size(max = 40)
	@Column(name = "email")
	@Email
	private String email;

	@OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "address_id")
	private AddressEntity address;

	@Column(name = "phone")
	private String phone;

	@Column(name = "website")
	private String website;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
	private List<RoleEntity> roles;

	@JsonIgnore
	@OneToMany(mappedBy = "org/example/user", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<TodoEntity> todos;

	@JsonIgnore
	@OneToMany(mappedBy = "org/example/user", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<AlbumEntity> albums;

	@JsonIgnore
	@OneToMany(mappedBy = "org/example/user", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<PostEntity> posts;

	@JsonIgnore
	@OneToMany(mappedBy = "org/example/user", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<CommentEntity> comments;

	@OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "company_id")
	private CompanyEntity company;

	public UserEntity(String firstName, String lastName, String username, String email, String password) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.username = username;
		this.email = email;
		this.password = password;
	}



	public List<TodoEntity> getTodos() {

		return todos == null ? null : new ArrayList<>(todos);
	}

	public void setTodos(List<TodoEntity> todos) {

		if (todos == null) {
			this.todos = null;
		} else {
			this.todos = Collections.unmodifiableList(todos);
		}
	}

	public List<AlbumEntity> getAlbums() {

		return albums == null ? null : new ArrayList<>(albums);
	}

	public void setAlbums(List<AlbumEntity> albums) {

		if (albums == null) {
			this.albums = null;
		} else {
			this.albums = Collections.unmodifiableList(albums);
		}
	}


	public List<PostEntity> getPosts() {

		return posts == null ? null : new ArrayList<>(posts);
	}

	public void setPosts(List<PostEntity> posts) {

		if (posts == null) {
			this.posts = null;
		} else {
			this.posts = Collections.unmodifiableList(posts);
		}
	}

	public List<RoleEntity> getRoles() {

		return roles == null ? null : new ArrayList<>(roles);
	}

	public void setRoles(List<RoleEntity> roles) {

		if (roles == null) {
			this.roles = null;
		} else {
			this.roles = Collections.unmodifiableList(roles);
		}
	}

	public List<CommentEntity> getComments() {
		return comments == null ? null : new ArrayList<>(comments);
	}

	public void setComments(List<CommentEntity> comments) {

		if (comments == null) {
			this.comments = null;
		} else {
			this.comments = Collections.unmodifiableList(comments);
		}
	}
}
