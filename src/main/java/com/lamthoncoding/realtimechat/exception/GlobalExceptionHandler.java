package com.lamthoncoding.realtimechat.exception;

import com.lamthoncoding.realtimechat.exception.handlers.EntityNotFound;
import com.lamthoncoding.realtimechat.exception.handlers.InvalidInputException;
import com.lamthoncoding.realtimechat.exception.handlers.UnauthorizedException;
import com.lamthoncoding.realtimechat.payload.response.ApiCode;
import com.lamthoncoding.realtimechat.payload.response.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.util.List;


@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(EntityNotFound.class)
    public ResponseEntity<ApiResponse<?>> handleEntityNotFound(EntityNotFound e) {
        String message = e.getMessage();
        if (message == null) {
            message = "Entity not found";
        }
        log.error(message);
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error(ApiCode.NOT_FOUND, message));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<?>> handleMethodArgumentNotValid(MethodArgumentNotValidException e) {

        List<String> errors = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .toList();

        log.error(errors.toString());

        return ResponseEntity.badRequest()
                .body(ApiResponse.error(ApiCode.VALIDATION_ERROR, errors));
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ApiResponse<?>> handleUnauthorized(UnauthorizedException e) {
        String message = e.getMessage();
        log.error(message);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResponse.error(ApiCode.UNAUTHORIZED, message));
    }

    @ExceptionHandler(InvalidInputException.class)
    public ResponseEntity<ApiResponse<?>> handleInvalidInput(InvalidInputException e) {
        String message = e.getMessage();
        log.error(message);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.error(ApiCode.CONFLICT, message));
    }


    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ApiResponse<?>> handleMaxSizeException() {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.error(ApiCode.CONFLICT, "File too big"));
    }
}
