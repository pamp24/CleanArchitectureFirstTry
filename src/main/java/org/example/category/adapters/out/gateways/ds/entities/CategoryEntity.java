package org.example.category.adapters.out.gateways.ds.entities;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.example.post.adapters.out.gateways.ds.entities.PostEntity;
import org.example.shared.models.audit.UserDateAudit;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@NoArgsConstructor
@Table(name = "categories")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class CategoryEntity extends UserDateAudit {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "name")
	private String name;

	@OneToMany(mappedBy = "org/example/category", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<PostEntity> posts;

	public CategoryEntity(String name) {
		super();
		this.name = name;
	}

	public List<PostEntity> getPosts() {
		return this.posts == null ? null : new ArrayList<>(this.posts);
	}

	public void setPosts(List<PostEntity> posts) {
		if (posts == null) {
			this.posts = null;
		} else {
			this.posts = Collections.unmodifiableList(posts);
		}
	}

}
