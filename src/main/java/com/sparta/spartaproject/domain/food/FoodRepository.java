package com.sparta.spartaproject.domain.food;

import com.sparta.spartaproject.domain.store.Store;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface FoodRepository extends JpaRepository<Food, UUID> {
    Optional<Food> findByIdAndIsDeletedFalse(UUID id);

    @Query("SELECT f FROM Food f WHERE f.name LIKE CONCAT('%', :name, '%')")
    Page<Food> findAllFoodList(@Param("name") String name, Pageable customPageable);

    @Query("SELECT f FROM Food f " +
            "WHERE f.store = :store " +
            "AND f.isDisplayed = true " +
            "AND f.isDeleted = false " +
            "AND f.name LIKE CONCAT('%', :name, '%')")
    Page<Food> findByStoreAndIsDisplayedIsTrueAndIsDeletedIsFalse(
            @Param("store") Store store,
            @Param("name") String name,
            Pageable pageable
    );

    @Query("SELECT f FROM Food f " +
            "WHERE f.store = :store " +
            "AND f.isDeleted = false " +
            "AND f.name LIKE CONCAT('%', :name, '%')")
    Page<Food> findByStoreAndIsDeletedIsFalse(
            @Param("store") Store store,
            @Param("name") String name,
            Pageable pageable
    );


}