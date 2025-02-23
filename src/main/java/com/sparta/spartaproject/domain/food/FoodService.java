package com.sparta.spartaproject.domain.food;

import com.sparta.spartaproject.common.SortUtils;
import com.sparta.spartaproject.domain.CircularService;
import com.sparta.spartaproject.domain.image.EntityType;
import com.sparta.spartaproject.domain.image.ImageService;
import com.sparta.spartaproject.domain.store.Store;
import com.sparta.spartaproject.domain.user.Role;
import com.sparta.spartaproject.domain.user.User;
import com.sparta.spartaproject.dto.request.CreateFoodRequestDto;
import com.sparta.spartaproject.dto.request.UpdateFoodRequestDto;
import com.sparta.spartaproject.dto.request.UpdateFoodStatusRequestDto;
import com.sparta.spartaproject.dto.response.FoodDetailDto;
import com.sparta.spartaproject.dto.response.FoodDto;
import com.sparta.spartaproject.exception.BusinessException;
import com.sparta.spartaproject.exception.ErrorCode;
import com.sparta.spartaproject.mapper.FoodMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class FoodService {
    private final FoodMapper foodMapper;
    private final ImageService imageService;
    private final FoodRepository foodRepository;
    private final CircularService circularService;

    private final Integer size = 10;

    @Transactional
    public void createFood(CreateFoodRequestDto request, MultipartFile image) {
        User user = circularService.getUserService().loginUser();
        Store store = circularService.getStoreService().getStoreById(request.storeId());

        if (user.getRole() == Role.OWNER) {
            if (!user.getId().equals(store.getOwner().getId())) {
                throw new BusinessException(ErrorCode.FOOD_FORBIDDEN);
            }
        }

        String descriptionForGemini = circularService.getGeminiService().requestGemini(
                store.getId(), request.description()
        );

        Food newFood = foodMapper.toFood(request, store, descriptionForGemini);
        foodRepository.save(newFood);

        if (image!=null) {
            String imagePathForFood = imageService.uploadImage(newFood.getId(), EntityType.FOOD, image);
            log.info("음식 이미지 등록 완료 : {}", imagePathForFood);
        }

        log.info("가게: {}, 음식 생성 완료", store.getId());
    }

    @Transactional(readOnly = true)
    public FoodDto getAllFoods(int page, String sortDirection) {
        Sort sort = SortUtils.getSort(sortDirection);
        Pageable pageable = PageRequest.of(page - 1, size, sort);

        List<Food> foodList = foodRepository.findAll(pageable).getContent();

        return getFoodDto(page, foodList);
    }

    @Transactional(readOnly = true)
    public FoodDto getAllFoodsForStore(UUID storeId, int page, String sortDirection) {
        Store store = circularService.getStoreService().getStoreById(storeId);

        log.info("store.getName() : {}", store.getName());

        Sort sort = SortUtils.getSort(sortDirection);
        Pageable pageable = PageRequest.of(page - 1, size, sort);

        List<Food> foodList = foodRepository.findByStoreAndIsDisplayedIsTrueAndIsDeletedIsFalse(store, pageable);

        log.info("foodList.size() : {}", foodList.size());

        return getFoodDto(page, foodList);
    }

    @Transactional(readOnly = true)
    public FoodDto getAllFoodsForStoreByOwner(UUID storeId, int page, String sortDirection) {
        User user = circularService.getUserService().loginUser();
        Store store = circularService.getStoreService().getStoreById(storeId);

        if (user.getRole() == Role.OWNER) {
            if (!user.getId().equals(store.getOwner().getId())) {
                throw new BusinessException(ErrorCode.FOOD_FORBIDDEN);
            }
        }

        Sort sort = SortUtils.getSort(sortDirection);
        Pageable pageable = PageRequest.of(page - 1, size, sort);

        List<Food> foodList = foodRepository.findByStoreAndIsDeletedIsFalse(store, pageable);

        return getFoodDto(page, foodList);
    }

    @Transactional(readOnly = true)
    public FoodDetailDto getFood(UUID id) {
        Food food = getFoodById(id);

        String imageUrl = imageService.getImageUrlByEntity(food.getId(), EntityType.FOOD).get(0);
        log.info("조회한 food ID: " + food.getId() + " imageUrl: " + imageUrl);

        return foodMapper.toFoodDetailDto(food, imageUrl);
    }

    @Transactional
    public void updateFood(UUID id, UpdateFoodRequestDto update, MultipartFile image) {
        User user = circularService.getUserService().loginUser();
        Food food = getFoodById(id);
        Store store = food.getStore();

        if (user.getRole() == Role.OWNER) {
            if (!user.getId().equals(store.getOwner().getId())) {
                throw new BusinessException(ErrorCode.FOOD_FORBIDDEN);
            }
        }

        food.update(update);

        imageService.deleteAllImagesByEntity(id, EntityType.FOOD);

        if (image!=null){
            imageService.uploadImage(id,EntityType.FOOD, image);
        }

        log.info("음식 수정 완료 : {}", food.getName());
    }

    @Transactional
    public void updateFoodStatus(UUID id, UpdateFoodStatusRequestDto update) {
        User user = circularService.getUserService().loginUser();
        Food food = getFoodById(id);
        Store store = food.getStore();

        if (user.getRole() == Role.OWNER) {
            if (!user.getId().equals(store.getOwner().getId())) {
                throw new BusinessException(ErrorCode.FOOD_FORBIDDEN);
            }
        }

        food.updateStatus(update);
        log.info("음식: {},  상태 정보 수정 완료", food.getId());
    }

    @Transactional
    public void toggleDisplay(UUID id) {
        User user = circularService.getUserService().loginUser();
        Food food = getFoodById(id);
        Store store = food.getStore();

        if (user.getRole() == Role.OWNER) {
            if (!user.getId().equals(store.getOwner().getId())) {
                throw new BusinessException(ErrorCode.FOOD_FORBIDDEN);
            }
        }

        if (food.getIsDisplayed().equals(Boolean.TRUE)) {
            log.info("음식: {}, 숨김 처리", food.getId());
        } else {
            log.info("음식: {}, 숨김 처리 해제", food.getId());
        }

        food.toggleIsDisplayed();
    }

    // 음식 삭제
    @Transactional
    public void deleteFood(UUID id) {
        User user = circularService.getUserService().loginUser();
        Food food = getFoodById(id);
        Store store = food.getStore();

        if (user.getRole() == Role.OWNER) {
            if (!user.getId().equals(store.getOwner().getId())) {
                throw new BusinessException(ErrorCode.FOOD_FORBIDDEN);
            }
        }

        food.delete();

        imageService.deleteAllImagesByEntity(food.getId(), EntityType.FOOD);

        log.info("음식: {}, 삭제 완료", id);
    }

    public Food getFoodById(UUID id) {
        return foodRepository.findByIdAndIsDeletedFalse(id)
            .orElseThrow(() -> new BusinessException(ErrorCode.FOOD_NOT_FOUND));
    }

    private FoodDto getFoodDto(int page, List<Food> foodList) {
        Integer totalPages = (int) Math.ceil((double) foodList.size() / size);

        return foodMapper.toFoodDto(
            foodList.stream().map(
            food -> {
                String imageUrl = imageService.getImageUrlByEntity(food.getId(), EntityType.FOOD)
                    .stream()
                    .findFirst()
                    .orElse(null);

                log.info("Image url: {}", imageUrl);
                return foodMapper.toFoodDetailDto(food, imageUrl);
            }).toList(),
            page,
            totalPages
        );
    }

}
