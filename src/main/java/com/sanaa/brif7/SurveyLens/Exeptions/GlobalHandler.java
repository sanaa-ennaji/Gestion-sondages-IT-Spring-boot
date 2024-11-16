package com.sanaa.brif7.SurveyLens.Exeptions;

import com.sanaa.brif7.SurveyLens.dto.ErrorDTO;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDTO> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            if (error instanceof FieldError) {
                String fieldName = ((FieldError) error).getField();
                String errorMessage = error.getDefaultMessage();
                errors.put(fieldName, errorMessage);
            } else {
                String objectName = error.getObjectName();
                String errorMessage = error.getDefaultMessage();
                errors.put(objectName, errorMessage);
            }
        });

        String message = "error de validation: " + errors;
        return new ResponseEntity<>(new ErrorDTO(message, HttpStatus.BAD_REQUEST.value()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorDTO> handleConstraintViolationException(ConstraintViolationException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getConstraintViolations().forEach(violation -> {
            String fieldName = violation.getPropertyPath().toString();
            String errorMessage = violation.getMessage();
            errors.put(fieldName, errorMessage);
        });

        String message = "error of validation : " + errors;
        return new ResponseEntity<>(new ErrorDTO(message, HttpStatus.BAD_REQUEST.value()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorDTO> handleIllegalArgumentException(IllegalArgumentException ex) {
        String message = "error : " + ex.getMessage();
        return new ResponseEntity<>(new ErrorDTO(message, HttpStatus.BAD_REQUEST.value()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDTO> handleGeneralException(Exception ex) {
        return new ResponseEntity<>(new ErrorDTO("error found : " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value()), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}