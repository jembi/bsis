package model.requesttype;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import repository.RequestTypeRepository;

@Component
public class RequestTypeExistsConstraintValidator implements
    ConstraintValidator<RequestTypeExists, RequestType> {

  @Autowired
  private RequestTypeRepository requestTypeRepository;

  public RequestTypeExistsConstraintValidator() {
  }
  
  @Override
  public void initialize(RequestTypeExists constraint) {
  }

  public boolean isValid(RequestType target, ConstraintValidatorContext context) {

   if (target == null)
     return true;

   try {
    if (requestTypeRepository.isRequestTypeValid(target.getRequestType()))
      return true;
   } catch (Exception e) {
    e.printStackTrace();
   }
   return false;
  }

  public void setRequestTypeRepository(RequestTypeRepository requestTypeRepository) {
    this.requestTypeRepository = requestTypeRepository;
  }
}