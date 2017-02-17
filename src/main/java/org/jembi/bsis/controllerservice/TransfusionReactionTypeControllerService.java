package org.jembi.bsis.controllerservice;

import java.util.List;

import org.jembi.bsis.factory.TransfusionReactionTypeFactory;
import org.jembi.bsis.model.transfusion.TransfusionReactionType;
import org.jembi.bsis.repository.TransfusionReactionTypeRepository;
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

  public List<TransfusionReactionTypeManagementViewModel> getTransfusionReactionTypes() {
    List<TransfusionReactionType> transfusionReactionTypes =
        transfusionReactionTypeRepository.getAllTransfusionReactionTypes(true);
    return transfusionReactionTypeFactory.createTransfusionReactionTypeManagementViewModels(transfusionReactionTypes);
  }
}
