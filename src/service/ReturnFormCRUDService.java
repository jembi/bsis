package service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import model.component.Component;
import model.returnform.ReturnForm;
import model.returnform.ReturnStatus;
import repository.ReturnFormRepository;

@Service
@Transactional
public class ReturnFormCRUDService {

  @Autowired
  private ReturnFormRepository returnFormRepository;

  @Autowired
  private ComponentReturnService componentReturnService;
  
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
      
      if (!returnFormConstraintChecker.canReturn(updatedReturnForm)) {
        throw new IllegalStateException("Cannot return ReturnForm");
      }
      
      for (Component component : updatedReturnForm.getComponents()) {
        componentReturnService.returnComponent(component, updatedReturnForm.getReturnedTo());
      }
    }

    existingReturnForm.setReturnDate(updatedReturnForm.getReturnDate());
    existingReturnForm.setStatus(updatedReturnForm.getStatus());
    existingReturnForm.setReturnedFrom(updatedReturnForm.getReturnedFrom());
    existingReturnForm.setReturnedTo(updatedReturnForm.getReturnedTo());
    existingReturnForm.setComponents(updatedReturnForm.getComponents());
    return returnFormRepository.update(existingReturnForm);
  }

}
