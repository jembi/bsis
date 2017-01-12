package org.jembi.bsis.controller;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceException;

import org.apache.log4j.Logger;
import org.jembi.bsis.backingform.validator.BaseValidatorRuntimeException;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;

@ControllerAdvice
public class GlobalControllerExceptionHandler {

  private static final Logger LOGGER = Logger.getLogger(GlobalControllerExceptionHandler.class);

  /**
   * Exception to be thrown when validation on an argument annotated with @Valid fails.
   */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Map<String, Object>> handleMethodArgumentNotValidException(
      MethodArgumentNotValidException errors) {
    
    if (LOGGER.isDebugEnabled()) {
      LOGGER.debug(errors.getMessage(), errors);
    } else {
      LOGGER.error(errors.getMessage());
    }
    
    Map<String, Object> errorMap = new HashMap<String, Object>();
    errorMap.put("hasErrors", "true");
    errorMap.put("developerMessage", "There are validation issues, please provide valid inputs");
    errorMap.put("userMessage", "Please provide valid inputs");
    errorMap.put("moreInfo", errors.getMessage());
    errorMap.put("errorCode", HttpStatus.BAD_REQUEST);
    Map<String, List<Map<String, String>>> fieldErrors = new HashMap<>();
    for (FieldError error : errors.getBindingResult().getFieldErrors()) {
      errorMap.put(error.getField(), error.getDefaultMessage());
      
      // Add this error to the list of errors for this field
      List<Map<String, String>> errorsForField = fieldErrors.get(error.getField());
      if (errorsForField == null) {
        errorsForField = new ArrayList<>();
      }
      
      Map<String, String> fieldError = new HashMap<>();
      fieldError.put("code", error.getCode());
      fieldError.put("message", error.getDefaultMessage());
      errorsForField.add(fieldError);

      fieldErrors.put(error.getField(), errorsForField);
    }
    errorMap.put("fieldErrors", fieldErrors);

    return new ResponseEntity<Map<String, Object>>(errorMap, HttpStatus.BAD_REQUEST);
  }

