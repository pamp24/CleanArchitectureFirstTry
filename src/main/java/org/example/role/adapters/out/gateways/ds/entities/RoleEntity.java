package org.example.role.adapters.out.gateways.ds.entities;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.role.domains.RoleName;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@Table(name = "roles")
public class RoleEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Enumerated(EnumType.STRING)
	@NaturalId
	@Column(name = "name")
	private RoleName name;

	public RoleEntity(RoleName name) {
		this.name = name;
	}
}
