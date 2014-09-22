/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 *
 * @author srikanth
 */
@ControllerAdvice
public class GlobalControllerExceptionHandler {

    
    //handles method request validation errors 
    @ExceptionHandler
    public ResponseEntity<Map<String, String>> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException errors) {
        Map<String, String> errorMap = new HashMap<String, String>();
        errorMap.put("hasErrors", "true");
        errorMap.put("errorMessage", errors.getMessage());
        errors.printStackTrace();
        for (FieldError error : errors.getBindingResult().getFieldErrors()) {
            errorMap.put(error.getField(), error.getDefaultMessage());
        }

        return new ResponseEntity<Map<String, String>>(errorMap, HttpStatus.BAD_REQUEST);
    }
}
