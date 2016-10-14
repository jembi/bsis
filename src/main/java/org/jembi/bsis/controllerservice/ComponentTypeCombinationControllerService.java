package org.jembi.bsis.controllerservice;

import java.util.List;

import javax.transaction.Transactional;

import org.jembi.bsis.factory.ComponentTypeCombinationFactory;
import org.jembi.bsis.model.componenttype.ComponentTypeCombination;
import org.jembi.bsis.repository.ComponentTypeCombinationRepository;
import org.jembi.bsis.viewmodel.ComponentTypeCombinationViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class ComponentTypeCombinationControllerService {

  @Autowired
  private ComponentTypeCombinationRepository componentTypeCombinationRepository;

  @Autowired
  private ComponentTypeCombinationFactory componentTypeCombinationFactory;

  public List<ComponentTypeCombinationViewModel> getComponentTypeCombinations(boolean includeDeleted) {
    List<ComponentTypeCombination> componentTypeCombinations;

    if (includeDeleted) {
      componentTypeCombinations = componentTypeCombinationRepository.getAllComponentTypeCombinations(includeDeleted);
    } else {
      componentTypeCombinations = componentTypeCombinationRepository.getAllComponentTypeCombinations(includeDeleted);
    }

    return componentTypeCombinationFactory.createViewModels(componentTypeCombinations);
  }

  public ComponentTypeCombinationViewModel findComponentTypeCombinationById(long id){
    return  componentTypeCombinationFactory.createViewModel(componentTypeCombinationRepository.findComponentTypeCombinationById(id));
  }
}
