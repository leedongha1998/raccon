package com.sparta.spartaproject.controller;

import com.sparta.spartaproject.domain.review.ReviewService;
import com.sparta.spartaproject.dto.request.CreateReviewRequestDto;
import com.sparta.spartaproject.dto.request.UpdateReviewRequestDto;
import com.sparta.spartaproject.dto.response.ReviewDto;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Description;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reviews")
public class ReviewController {
    private final ReviewService reviewService;

    @Description(
        "리뷰 전체 조회"
    )
    @GetMapping
    @PreAuthorize("hasAnyAuthority('MASTER', 'MANAGER')")
    public ResponseEntity<List<ReviewDto>> getReviews(
        @RequestParam(required = false, defaultValue = "1") int page,
        @RequestParam(defaultValue = "asc") String sortDirection
    ) {
        return ResponseEntity.ok(reviewService.getReviews(page, sortDirection));
    }

    @Description(
        "내가 작성한 리뷰 전체 조회"
    )
    @GetMapping("/my")
    public ResponseEntity<List<ReviewDto>> getMyReviews(
        @RequestParam(required = false, defaultValue = "1") int page,
        @RequestParam(defaultValue = "asc") String sortDirection
    ) {
        return ResponseEntity.ok(reviewService.getMyReviews(page, sortDirection));
    }

    @Description(
        "리뷰 상세 조회"
    )
    @GetMapping("/{id}")
    public ResponseEntity<ReviewDto> getReview(@PathVariable UUID id) {
        return ResponseEntity.ok(reviewService.getReview(id));
    }

    @Description(
        "리뷰 생성하기"
    )
    @PostMapping
    public ResponseEntity<Void> createReview(@RequestBody CreateReviewRequestDto request) {
        reviewService.createReview(request);
        return ResponseEntity.ok().build();
    }

    @Description(
        "리뷰 수정하기"
    )
    @PatchMapping("/{id}")
    public ResponseEntity<Void> updateReview(@PathVariable UUID id, @RequestBody UpdateReviewRequestDto update) {
        reviewService.updateReview(id, update);
        return ResponseEntity.ok().build();
    }

    @Description(
        "리뷰 삭제하기"
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReview(@PathVariable UUID id) {
        reviewService.deleteReview(id);
        return ResponseEntity.ok().build();
    }

    @Description(
        "가게 리뷰 전체조회"
    )
    @GetMapping("/stores/{storeId}")
    public ResponseEntity<List<ReviewDto>> getReviewsForStore(
        @PathVariable UUID storeId,
        @RequestParam(required = false, defaultValue = "1") int page,
        @RequestParam(defaultValue = "asc") String sortDirection
    ) {
        return ResponseEntity.ok(reviewService.getReviewsForStore(storeId, page, sortDirection));
    }
}