  /**
   * Exception that is thrown when a Validator encounters an unhandled Exception
   */
  @ExceptionHandler(BaseValidatorRuntimeException.class)
  public ResponseEntity<Map<String, Object>> handleBaseValidatorRuntimeException(BaseValidatorRuntimeException e) {
    
    LOGGER.error(e.getMessage(), e);
    
    Map<String, Object> errorMap = new HashMap<String, Object>();
    errorMap.put("hasErrors", "true");
    errorMap.put("developerMessage", e.getMessage());
    errorMap.put("userMessage", "");
    errorMap.put("moreInfo", e.getStackTrace()[0]);
    errorMap.put("errorCode", HttpStatus.INTERNAL_SERVER_ERROR);
    for (FieldError error : e.getErrors().getFieldErrors()) {
      errorMap.put(error.getField(), error.getDefaultMessage());
    }
    return new ResponseEntity<Map<String, Object>>(errorMap, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  /**
   * thrown at flush or commit time for detached entities
   */
  @ExceptionHandler(PersistenceException.class)
  public ResponseEntity<Map<String, Object>> handlePersistenceException(PersistenceException error) {
    
    LOGGER.error(error.getMessage(), error);
    
    Map<String, Object> errorMap = new HashMap<String, Object>();
    errorMap.put("hasErrors", "true");
    errorMap.put("developerMessage", error.getMessage());
    errorMap.put("userMessage", "");
    errorMap.put("moreInfo", "");
    errorMap.put("errorCode", HttpStatus.INTERNAL_SERVER_ERROR);

    return new ResponseEntity<Map<String, Object>>(errorMap, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  /**
   * Thrown by the persistence provider when getSingleResult() is executed on a query and there is
   * no result to return.
   */
  @ExceptionHandler(NoResultException.class)
  public ResponseEntity<Map<String, Object>> handleNoResultException(NoResultException error) {
    
    LOGGER.error(error.getMessage());
    LOGGER.trace(error);
    
    Map<String, Object> errorMap = new HashMap<String, Object>();
    errorMap.put("hasErrors", "true");
    errorMap.put("developerMessage", error.getMessage());
    errorMap.put("userMessage", "");
    errorMap.put("moreInfo", error.getStackTrace()[0]);
    errorMap.put("errorCode", HttpStatus.NOT_FOUND);

    return new ResponseEntity<Map<String, Object>>(errorMap, HttpStatus.NOT_FOUND);
  }

  /**
   * Thrown to indicate that a method has been passed an illegal or inappropriate argument.
   */
  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<Map<String, Object>> handleIllegalArgumentException(IllegalArgumentException error) {
    
    if (LOGGER.isDebugEnabled()) {
      LOGGER.debug(error.getMessage(), error);
    } else {
      LOGGER.error(error.getMessage());
    }
    
    Map<String, Object> errorMap = new HashMap<>();
    errorMap.put("hasErrors", "true");
    errorMap.put("developerMessage", error.getMessage());
    errorMap.put("userMessage", error.getMessage());
    errorMap.put("moreInfo", error.getStackTrace()[0]);
    errorMap.put("errorCode", HttpStatus.BAD_REQUEST);

    return new ResponseEntity<Map<String, Object>>(errorMap, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(IllegalStateException.class)
  public ResponseEntity<Map<String, Object>> handleIllegalStateException(IllegalStateException error) {
    
    if (LOGGER.isDebugEnabled()) {
      LOGGER.debug(error.getMessage(), error);
    } else {
      LOGGER.error(error.getMessage());
    }
    
    Map<String, Object> errorMap = new HashMap<String, Object>();
    errorMap.put("hasErrors", "true");
    errorMap.put("developerMessage", error.getMessage());
    errorMap.put("userMessage", "");
    errorMap.put("moreInfo", error.getStackTrace()[0]);
    errorMap.put("errorCode", HttpStatus.CONFLICT);

    return new ResponseEntity<Map<String, Object>>(errorMap, HttpStatus.CONFLICT);
  }

  /**
   * Thrown when the application calls Query.uniqueResult() and the query returned more than one
   * result. Unlike all other Hibernate exceptions, this one is recoverable!
   */
  @ExceptionHandler(NonUniqueResultException.class)
  public ResponseEntity<Map<String, Object>> handleNonUniqueResultException(NonUniqueResultException error) {
    
    LOGGER.error(error.getMessage(), error);
    
    Map<String, Object> errorMap = new HashMap<String, Object>();
    errorMap.put("hasErrors", "true");
    errorMap.put("developerMessage", "Fetched more than one object/entity but expected only one");
    errorMap.put("userMessage", "");
    errorMap.put("moreInfo", error.getMessage());
    errorMap.put("errorCode", HttpStatus.INTERNAL_SERVER_ERROR);

    return new ResponseEntity<Map<String, Object>>(errorMap, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  //Service layer Exceptions

  /**
   * Exception thrown when a request handler does not support a specific request method.
   */
  @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
  public ResponseEntity<Map<String, Object>> handleHttpRequestMethodNotSupportedException(
      HttpRequestMethodNotSupportedException error) {
    
    String errorMessage = error.getMethod() + " supports only " + error.getSupportedHttpMethods()
        + ", change the request type to " + error.getSupportedHttpMethods();

    if (LOGGER.isDebugEnabled()) {
      LOGGER.debug(errorMessage, error);
    } else {
      LOGGER.error(errorMessage);
    }
    
    Map<String, Object> errorMap = new HashMap<String, Object>();
    errorMap.put("hasErrors", "true");
    errorMap.put("developerMessage", errorMessage);
    errorMap.put("userMessage", "");
    errorMap.put("moreInfo", error.getMessage());
    errorMap.put("errorCode", HttpStatus.METHOD_NOT_ALLOWED);

    return new ResponseEntity<Map<String, Object>>(errorMap, HttpStatus.METHOD_NOT_ALLOWED);
  }

  /**
   * Exception thrown when a client POSTs, PUTs, or PATCHes content of a type not supported   by
   * request handler.
   */
  @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
  public ResponseEntity<Map<String, Object>> handleHttpMediaTypeNotSupportedException(
      HttpMediaTypeNotSupportedException error) {
    
    String errorMessage = "the requested content type [" + error.getContentType() + "] is not supported";

    if (LOGGER.isDebugEnabled()) {
      LOGGER.debug(errorMessage, error);
    } else {
      LOGGER.error(errorMessage);
    }
    
    Map<String, Object> errorMap = new HashMap<String, Object>();
    errorMap.put("hasErrors", "true");
    errorMap.put("developerMessage", errorMessage);
    errorMap.put("userMessage", "");
    errorMap.put("moreInfo", error.getMessage());
    errorMap.put("errorCode", HttpStatus.UNSUPPORTED_MEDIA_TYPE);

    return new ResponseEntity<Map<String, Object>>(errorMap, HttpStatus.UNSUPPORTED_MEDIA_TYPE);
  }

  /**
   * indicates a missing parameter.
   */
  @ExceptionHandler(MissingServletRequestParameterException.class)
  public ResponseEntity<Map<String, Object>> handleMissingServletRequestParameterException(
      MissingServletRequestParameterException error) {
    
    String errorMessage = "the request parameter ["
        + error.getParameterName() + "] of type [" + error.getParameterType() + "] is missing";
    
    if (LOGGER.isDebugEnabled()) {
      LOGGER.debug(errorMessage, error);
    } else {
      LOGGER.error(errorMessage);
    }
        
    Map<String, Object> errorMap = new HashMap<String, Object>();
    errorMap.put("hasErrors", "true");
    errorMap.put("developerMessage", errorMessage);
    errorMap.put("userMessage", "please provide all the values");
    errorMap.put("moreInfo", error.getMessage());
    errorMap.put("errorCode", HttpStatus.BAD_REQUEST);

    return new ResponseEntity<Map<String, Object>>(errorMap, HttpStatus.BAD_REQUEST);
  }

  /**
   * Exception thrown when no suitable editor or converter can be found for a bean property.
   */
  @ExceptionHandler(ConversionNotSupportedException.class)
  public ResponseEntity<Map<String, Object>> handleConversionNotSupportedException(
      ConversionNotSupportedException error) {
    
    String errorMessage = error.getPropertyName() + "with value " + error.getValue()
        + "is not compatable to" + error.getRequiredType();
    
    if (LOGGER.isDebugEnabled()) {
      LOGGER.debug(errorMessage, error);
    } else {
      LOGGER.error(errorMessage);
    }
    
    Map<String, Object> errorMap = new HashMap<String, Object>();
    errorMap.put("hasErrors", "true");
    errorMap.put("errorMessage", errorMessage);
    errorMap.put("userMessage", "Please check the input with value " + error.getValue());
    errorMap.put("moreInfo", error.getMessage());
    errorMap.put("errorCode", HttpStatus.INTERNAL_SERVER_ERROR);

    return new ResponseEntity<Map<String, Object>>(errorMap, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  /**
   * Thrown to indicate that the application has attempted to convert a string to one of the numeric
   * types, but that the string does not have the appropriate format.
   */
  @ExceptionHandler(NumberFormatException.class)
  public ResponseEntity<Map<String, Object>> handleNumberFormatException(
      NumberFormatException error) {
    
    LOGGER.error(error.getMessage(), error);
    
    Map<String, Object> errorMap = new HashMap<String, Object>();
    errorMap.put("hasErrors", "true");
    errorMap.put("developerMessage", error.getMessage());
    errorMap.put("userMessage", "");
    errorMap.put("moreInfo", error.getStackTrace()[0]);
    errorMap.put("errorCode", HttpStatus.INTERNAL_SERVER_ERROR);

    return new ResponseEntity<Map<String, Object>>(errorMap, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  /**
   * Exception thrown on a type mismatch when trying to set a bean property.
   */
  @ExceptionHandler(TypeMismatchException.class)
  public ResponseEntity<Map<String, Object>> handleTypeMismatchException(TypeMismatchException error) {

    String errorMessage = "Value '" + error.getValue() + "' is not compatable with " + error.getRequiredType();
    
    if (LOGGER.isDebugEnabled()) {
      LOGGER.debug(errorMessage, error);
    } else {
      LOGGER.error(errorMessage);
    }
    
    Map<String, Object> errorMap = new HashMap<String, Object>();
    errorMap.put("hasErrors", "true");
    errorMap.put("developerMessage", errorMessage);
    errorMap.put("userMessage", "Please check the input value '" + error.getValue() + "'");
    errorMap.put("moreInfo", error.getMessage());
    errorMap.put("errorCode", HttpStatus.BAD_REQUEST);

    return new ResponseEntity<Map<String, Object>>(errorMap, HttpStatus.BAD_REQUEST);
  }

  /**
   * Specialized sub-class of JsonMappingException that is used when the underlying problem appears
   * to be that of bad formatting of a value to deserialize.
   */
  @ExceptionHandler(InvalidFormatException.class)
  public ResponseEntity<Map<String, Object>> handleInvalidFormatException(InvalidFormatException error) {

    String errorMessage = error.getValue() + "cannot be converted to " + error.getTargetType() +
        "change '" + error.getValue() + "' To match target type" + error.getTargetType();
    
    if (LOGGER.isDebugEnabled()) {
      LOGGER.debug(errorMessage, error);
    } else {
      LOGGER.error(errorMessage);
    }
    
    Map<String, Object> errorMap = new HashMap<String, Object>();
    errorMap.put("hasErrors", "true");
    errorMap.put("developerMessage", errorMessage);
    errorMap.put("userMessage", "Please enter a correct value in place of '" + error.getValue() + "'");
    errorMap.put("moreInfo", error.getMessage());
    errorMap.put("errorCode", HttpStatus.BAD_REQUEST);

    return new ResponseEntity<Map<String, Object>>(errorMap, HttpStatus.BAD_REQUEST);
  }


  /**
   * Thrown when an application attempts to use null in a case where an object is required. These
   * include: Calling the instance method of a null object. Accessing or modifying the field of a
   * null object. Taking the length of null as if it were an array. Accessing or modifying the slots
   * of null as if it were an array. Throwing null as if it were a Throwable value.
   */
  @ExceptionHandler(NullPointerException.class)
  public ResponseEntity<Map<String, Object>> handleNullPointerException(NullPointerException error) {

    LOGGER.error(error.getMessage(), error);
    
    Map<String, Object> errorMap = new HashMap<String, Object>();
    errorMap.put("hasErrors", "true");
    errorMap.put("developerMessage", "Null Pointer Exception: " + error.getStackTrace()[0]);
    errorMap.put("userMessage", "");
    errorMap.put("moreInfo", error.getStackTrace()[0]);
    errorMap.put("errorCode", HttpStatus.INTERNAL_SERVER_ERROR);

    return new ResponseEntity<Map<String, Object>>(errorMap, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  /**
   * Thrown by HttpMessageConverter implementations when the read method fails..
   */
  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<Map<String, Object>> handleHttpMessageNotReadableException(HttpMessageNotReadableException error) {

    String errorMessage = "Error parsing Json request to corresponding object. Error: " + error.getMessage();
    
    if (LOGGER.isDebugEnabled()) {
      LOGGER.debug(errorMessage, error);
    } else {
      LOGGER.error(errorMessage);
    }
    
    Map<String, Object> errorMap = new HashMap<String, Object>();
    errorMap.put("hasErrors", "true");
    errorMap.put("developerMessage", "Error parsing  Json request to corresponding object."
        + " please form the correct JSON String ");
    errorMap.put("userMessage", "");
    errorMap.put("moreInfo", error.getMessage());
    errorMap.put("errorCode", HttpStatus.BAD_REQUEST);

    return new ResponseEntity<Map<String, Object>>(errorMap, HttpStatus.BAD_REQUEST);
  }

  /**
   * Thrown by HttpMessageConverter implementations when the write method fails.
   */
  @ExceptionHandler(HttpMessageNotWritableException.class)
  public ResponseEntity<Map<String, Object>> handleHttpMessageNotWritableException(HttpMessageNotWritableException error) {
    
    String errorMessage = "Error parsing Object to JSON response";

    if (LOGGER.isDebugEnabled()) {
      LOGGER.debug(error.getMessage(), error);
    } else {
      LOGGER.error(errorMessage + ". Error: " + error.getMessage());
    }
    
    Map<String, Object> errorMap = new HashMap<String, Object>();
    errorMap.put("hasErrors", "true");
    errorMap.put("developerMessage", errorMessage);
    errorMap.put("userMessage", "");
    errorMap.put("moreInfo", error.getMessage());
    errorMap.put("errorCode", HttpStatus.INTERNAL_SERVER_ERROR);

    return new ResponseEntity<Map<String, Object>>(errorMap, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  /**
   * Signals that an error has been reached unexpectedly while parsing.
   */
  @ExceptionHandler(ParseException.class)
  public ResponseEntity<Map<String, Object>> handleParseException(ParseException error) {
    
    String errorMessage = error.getMessage() + "at position " + error.getErrorOffset();

    LOGGER.error(errorMessage, error);

    Map<String, Object> errorMap = new HashMap<String, Object>();
    errorMap.put("hasErrors", "true");
    errorMap.put("developerMessage", errorMessage);
    errorMap.put("userMessage", error.getMessage());
    errorMap.put("moreInfo", error.getMessage());
    errorMap.put("errorCode", HttpStatus.BAD_REQUEST);

    return new ResponseEntity<Map<String, Object>>(errorMap, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(AccessDeniedException.class)
  public ResponseEntity<Map<String, Object>> handleAccessDeniedException(AccessDeniedException error) {
    
    LOGGER.error("Access denied. Error: " + error.getMessage());
    LOGGER.trace(error);
    
    Map<String, Object> errorMap = new HashMap<String, Object>();
    errorMap.put("hasErrors", "true");
    errorMap.put("developerMessage", "Access denied");
    errorMap.put("userMessage", error.getMessage());
    errorMap.put("moreInfo", error.getMessage());
    errorMap.put("errorCode", HttpStatus.FORBIDDEN);

    return new ResponseEntity<Map<String, Object>>(errorMap, HttpStatus.FORBIDDEN);
  }
  
  @ExceptionHandler(IOException.class)
  public ResponseEntity<Map<String, Object>> handleIOException(IOException ex) {
    
    LOGGER.error(ex.getMessage(), ex);
    
    Map<String, Object> errorMap = new HashMap<String, Object>();
    errorMap.put("hasErrors", "true");
    errorMap.put("developerMessage", "An IOException occurred while performing the request.");
    errorMap.put("userMessage", "An error occurred while reading or writing a file or data. Please try again later.");
    errorMap.put("moreInfo", ex.getMessage());
    errorMap.put("errorCode", HttpStatus.INTERNAL_SERVER_ERROR);

    return new ResponseEntity<>(errorMap, HttpStatus.INTERNAL_SERVER_ERROR);
  }

}
