package org.example.comment.adapters.out.gateways.ds.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import org.example.post.adapters.out.gateways.ds.entities.PostEntity;
import org.example.shared.models.audit.UserDateAudit;
import org.example.user.adapters.out.gateways.ds.entities.UserEntity;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@NoArgsConstructor
@Table(name = "comments")
public class CommentEntity extends UserDateAudit {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    @NotBlank
    @Size(min = 4, max = 50)
    private String name;

    @Column(name = "email")
    @NotBlank
    @Email
    @Size(min = 4, max = 50)
    private String email;

    @Column(name = "body")
    @NotBlank
    @Size(min = 10, message = "Comment body must be minimum 10 characters")
    private String body;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private PostEntity post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    public CommentEntity(@NotBlank @Size(min = 10, message = "Comment body must be minimum 10 characters") String body) {
        this.body = body;
    }

    @JsonIgnore
    public PostEntity getPost() {
        return post;
    }

    @JsonIgnore
    public UserEntity getUser() {
        return user;
    }
}
