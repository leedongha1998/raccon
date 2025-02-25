package com.sparta.spartaproject.domain.gemini;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface GeminiHistoryRepository extends JpaRepository<GeminiHistory, UUID> {
    @Query("SELECT gh FROM GeminiHistory gh " +
            "WHERE gh.requestText LIKE CONCAT('%', :name, '%')")
    List<GeminiHistory> findAllByRequestTextContaining(
            @Param("name") String name,
            Pageable pageable
    ); // page 의 count 쿼리 1개 더 나가는거 방지하기 위해 추가
}