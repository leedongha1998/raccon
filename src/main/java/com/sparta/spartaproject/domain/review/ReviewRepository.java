package com.sparta.spartaproject.domain.review;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface ReviewRepository extends JpaRepository<Review, UUID> {

    @Query(
        "SELECT r FROM Review r " +
            "WHERE r.isDeleted = false " +
            "AND r.content LIKE CONCAT('%', :name, '%')"
    )
    Page<Review> findAllByIsDeletedIsFalse(@Param("name") String name, Pageable pageable);

    @Query(
        "SELECT r FROM Review r " +
            "WHERE r.user.id = :userId AND r.isDeleted = false " +
            "AND r.content LIKE CONCAT('%', :name, '%')"
    )
    Page<Review> findAllByUserIdAndContentContainingAndIsDeletedIsFalse(
        @Param("userId") Long userId,
        @Param("name") String name,
        Pageable pageable
    );

    @Query(
        "SELECT r FROM Review r " +
            "WHERE r.store.id = :storeId AND r.isDeleted = false " +
            "AND r.content LIKE CONCAT('%', :name, '%')"
    )
    Page<Review> findAllByStoreIdAndContentContainingAndIsDeletedIsFalse(
        @Param("storeId") UUID storeId,
        @Param("name") String name,
        Pageable pageable
    );

    Boolean existsByStoreIdAndOrderIdAndIsDeletedIsFalse(UUID storeId, UUID orderId);

    Long countByStoreIdAndIsDeletedIsFalse(UUID storeId);

    Optional<Review> findByIdAndIsDeletedIsFalse(UUID id);
}