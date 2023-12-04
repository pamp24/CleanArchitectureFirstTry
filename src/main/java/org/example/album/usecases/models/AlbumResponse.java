package org.example.album.usecases.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.example.photo.adapters.out.gateways.ds.entities.PhotoEntity;
import org.example.shared.models.audit.UserDateAuditPayload;
import org.springframework.security.core.userdetails.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@JsonInclude(Include.NON_NULL)
public class AlbumResponse extends UserDateAuditPayload {

	private String title;

	private User user;

	private List<PhotoEntity> photo;

	public List<PhotoEntity> getPhoto() {

		return photo == null ? null : new ArrayList<>(photo);
	}

	public void setPhoto(List<PhotoEntity> photo) {

		if (photo == null) {
			this.photo = null;
		} else {
			this.photo = Collections.unmodifiableList(photo);
		}
	}
}
