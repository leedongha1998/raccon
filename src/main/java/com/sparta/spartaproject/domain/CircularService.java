package com.sparta.spartaproject.domain;

import com.sparta.spartaproject.domain.food.FoodService;
import com.sparta.spartaproject.domain.mail.MailService;
import com.sparta.spartaproject.domain.store.StoreService;
import com.sparta.spartaproject.domain.user.CustomUserDetailsService;
import com.sparta.spartaproject.domain.user.UserService;
import com.sparta.spartaproject.domain.verify.VerifyService;
import lombok.Getter;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Getter
@Component
public class CircularService {
    private final UserService userService;
    private final MailService mailService;
    private final VerifyService verifyService;
    private final StoreService storeService;
    private final FoodService foodService;
    private final CustomUserDetailsService customUserDetailsService;

    public CircularService(
        @Lazy UserService userService,
        @Lazy MailService mailService,
        @Lazy VerifyService verifyService,
        @Lazy StoreService storeService,
        @Lazy FoodService foodService,
        @Lazy CustomUserDetailsService customUserDetailsService
    ) {
        this.userService = userService;
        this.mailService = mailService;
        this.verifyService = verifyService;
        this.storeService = storeService;
        this.foodService = foodService;
        this.customUserDetailsService = customUserDetailsService;
    }
}