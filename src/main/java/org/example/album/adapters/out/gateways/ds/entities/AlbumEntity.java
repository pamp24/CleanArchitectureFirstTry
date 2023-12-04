package org.example.album.adapters.out.gateways.ds.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.example.photo.adapters.out.gateways.ds.entities.PhotoEntity;
import org.example.shared.models.audit.UserDateAudit;
import org.example.user.adapters.out.gateways.ds.entities.UserEntity;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@Table(name = "albums", uniqueConstraints = { @UniqueConstraint(columnNames = { "title" }) })
public class AlbumEntity extends UserDateAudit {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank
	@Column(name = "title")
	private String title;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private UserEntity user;

	@OneToMany(mappedBy = "org/example/album", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<PhotoEntity> photo;

	@JsonIgnore
	public UserEntity getUser() {
		return user;
	}

	public List<PhotoEntity> getPhoto() {
		return this.photo == null ? null : new ArrayList<>(this.photo);
	}

	public void setPhoto(List<PhotoEntity> photo) {
		if (photo == null) {
			this.photo = null;
		} else {
			this.photo = Collections.unmodifiableList(photo);
		}
	}
}
