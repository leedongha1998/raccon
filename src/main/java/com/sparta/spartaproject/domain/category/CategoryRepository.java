package com.sparta.spartaproject.domain.category;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CategoryRepository extends JpaRepository<Category, UUID> {
    Boolean existsByName(String name);

    List<Category> findByIdIn(List<UUID> categoriesIds);
}