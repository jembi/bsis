package controllerservice;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import backingform.ReturnFormBackingForm;
import factory.ReturnFormFactory;
import model.returnform.ReturnForm;
import repository.ReturnFormRepository;
import service.ReturnFormCRUDService;
import viewmodel.ReturnFormFullViewModel;
import viewmodel.ReturnFormViewModel;

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
  
  public ReturnFormFullViewModel updateOrderForm(ReturnFormBackingForm backingForm) {
    ReturnForm returnForm = returnFormFactory.createEntity(backingForm);
    ReturnForm updatedReturnForm = returnFormCRUDService.updateReturnForm(returnForm);
    return returnFormFactory.createFullViewModel(updatedReturnForm);
  }

  public ReturnFormFullViewModel findById(Long id) {
    ReturnForm returnForm = returnFormRepository.findById(id);
    return returnFormFactory.createFullViewModel(returnForm);
  }

}
