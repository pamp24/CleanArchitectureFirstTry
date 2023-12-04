package org.example.post.adapters.out.gateways.ds.entities;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.example.category.adapters.out.gateways.ds.entities.CategoryEntity;
import org.example.comment.adapters.out.gateways.ds.entities.CommentEntity;
import org.example.shared.models.audit.UserDateAudit;
import org.example.tag.adapters.out.gateways.ds.entities.TagEntity;
import org.example.user.adapters.out.gateways.ds.entities.UserEntity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@Table(name = "posts", uniqueConstraints = { @UniqueConstraint(columnNames = { "title" }) })
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class PostEntity extends UserDateAudit {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "title")
	private String title;

	@Column(name = "body")
	private String body;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private UserEntity user;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "category_id")
	private CategoryEntity category;

	@JsonIgnore
	@OneToMany(mappedBy = "org/example/post", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<CommentEntity> comments;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "post_tag", joinColumns = @JoinColumn(name = "post_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "tag_id", referencedColumnName = "id"))
	private List<TagEntity> tags;

	@JsonIgnore
	public UserEntity getUser() {
		return user;
	}

	public void setUser(UserEntity user) {
		this.user = user;
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

	public List<TagEntity> getTags() {
		return tags == null ? null : new ArrayList<>(tags);
	}

	public void setTags(List<TagEntity> tags) {
		if (tags == null) {
			this.tags = null;
		} else {
			this.tags = Collections.unmodifiableList(tags);
		}
	}
}
