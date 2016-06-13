package service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import model.returnform.ReturnForm;
import model.returnform.ReturnStatus;

@Transactional(readOnly = true)
@Service
public class ReturnFormConstraintChecker {
  
  public boolean canEdit(ReturnForm returnForm) {
    return returnForm.getStatus() == ReturnStatus.CREATED;
  }
  
  public boolean canReturn(ReturnForm returnForm) {
    return returnForm.getStatus() == ReturnStatus.CREATED && !returnForm.getComponents().isEmpty();
  }
}
