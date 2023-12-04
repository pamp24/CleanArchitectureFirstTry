package org.example.role.adapters.out.gateways.ds.repositories;


import org.example.role.adapters.out.gateways.ds.entities.RoleEntity;
import org.example.role.domains.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<RoleEntity, Long> {
	Optional<RoleEntity> findByName(RoleName name);
}
