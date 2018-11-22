package org.jembi.bsis.controllerservice;

import java.util.List;
import java.util.UUID;

import org.jembi.bsis.backingform.TransfusionReactionTypeBackingForm;
import org.jembi.bsis.factory.TransfusionReactionTypeFactory;
import org.jembi.bsis.model.transfusion.TransfusionReactionType;
import org.jembi.bsis.repository.TransfusionReactionTypeRepository;
import org.jembi.bsis.service.TransfusionReactionTypeCRUDService;
import org.jembi.bsis.viewmodel.TransfusionReactionTypeManagementViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class TransfusionReactionTypeControllerService {

  @Autowired
  private TransfusionReactionTypeFactory transfusionReactionTypeFactory;

  @Autowired
  private TransfusionReactionTypeRepository transfusionReactionTypeRepository;

  @Autowired
  private TransfusionReactionTypeCRUDService transfusionReactionTypeCRUDService;

  public List<TransfusionReactionTypeManagementViewModel> getTransfusionReactionTypes() {
    List<TransfusionReactionType> transfusionReactionTypes =
        transfusionReactionTypeRepository.getAllTransfusionReactionTypes(true);
    return transfusionReactionTypeFactory.createTransfusionReactionTypeManagementViewModels(transfusionReactionTypes);
  }

  public TransfusionReactionTypeManagementViewModel createTransfusionReactionType(TransfusionReactionTypeBackingForm backingForm) {
     TransfusionReactionType transfusionReactionType = transfusionReactionTypeFactory.createEntity(backingForm);
     transfusionReactionType = transfusionReactionTypeCRUDService.createTransfusionReactionType(transfusionReactionType);
     return transfusionReactionTypeFactory.createTransfusionReactionTypeManagementViewModel(transfusionReactionType);
  }
  
  public TransfusionReactionTypeManagementViewModel getTransfusionReactionType(UUID id) {
     TransfusionReactionType transfusionReactionType = transfusionReactionTypeRepository.findById(id);
     return transfusionReactionTypeFactory.createTransfusionReactionTypeManagementViewModel(transfusionReactionType);
  }
  
  public TransfusionReactionTypeManagementViewModel updateTransfusionReactionType(TransfusionReactionTypeBackingForm backingForm) {
    TransfusionReactionType transfusionReactionType = transfusionReactionTypeFactory.createEntity(backingForm);
    transfusionReactionType = transfusionReactionTypeCRUDService.updateTransfusionReactionType(transfusionReactionType);  
    return transfusionReactionTypeFactory.createTransfusionReactionTypeManagementViewModel(transfusionReactionType);
  }
}