package controller;

import java.util.HashMap;
import java.util.Map;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalControllerExceptionHandler {

 /**
  * 
  * Exception to be thrown when validation on an argument annotated with @Valid fails.
  */
  @ExceptionHandler(MethodArgumentNotValidException.class)
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
  
 /**
  * thrown at flush or commit time for detached entities 
  */
  @ExceptionHandler(PersistenceException.class)
  public ResponseEntity<Map<String, String>> handlePersistenceException(
        PersistenceException errors) {
    Map<String, String> errorMap = new HashMap<String, String>();
    errorMap.put("hasErrors", "true");
    errorMap.put("errorMessage", errors.getMessage());
    errors.printStackTrace();
    return new ResponseEntity<Map<String, String>>(errorMap, HttpStatus.INTERNAL_SERVER_ERROR);
  }
  
 /**
  * Thrown by the persistence provider when getSingleResult() is executed on a query
    and there is no result to return.
  */
  @ExceptionHandler(NoResultException.class)
  public ResponseEntity<Map<String, String>> handleNoResultException(
        NoResultException errors) {
    Map<String, String> errorMap = new HashMap<String, String>();
    errorMap.put("hasErrors", "true");
    errorMap.put("errorMessage", errors.getMessage());
    errors.printStackTrace();
    return new ResponseEntity<Map<String, String>>(errorMap, HttpStatus.NOT_FOUND);
  }
  
   
}
