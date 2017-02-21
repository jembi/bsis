package org.jembi.bsis.controllerservice;

import org.jembi.bsis.backingform.TransfusionBackingForm;
import org.jembi.bsis.factory.TransfusionFactory;
import org.jembi.bsis.model.transfusion.Transfusion;
import org.jembi.bsis.service.TransfusionCRUDService;
import org.jembi.bsis.viewmodel.TransfusionViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class TransfusionControllerService {

  @Autowired
  private TransfusionCRUDService transfusionCRUDService;
  @Autowired
  private TransfusionFactory transfusionFactory;
  
  public TransfusionViewModel createTransfusionForm(TransfusionBackingForm backingForm) {
    Transfusion entity = transfusionFactory.createEntity(backingForm);
    Long transfusedComponentTypeId = null;
    if (backingForm.getComponentType() != null) {
      transfusedComponentTypeId = backingForm.getComponentType().getId();
    }
    entity = transfusionCRUDService.createTransfusion(entity, transfusedComponentTypeId);
    return transfusionFactory.createViewModel(entity);
  }
}