package com.sparta.spartaproject.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@RequiredArgsConstructor
@Getter
public enum ErrorCode {

    //Common -> http 요청시 발생할만한 예외
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "Common-001", " Invalid Input Value"),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "Common-002", " Invalid Http Method"),
    ENTITY_NOT_FOUND(HttpStatus.BAD_REQUEST, "Common-003", " Entity Not Found"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Common-004", "Server Error"),
    INVALID_TYPE_VALUE(HttpStatus.BAD_REQUEST, "Common-005", " Invalid Type Value"),
    HANDLE_ACCESS_DENIED(HttpStatus.FORBIDDEN, "Common-006", "Access is Denied"),

    //Member Validation
    EMAIL_DUPLICATION(HttpStatus.BAD_REQUEST, "Member-001", "중복 이메일입니다."),
    LOGIN_INPUT_INVALID(HttpStatus.BAD_REQUEST, "Member-002", "비밀번호 혹은 아이디가 일치하지 않습니다."),
    NOT_EXIST_USER(HttpStatus.NOT_FOUND, "Member-003", "존재하지 않는 회원입니다."),

    //room Validation


    //...등

    // 찜
    LIKE_NOT_FOUND(HttpStatus.NOT_FOUND, "Like-001", "찜 목록이 존재하지 않습니다."),

    // 리뷰
    REVIEW_NOT_FOUND(HttpStatus.NOT_FOUND, "Review-001", "리뷰가 존재하지 않습니다."),

    // 가게
    STORE_NOT_FOUND(HttpStatus.NOT_FOUND, "Store-001", "가게 정보가 존재하지 않습니다."),
    STORE_UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "Store-002", "가게 정보에 대한 권한이 존재하지 않습니다."),

    // 카테고리
    CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "Category-001", "카테고리 정보가 존재하지 않습니다.");

    private final HttpStatus status; // http 상태코드
    private final String code;//에러코드
    private final String message;//에러메시지
}