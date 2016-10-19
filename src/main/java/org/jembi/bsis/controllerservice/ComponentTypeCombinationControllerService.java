package org.jembi.bsis.controllerservice;

import java.util.List;

import javax.transaction.Transactional;

import org.jembi.bsis.factory.ComponentTypeCombinationFactory;
import org.jembi.bsis.factory.ComponentTypeFactory;
import org.jembi.bsis.repository.ComponentTypeCombinationRepository;
import org.jembi.bsis.repository.ComponentTypeRepository;
import org.jembi.bsis.viewmodel.ComponentTypeCombinationViewModel;
import org.jembi.bsis.viewmodel.ComponentTypeViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class ComponentTypeCombinationControllerService {

  @Autowired
  private ComponentTypeCombinationRepository componentTypeCombinationRepository;

  @Autowired
  private ComponentTypeCombinationFactory componentTypeCombinationFactory;

  @Autowired
  private ComponentTypeFactory componentTypeFactory;

  @Autowired
  private ComponentTypeRepository componentTypeRepository;

  public List<ComponentTypeCombinationViewModel> getComponentTypeCombinations(boolean includeDeleted) {
    return componentTypeCombinationFactory.createViewModels(componentTypeCombinationRepository.getAllComponentTypeCombinations(includeDeleted));
  }

  public ComponentTypeCombinationViewModel findComponentTypeCombinationById(long id){
    return  componentTypeCombinationFactory.createViewModel(componentTypeCombinationRepository.findComponentTypeCombinationById(id));
  }

  public List<ComponentTypeViewModel> getAllComponentTypes() {
    return componentTypeFactory.createViewModels(componentTypeRepository.getAllComponentTypesThatCanBeIssued());
  }
}
