package org.jembi.bsis.service;

import org.jembi.bsis.model.component.Component;
import org.jembi.bsis.model.returnform.ReturnForm;
import org.jembi.bsis.model.returnform.ReturnStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class ReturnFormConstraintChecker {
  
  @Autowired
  private ComponentConstraintChecker componentConstraintChecker;
  
  public boolean canEdit(ReturnForm returnForm) {
    return returnForm.getStatus() == ReturnStatus.CREATED;
  }
  
  public boolean canReturn(ReturnForm returnForm) {
    return returnForm.getStatus() == ReturnStatus.CREATED && !returnForm.getComponents().isEmpty();
  }
  
  public boolean canDiscard(ReturnForm returnForm) {

    if (returnForm.getStatus() != ReturnStatus.RETURNED) {
      // Can't discard components for a return form which has not been returned
      return false;
    }

    for (Component component : returnForm.getComponents()) {
      if (componentConstraintChecker.canDiscard(component)) {
        // If any components can be discarded then so can the return form
        return true;
      }
    }

    return false;
  }

  public boolean canDelete(ReturnForm returnForm) {
    return returnForm.getStatus() == ReturnStatus.CREATED;
  }
}
