package org.jembi.bsis.service;

import java.util.UUID;

import javax.transaction.Transactional;

import org.jembi.bsis.model.component.Component;
import org.jembi.bsis.model.returnform.ReturnForm;
import org.jembi.bsis.model.returnform.ReturnStatus;
import org.jembi.bsis.repository.ReturnFormRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class ReturnFormCRUDService {

  @Autowired
  private ReturnFormRepository returnFormRepository;

  @Autowired
  private ComponentCRUDService componentCRUDService;
  
  @Autowired
  private ReturnFormConstraintChecker returnFormConstraintChecker;

  public ReturnForm createReturnForm(ReturnForm returnForm) {
    returnForm.setStatus(ReturnStatus.CREATED);
    returnFormRepository.save(returnForm);
    return returnForm;
  }

  public ReturnForm updateReturnForm(ReturnForm updatedReturnForm) {
    ReturnForm existingReturnForm = returnFormRepository.findById(updatedReturnForm.getId());
    
    if (!returnFormConstraintChecker.canEdit(existingReturnForm)) {
      throw new IllegalStateException("Cannot edit ReturnForm");
    }

    // If the form is being returned then update each component
    if (updatedReturnForm.getStatus() == ReturnStatus.RETURNED) {

      if (!returnFormConstraintChecker.canReturn(existingReturnForm)) {
        throw new IllegalStateException("Cannot return ReturnForm");
      }
      
      for (Component component : updatedReturnForm.getComponents()) {
        componentCRUDService.returnComponent(component, updatedReturnForm.getReturnedTo());
      }
    }

    existingReturnForm.setReturnDate(updatedReturnForm.getReturnDate());
    existingReturnForm.setStatus(updatedReturnForm.getStatus());
    existingReturnForm.setReturnedFrom(updatedReturnForm.getReturnedFrom());
    existingReturnForm.setReturnedTo(updatedReturnForm.getReturnedTo());
    existingReturnForm.setComponents(updatedReturnForm.getComponents());
    return returnFormRepository.update(existingReturnForm);
  }
  
  public void deleteReturnForm(UUID returnFormId) {
    ReturnForm existingReturnForm = returnFormRepository.findById(returnFormId);
    
    if (!returnFormConstraintChecker.canDelete(existingReturnForm)) {
      throw new IllegalStateException("Cannot delete ReturnForm");
    }
    existingReturnForm.setIsDeleted(true);
    returnFormRepository.update(existingReturnForm);
  }

}