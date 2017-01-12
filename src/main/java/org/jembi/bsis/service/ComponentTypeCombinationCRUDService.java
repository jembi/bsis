package org.jembi.bsis.service;

import java.util.Set;

import org.jembi.bsis.model.componenttype.ComponentType;
import org.jembi.bsis.model.componenttype.ComponentTypeCombination;
import org.jembi.bsis.repository.ComponentTypeCombinationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class ComponentTypeCombinationCRUDService {

  @Autowired
  private ComponentTypeCombinationRepository componentTypeCombinationRepository;

  public ComponentTypeCombination createComponentTypeCombination(ComponentTypeCombination componentTypeCombination) {
    componentTypeCombinationRepository.save(componentTypeCombination);
    return componentTypeCombination;
  }

  public ComponentTypeCombination updateComponentTypeCombinations(ComponentTypeCombination componentTypeCombination) {

    ComponentTypeCombination existingCombination = componentTypeCombinationRepository
        .findComponentTypeCombinationById(componentTypeCombination.getId());

    // Manually remove combination orphans from source component types
    Set<ComponentType> sourceComponentTypes = existingCombination.getSourceComponentTypes();
    for (ComponentType existingType : sourceComponentTypes) {
      if (!componentTypeCombination.getSourceComponentTypes().contains(existingType)) {
        existingType.getProducedComponentTypeCombinations().remove(existingCombination);
      }
    }
        
    existingCombination.setCombinationName(componentTypeCombination.getCombinationName());
    existingCombination.setComponentTypes(componentTypeCombination.getComponentTypes());
    existingCombination.setIsDeleted(componentTypeCombination.getIsDeleted());
    existingCombination.setSourceComponentTypes(componentTypeCombination.getSourceComponentTypes());

    return componentTypeCombinationRepository.update(existingCombination);
  }
}
