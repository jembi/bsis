package org.jembi.bsis.backingform.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;

/**
 * Class used to wrap all Exceptions that are thrown during backing form validation
 */
public class BaseValidatorRuntimeException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  private Errors errors;

  public BaseValidatorRuntimeException(String message, Errors errors, Throwable cause) {
    super(message, cause);
    this.errors = errors;
  }

  public BaseValidatorRuntimeException(Throwable cause) {
    super(cause);
  }

  /**
   * Return the results of the failed validation.
   */
  public Errors getErrors() {
    return this.errors;
  }

  @Override
  public String getMessage() {
    StringBuilder sb = new StringBuilder(getMessage()).append(".\n")
        .append(this.errors.getErrorCount()).append(" error(s): ");
    for (ObjectError error : this.errors.getAllErrors()) {
      sb.append("[").append(error).append("] ");
    }
    return sb.toString();
  }
}
