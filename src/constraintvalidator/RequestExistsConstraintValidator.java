package constraintvalidator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import model.request.Request;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import repository.RequestRepository;

@Component
public class RequestExistsConstraintValidator implements
    ConstraintValidator<RequestExists, Request> {

  @Autowired
  private RequestRepository requestRepository;

  public RequestExistsConstraintValidator() {
  }
  
  @Override
  public void initialize(RequestExists constraint) {
  }

  public boolean isValid(Request target, ConstraintValidatorContext context) {

   if (target == null)
     return true;

   try {

      Request request = null;

      if (target.getId() != null) {
        request = requestRepository.findRequestById(target.getId());
      }
      else if (target.getRequestNumber() != null) {

        if (target.getRequestNumber().isEmpty())
          return true;

        request = requestRepository.findRequestByRequestNumber(target.getRequestNumber());
      }
      if (request != null) {
        return true;
      }
    } catch (Exception e) {
       e.printStackTrace();
    }
    return false;
  }

  public void setRequestRepository(RequestRepository requestRepository) {
    this.requestRepository = requestRepository;
  }
}