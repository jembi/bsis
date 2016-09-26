package org.jembi.bsis.factory;

import java.util.ArrayList;
import java.util.List;

import org.jembi.bsis.backingform.ComponentTypeBackingForm;
import org.jembi.bsis.model.componenttype.ComponentType;
import org.jembi.bsis.model.componenttype.ComponentTypeCombination;
import org.jembi.bsis.viewmodel.ComponentTypeCombinationViewModel;
import org.jembi.bsis.viewmodel.ComponentTypeFullViewModel;
import org.jembi.bsis.viewmodel.ComponentTypeSearchViewModel;
import org.jembi.bsis.viewmodel.ComponentTypeViewModel;
import org.springframework.stereotype.Service;

@Service
public class ComponentTypeFactory {

  public ComponentType createEntity(ComponentTypeBackingForm backingForm) {
    return backingForm.getComponentType();
  }

  public ComponentTypeViewModel createViewModel(ComponentType componentType) {
    ComponentTypeViewModel viewModel = new ComponentTypeViewModel();
    
    populateComponentTypeViewModelFields(componentType, viewModel);
    
    return viewModel;
  }

  public ComponentTypeSearchViewModel createSearchViewModel(ComponentType componentType) {
    ComponentTypeSearchViewModel viewModel = new ComponentTypeSearchViewModel();
    
    populateComponentTypeViewModelFields(componentType, viewModel);
    populateComponentTypeSearchViewModelFields(componentType, viewModel);
    
    return viewModel;
  }
  
  public ComponentTypeFullViewModel createFullViewModel(ComponentType componentType) {
    ComponentTypeFullViewModel viewModel = new ComponentTypeFullViewModel();
    
    populateComponentTypeViewModelFields(componentType, viewModel);    
    populateComponentTypeSearchViewModelFields(componentType, viewModel);
    populateComponentTypeFullViewModelFields(componentType, viewModel);
    
    populateProducedComponentTypeCombinationViewModels(viewModel, componentType);
    
    return viewModel;
  }

  public ComponentTypeCombinationViewModel createComponentTypeCombinationViewModel(
      ComponentTypeCombination componentTypeCombination) {
    ComponentTypeCombinationViewModel viewModel = new ComponentTypeCombinationViewModel();
    viewModel.setId(componentTypeCombination.getId());
    viewModel.setCombinationName(componentTypeCombination.getCombinationName());
    viewModel.setComponentTypes(createViewModels(componentTypeCombination.getComponentTypes()));
    return viewModel;

  }
  
  private void populateComponentTypeViewModelFields(ComponentType componentType, ComponentTypeViewModel componentTypeViewModel) {
    componentTypeViewModel.setId(componentType.getId());
    componentTypeViewModel.setComponentTypeName(componentType.getComponentTypeName());
    componentTypeViewModel.setComponentTypeCode(componentType.getComponentTypeCode());
    componentTypeViewModel.setDescription(componentType.getDescription());
  }

  private void populateComponentTypeSearchViewModelFields(
      ComponentType componentType, ComponentTypeSearchViewModel componentTypeSearchViewModel) {
    componentTypeSearchViewModel.setExpiresAfter(componentType.getExpiresAfter());
    componentTypeSearchViewModel.setCanBeIssued(componentType.getCanBeIssued());
    componentTypeSearchViewModel.thatIsDeleted(componentType.getIsDeleted());
    componentTypeSearchViewModel.setExpiresAfterUnits(componentType.getExpiresAfterUnits());
    componentTypeSearchViewModel.thatContainsPlasma(componentType.getContainsPlasma());
  }

  private void populateComponentTypeFullViewModelFields(
      ComponentType componentType, ComponentTypeFullViewModel componentTypeFullViewModel) {
    componentTypeFullViewModel.setHasBloodGroup(componentType.getHasBloodGroup());
    componentTypeFullViewModel.setLowStorageTemperature(componentType.getLowStorageTemperature());
    componentTypeFullViewModel.setHighStorageTemperature(componentType.getHighStorageTemperature());
    componentTypeFullViewModel.setPreparationInfo(componentType.getPreparationInfo());
    componentTypeFullViewModel.setTransportInfo(componentType.getTransportInfo());
    componentTypeFullViewModel.setStorageInfo(componentType.getStorageInfo());
  }

  private void populateProducedComponentTypeCombinationViewModels(ComponentTypeFullViewModel viewModel, ComponentType componentType) {
    List<ComponentTypeCombination> producedComponentTypeCombinations = componentType.getProducedComponentTypeCombinations();
    List<ComponentTypeCombinationViewModel> producedComponentTypeCombinationViewModels = new ArrayList<>();
    if (producedComponentTypeCombinations != null) {
      for (ComponentTypeCombination producedComponentTypeCombination : producedComponentTypeCombinations) {
        producedComponentTypeCombinationViewModels
            .add(createComponentTypeCombinationViewModel(producedComponentTypeCombination));
      }
    }
    viewModel.setProducedComponentTypeCombinations(producedComponentTypeCombinationViewModels);
  }
  
  /**
   * Converts a List of Component Type Entities to a List of Component Type Search View Models.
   * 
   * @param componentTypes
   * @return
   */
  public List<ComponentTypeSearchViewModel> createSearchViewModels(List<ComponentType> componentTypes) {
    List<ComponentTypeSearchViewModel> viewModels = new ArrayList<>();
    if (componentTypes != null) {
      for (ComponentType componentType : componentTypes) {
        viewModels.add(createSearchViewModel(componentType));
      }
    }
    return viewModels;
  }

  /**
   * Converts a List of Component Type Entities to a List of Component Type View Models.
   * 
   * @param componentTypes
   * @return
   */
  public List<ComponentTypeViewModel> createViewModels(List<ComponentType> componentTypes) {
    List<ComponentTypeViewModel> viewModels = new ArrayList<>();
    if (componentTypes != null) {
      for (ComponentType componentType : componentTypes) {
        viewModels.add(createViewModel(componentType));
      }
    }
    return viewModels;
  }
}
