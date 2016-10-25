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
import org.jembi.bsis.viewmodel.ComponentTypeCombinationFullViewModel;
import org.jembi.bsis.viewmodel.ComponentTypeCombinationViewModel;
import org.jembi.bsis.viewmodel.ComponentTypeViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ComponentTypeCombinationFactory {

  @Autowired
  private ComponentTypeFactory componentTypeFactory;

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
      populateComponentTypeCombination(viewModel, componentTypeCombination);
    }
    return viewModel;
  }

  public ComponentTypeCombinationFullViewModel createFullViewModel(ComponentTypeCombination componentTypeCombination) {
    ComponentTypeCombinationFullViewModel viewModel = new ComponentTypeCombinationFullViewModel();
    if (componentTypeCombination != null) {
      populateComponentTypeCombination(viewModel, componentTypeCombination);
      populateComponentTypeCombinationFull(viewModel, componentTypeCombination);
    }
    return viewModel;
  }

  public ComponentTypeCombination createEntity(ComponentTypeCombinationBackingForm backingForm) {
    ComponentTypeCombination entity = new ComponentTypeCombination();
    entity.setId(backingForm.getId());
    entity.setCombinationName(backingForm.getCombinationName());
    entity.setIsDeleted(backingForm.getIsDeleted());

    List<ComponentType> producedComponents = retrieveProducedComponents(backingForm);

    Set<ComponentType> sourceComponentTypes = retrieveAndUpdateSourceComponents(backingForm, entity);

    entity.setComponentTypes(producedComponents);
    entity.setSourceComponentTypes(sourceComponentTypes);
    return entity;
  }

  /**
   * This method retrieves all the source components contained in the backing form based on its
   * component type ID. It updates the source components by adding new componentTypeCombination to
   * the Component Type.
   *
   * @param backingForm - Backing Form
   * @param componentTypeCombination - The Component Type Combination that must be added/updated.
   * @return - Set<ComponentType>
   */
  private Set<ComponentType> retrieveAndUpdateSourceComponents(ComponentTypeCombinationBackingForm backingForm,
      ComponentTypeCombination componentTypeCombination) {
    Set<ComponentType> sourceComponentTypes = new HashSet<>();
    if (backingForm.getSourceComponentTypes() != null) {
      for (ComponentTypeBackingForm sourceComponentType : backingForm.getSourceComponentTypes()) {
        ComponentType ct = componentTypeRepository.getComponentTypeById(sourceComponentType.getId());
        Set<ComponentTypeCombination> producedComponentTypeCombinations = ct.getProducedComponentTypeCombinations();
        if (producedComponentTypeCombinations == null) {
          producedComponentTypeCombinations = new HashSet<ComponentTypeCombination>();
          ct.setProducedComponentTypeCombinations(producedComponentTypeCombinations);
        }
        producedComponentTypeCombinations.add(componentTypeCombination);
        sourceComponentTypes.add(ct);
      }
    }
    return sourceComponentTypes;
  }

  private List<ComponentType> retrieveProducedComponents(
      ComponentTypeCombinationBackingForm backingForm) {
    List<ComponentType> producedComponents = new ArrayList<>();
    if (backingForm.getComponentTypes() != null) {
      for (ComponentTypeBackingForm producedComponentType : backingForm.getComponentTypes()) {
        producedComponents.add(componentTypeRepository.getComponentTypeById(producedComponentType.getId()));
      }
    }
    return producedComponents;
  }

  private void populateComponentTypeCombination(ComponentTypeCombinationViewModel viewModel,
      ComponentTypeCombination componentTypeCombination) {
    viewModel.setId(componentTypeCombination.getId());
    viewModel.setCombinationName(componentTypeCombination.getCombinationName());
    viewModel.setIsDeleted(componentTypeCombination.getIsDeleted());
  }

  private void populateComponentTypeCombinationFull(ComponentTypeCombinationFullViewModel viewModel,
      ComponentTypeCombination componentTypeCombination) {
    // set produced component types (a list)
    viewModel.setComponentTypes(componentTypeFactory.createViewModels(componentTypeCombination.getComponentTypes()));
    // set source component types (a set)
    List<ComponentType> sourceComponentTypes = new ArrayList<>(componentTypeCombination.getSourceComponentTypes()); 
    List<ComponentTypeViewModel> sourceComponentTypeViewModels = componentTypeFactory.createViewModels(sourceComponentTypes);
    viewModel.setSourceComponentTypes(new HashSet<>(sourceComponentTypeViewModels));
  }
}
