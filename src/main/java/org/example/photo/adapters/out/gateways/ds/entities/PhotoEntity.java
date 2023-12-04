package org.example.photo.adapters.out.gateways.ds.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.example.album.adapters.out.gateways.ds.entities.AlbumEntity;
import org.example.shared.models.audit.UserDateAudit;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@NoArgsConstructor
@Table(name = "photos", uniqueConstraints = { @UniqueConstraint(columnNames = { "title" }) })
public class PhotoEntity extends UserDateAudit {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank
	@Column(name = "title")
	private String title;

	@NotBlank
	@Column(name = "url")
	private String url;

	@NotBlank
	@Column(name = "thumbnail_url")
	private String thumbnailUrl;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "album_id")
	private AlbumEntity albumEntity;

	public PhotoEntity(@NotBlank String title, @NotBlank String url, @NotBlank String thumbnailUrl, AlbumEntity album) {
		this.title = title;
		this.url = url;
		this.thumbnailUrl = thumbnailUrl;
		this.albumEntity = album;
	}

	@JsonIgnore
	public AlbumEntity getAlbum() {
		return albumEntity;
	}
}
