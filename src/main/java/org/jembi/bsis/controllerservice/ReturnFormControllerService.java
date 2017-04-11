package org.jembi.bsis.controllerservice;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.transaction.Transactional;

import org.jembi.bsis.backingform.ReturnFormBackingForm;
import org.jembi.bsis.factory.ReturnFormFactory;
import org.jembi.bsis.model.returnform.ReturnForm;
import org.jembi.bsis.model.returnform.ReturnStatus;
import org.jembi.bsis.repository.ReturnFormRepository;
import org.jembi.bsis.service.ReturnFormCRUDService;
import org.jembi.bsis.viewmodel.ReturnFormFullViewModel;
import org.jembi.bsis.viewmodel.ReturnFormViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class ReturnFormControllerService {

  @Autowired
  private ReturnFormFactory returnFormFactory;

  @Autowired
  private ReturnFormCRUDService returnFormCRUDService;

  @Autowired
  private ReturnFormRepository returnFormRepository;

  public ReturnFormViewModel createReturnForm(ReturnFormBackingForm backingForm) {
    ReturnForm returnForm = returnFormFactory.createEntity(backingForm);
    returnForm = returnFormCRUDService.createReturnForm(returnForm);
    return returnFormFactory.createViewModel(returnForm);
  }
  
  public ReturnFormFullViewModel updateReturnForm(ReturnFormBackingForm backingForm) {
    ReturnForm returnForm = returnFormFactory.createEntity(backingForm);
    ReturnForm updatedReturnForm = returnFormCRUDService.updateReturnForm(returnForm);
    return returnFormFactory.createFullViewModel(updatedReturnForm);
  }

  public ReturnFormFullViewModel findById(UUID id) {
    ReturnForm returnForm = returnFormRepository.findById(id);
    return returnFormFactory.createFullViewModel(returnForm);
  }

  public List<ReturnFormViewModel> findReturnForms(Date returnDateFrom, Date returnDateTo, 
      UUID returnedFromId, UUID returnedToId, ReturnStatus status) {

    List<ReturnForm> returnForms = returnFormRepository.findReturnForms(returnDateFrom, returnDateTo, 
        returnedFromId, returnedToId, status);
    
    return returnFormFactory.createViewModels(returnForms);
  }
  
  public void deleteReturnForm(UUID returnFormId) {
    returnFormCRUDService.deleteReturnForm(returnFormId);
  }
}