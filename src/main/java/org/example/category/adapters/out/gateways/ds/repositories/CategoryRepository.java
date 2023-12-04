package org.example.category.adapters.out.gateways.ds.repositories;


import org.example.category.adapters.out.gateways.ds.entities.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {

}
