package service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import model.returnform.ReturnForm;
import model.returnform.ReturnStatus;
import repository.ReturnFormRepository;

@Service
@Transactional
public class ReturnFormCRUDService {

  @Autowired
  private ReturnFormRepository returnFormRepository;

  public ReturnForm createReturnForm(ReturnForm returnForm) {
    returnForm.setStatus(ReturnStatus.CREATED);
    returnFormRepository.save(returnForm);
    return returnForm;
  }

}
