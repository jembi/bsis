package controllerservice;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import model.returnform.ReturnForm;
import model.returnform.ReturnStatus;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import repository.ReturnFormRepository;
import service.ReturnFormCRUDService;
import viewmodel.ReturnFormFullViewModel;
import viewmodel.ReturnFormViewModel;
import backingform.ReturnFormBackingForm;
import factory.ReturnFormFactory;

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

  public ReturnFormFullViewModel findById(Long id) {
    ReturnForm returnForm = returnFormRepository.findById(id);
    return returnFormFactory.createFullViewModel(returnForm);
  }

  public List<ReturnFormViewModel> findReturnForms(Date returnDateFrom, Date returnDateTo, 
      Long returnedFromId, Long returnedToId, ReturnStatus status) {

    List<ReturnForm> returnForms = returnFormRepository.findReturnForms(returnDateFrom, returnDateTo, 
        returnedFromId, returnedToId, status);
    
    return returnFormFactory.createViewModels(returnForms);
  }
}
