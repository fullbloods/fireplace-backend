package com.fullbloods.fireplace.common.exception.advice;

import com.fullbloods.fireplace.common.response.CommonResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class ExceptionAdvice {

    @ExceptionHandler({IllegalArgumentException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public CommonResponse<Void> handleIllegalArgumentException(IllegalArgumentException e) {
        return new CommonResponse<>(HttpStatus.BAD_REQUEST.value(), null, false, e.getMessage());
    }

    @ExceptionHandler({IllegalStateException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public CommonResponse<Void> handleIllegalStateException(IllegalStateException e) {
        return new CommonResponse<>(HttpStatus.BAD_REQUEST.value(), null, false, e.getMessage());
    }

    @ExceptionHandler({BadCredentialsException.class})
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public CommonResponse<Void> handleBadCredentialsException(BadCredentialsException e) {
        return new CommonResponse<>(HttpStatus.UNAUTHORIZED.value(), null, false, e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public CommonResponse<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        return new CommonResponse<>(HttpStatus.BAD_REQUEST.value(), null, false, ex.getBindingResult().getFieldErrors().get(0).getDefaultMessage());
    }
}