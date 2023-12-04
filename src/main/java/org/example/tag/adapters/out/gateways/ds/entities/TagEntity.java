package org.example.tag.adapters.out.gateways.ds.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
@Table(name = "tags")
//@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class TagEntity extends UserDateAudit {

	private static final long serialVersionUID = -5298707266367331514L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "name")
	private String name;

	@JsonIgnore
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "post_tag", joinColumns = @JoinColumn(name = "tag_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "post_id", referencedColumnName = "id"))
	private List<PostEntity> posts;

	public TagEntity(String name) {
		super();
		this.name = name;
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

}
