package backingform.validator;

/**
 * Class used to wrap all Exceptions that are thrown during backing form validation
 */
public class BaseValidatorRuntimeException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  public BaseValidatorRuntimeException(String message, Throwable cause) {
    super(message, cause);
  }

  public BaseValidatorRuntimeException(Throwable cause) {
    super(cause);
  }
}
