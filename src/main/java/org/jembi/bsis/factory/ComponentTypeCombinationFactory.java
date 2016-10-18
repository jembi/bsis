package org.jembi.bsis.factory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jembi.bsis.backingform.ComponentTypeBackingForm;
import org.jembi.bsis.backingform.ComponentTypeCombinationBackingForm;
import org.jembi.bsis.model.componenttype.ComponentType;
import org.jembi.bsis.model.componenttype.ComponentTypeCombination;
import org.jembi.bsis.repository.ComponentTypeRepository;
import org.jembi.bsis.viewmodel.ComponentTypeCombinationViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ComponentTypeCombinationFactory {

  @Autowired
  private ComponentTypeRepository componentTypeRepository;

  public List<ComponentTypeCombinationViewModel> createViewModels(List<ComponentTypeCombination> componentTypeCombinations) {
    List<ComponentTypeCombinationViewModel> combinationViewModels = new ArrayList<>();
    if (componentTypeCombinations != null) {
      for (ComponentTypeCombination componentTypeCombination : componentTypeCombinations) {
        combinationViewModels.add(createViewModel(componentTypeCombination));
      }
    }
    return combinationViewModels;
  }

  public ComponentTypeCombinationViewModel createViewModel(ComponentTypeCombination componentTypeCombination) {
    ComponentTypeCombinationViewModel viewModel = new ComponentTypeCombinationViewModel();
    if (componentTypeCombination != null) {
      viewModel.setId(componentTypeCombination.getId());
      viewModel.setCombinationName(componentTypeCombination.getCombinationName());
      viewModel.setIsDeleted(componentTypeCombination.getIsDeleted());
    }
    return viewModel;
  }

  public ComponentTypeCombination createEntity(ComponentTypeCombinationBackingForm backingForm) {
    ComponentTypeCombination entity = new ComponentTypeCombination();
    entity.setId(backingForm.getId());
    entity.setCombinationName(backingForm.getCombinationName());
    entity.setIsDeleted(backingForm.getIsDeleted());
    List<ComponentType> producedComponents = new ArrayList<>();
    if (backingForm.getComponentTypes() != null) {
      for (ComponentTypeBackingForm producedComponentType : backingForm.getComponentTypes()) {
        producedComponents.add(componentTypeRepository.getComponentTypeById(producedComponentType.getId()));
      }
    }
    Set<ComponentType> sourceComponentTypes = new HashSet<>();
    if (backingForm.getSourceComponentTypes() != null) {
      for (ComponentTypeBackingForm sourceComponentType : backingForm.getSourceComponentTypes()) {
        sourceComponentTypes.add(componentTypeRepository.getComponentTypeById(sourceComponentType.getId()));
      }
    }
    entity.setComponentTypes(producedComponents);
    entity.setSourceComponentTypes(sourceComponentTypes);
    return entity;
  }

}
