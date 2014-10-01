package controller;

import com.fasterxml.jackson.databind.JsonMappingException;
import java.util.HashMap;
import java.util.Map;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceException;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
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
  
 /**
    * Thrown when the application calls Query.uniqueResult() and the query 
    * returned more than one result. Unlike all other Hibernate exceptions, this one is recoverable!
  */
  @ExceptionHandler(NonUniqueResultException .class)
  public ResponseEntity<Map<String, String>> handleNonUniqueResultException (
        NoResultException errors) {
    Map<String, String> errorMap = new HashMap<String, String>();
    errorMap.put("hasErrors", "true");
    errorMap.put("errorMessage", errors.getMessage());
    errors.printStackTrace();
    return new ResponseEntity<Map<String, String>>(errorMap, HttpStatus.INTERNAL_SERVER_ERROR);
  }
  
  //Service layer Exceptions
  /**
  *   Exception thrown when a request handler does not support a specific request method.
  */
  @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
  public ResponseEntity<Map<String, String>> handleHttpRequestMethodNotSupportedException(
        HttpRequestMethodNotSupportedException error) {
    Map<String, String> errorMap = new HashMap<String, String>();
    errorMap.put("hasErrors", "true");
    errorMap.put("errorMessage", error.getMessage());
    error.printStackTrace();
    return new ResponseEntity<Map<String, String>>(errorMap, HttpStatus.METHOD_NOT_ALLOWED);
  }
  
  /**
  *  Exception thrown when a client POSTs, PUTs, or PATCHes content of a type not supported   by request handler.
  */
  @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
  public ResponseEntity<Map<String, String>> handleHttpMediaTypeNotSupportedException(
        HttpMediaTypeNotSupportedException error) {
    Map<String, String> errorMap = new HashMap<String, String>();
    errorMap.put("hasErrors", "true");
    errorMap.put("errorMessage", error.getMessage());
    error.printStackTrace();
    return new ResponseEntity<Map<String, String>>(errorMap, HttpStatus.UNSUPPORTED_MEDIA_TYPE);
  }
  
  /**
  *  indicates a missing parameter.
  */
  @ExceptionHandler(MissingServletRequestParameterException.class)
  public ResponseEntity<Map<String, String>> handleMissingServletRequestParameterException(
        MissingServletRequestParameterException error) {
    Map<String, String> errorMap = new HashMap<String, String>();
    errorMap.put("hasErrors", "true");
    errorMap.put("errorMessage", error.getMessage());
    error.printStackTrace();
    return new ResponseEntity<Map<String, String>>(errorMap, HttpStatus.BAD_REQUEST);
  }
  
  /**
  *   Exception thrown when no suitable editor or converter can be found for a bean property.
  */
  @ExceptionHandler(ConversionNotSupportedException.class)
  public ResponseEntity<Map<String, String>> handleConversionNotSupportedException(
        ConversionNotSupportedException error) {
    Map<String, String> errorMap = new HashMap<String, String>();
    errorMap.put("hasErrors", "true");
    errorMap.put("errorMessage", error.getMessage());
    error.printStackTrace();
    return new ResponseEntity<Map<String, String>>(errorMap, HttpStatus.INTERNAL_SERVER_ERROR);
  }
  
  /**
  *  Exception thrown on a type mismatch when trying to set a bean property.
  */
  @ExceptionHandler(TypeMismatchException.class)
  public ResponseEntity<Map<String, String>> handleTypeMismatchException(
        TypeMismatchException error) {
    Map<String, String> errorMap = new HashMap<String, String>();
    errorMap.put("hasErrors", "true");
    errorMap.put("errorMessage", error.getMessage());
    error.printStackTrace();
    return new ResponseEntity<Map<String, String>>(errorMap, HttpStatus.BAD_GATEWAY);
  }
  
  /**
  *  Thrown by HttpMessageConverter implementations when the read method fails..
  */
  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<Map<String, String>> handleHttpMessageNotReadableException(
        HttpMessageNotReadableException error) {
    Map<String, String> errorMap = new HashMap<String, String>();
    errorMap.put("hasErrors", "true");
    errorMap.put("errorMessage", error.getMessage());
    error.printStackTrace();
    return new ResponseEntity<Map<String, String>>(errorMap, HttpStatus.BAD_GATEWAY);
  }
  
  /**
  *  Thrown by HttpMessageConverter implementations when the write method fails.
  */
  @ExceptionHandler(HttpMessageNotWritableException.class)
  public ResponseEntity<Map<String, String>> handleHttpMessageNotWritableException(
        HttpMessageNotWritableException error) {
    Map<String, String> errorMap = new HashMap<String, String>();
    errorMap.put("hasErrors", "true");
    errorMap.put("errorMessage", error.getMessage());
    error.printStackTrace();
    return new ResponseEntity<Map<String, String>>(errorMap, HttpStatus.INTERNAL_SERVER_ERROR);
  }
  
  /**
  * Thrown to indicate that the application has attempted to convert a
    string to one of the numeric types, but that the string does not have the appropriate format.
  */
  @ExceptionHandler(NumberFormatException.class)
  public ResponseEntity<Map<String, String>> handleNumberFormatException(
        NumberFormatException error) {
    Map<String, String> errorMap = new HashMap<String, String>();
    errorMap.put("hasErrors", "true");
    errorMap.put("errorMessage", error.getMessage());
    error.printStackTrace();
    return new ResponseEntity<Map<String, String>>(errorMap, HttpStatus.INTERNAL_SERVER_ERROR);
  }
  
  /**
  * Checked exception used to signal fatal problems with mapping of content.
  * One additional feature is the ability to denote relevant path of references (during serialization/deserialization) to help in troubleshooting.
  */
  @ExceptionHandler(JsonMappingException.class)
  public ResponseEntity<Map<String, String>> handleJsonMappingException(
        JsonMappingException error) {
    Map<String, String> errorMap = new HashMap<String, String>();
    errorMap.put("hasErrors", "true");
    errorMap.put("errorMessage", error.getMessage());
    error.printStackTrace();
    return new ResponseEntity<Map<String, String>>(errorMap, HttpStatus.INTERNAL_SERVER_ERROR);
  }
  

}
