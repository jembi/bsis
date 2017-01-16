package org.jembi.bsis.factory;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.jembi.bsis.backingform.ComponentTypeBackingForm;
import org.jembi.bsis.model.componenttype.ComponentType;
import org.jembi.bsis.model.componenttype.ComponentTypeCombination;
import org.jembi.bsis.viewmodel.ComponentTypeCombinationViewModel;
import org.jembi.bsis.viewmodel.ComponentTypeFullViewModel;
import org.jembi.bsis.viewmodel.ComponentTypeSearchViewModel;
import org.jembi.bsis.viewmodel.ComponentTypeViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ComponentTypeFactory {

  @Autowired
  private ComponentTypeCombinationFactory componentTypeCombinationFactory;

  public ComponentType createEntity(ComponentTypeBackingForm backingForm) {
    ComponentType entity = new ComponentType();
    populateComponentTypeEntity(backingForm, entity);
    return entity;
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
  
  private void populateComponentTypeEntity(ComponentTypeBackingForm componentTypeBackingForm, ComponentType componentType) {
    componentType.setId(componentTypeBackingForm.getId());
    componentType.setComponentTypeName(componentTypeBackingForm.getComponentTypeName());
    componentType.setComponentTypeCode(componentTypeBackingForm.getComponentTypeCode());
    componentType.setExpiresAfter(componentTypeBackingForm.getExpiresAfter());
    componentType.setMaxBleedTime(componentTypeBackingForm.getMaxBleedTime());
    componentType.setMaxTimeSinceDonation(componentTypeBackingForm.getMaxTimeSinceDonation());
    componentType.setExpiresAfterUnits(componentTypeBackingForm.getExpiresAfterUnits());
    componentType.setHasBloodGroup(componentTypeBackingForm.getHasBloodGroup());
    componentType.setDescription(componentTypeBackingForm.getDescription());
    componentType.setIsDeleted(componentTypeBackingForm.getIsDeleted());
    componentType.setLowStorageTemperature(componentTypeBackingForm.getLowStorageTemperature());
    componentType.setHighStorageTemperature(componentTypeBackingForm.getHighStorageTemperature());
    componentType.setHighTransportTemperature(componentTypeBackingForm.getHighTransportTemperature());
    componentType.setLowTransportTemperature(componentTypeBackingForm.getLowTransportTemperature());
    componentType.setStorageInfo(componentTypeBackingForm.getStorageInfo());
    componentType.setPreparationInfo(componentTypeBackingForm.getPreparationInfo());
    componentType.setTransportInfo(componentTypeBackingForm.getTransportInfo());
    componentType.setCanBeIssued(componentTypeBackingForm.getCanBeIssued());
    componentType.setContainsPlasma(componentTypeBackingForm.getContainsPlasma());
    componentType.setProducedComponentTypeCombinations(componentTypeBackingForm.getProducedComponentTypeCombinations());
    componentType.setGravity(componentTypeBackingForm.getGravity());
  }
  
  private void populateComponentTypeViewModelFields(ComponentType componentType, ComponentTypeViewModel componentTypeViewModel) {
    componentTypeViewModel.setId(componentType.getId());
    componentTypeViewModel.setComponentTypeName(componentType.getComponentTypeName());
    componentTypeViewModel.setComponentTypeCode(componentType.getComponentTypeCode());
    componentTypeViewModel.setDescription(componentType.getDescription());
    componentTypeViewModel.setMaxBleedTime(componentType.getMaxBleedTime());
    componentTypeViewModel.setMaxTimeSinceDonation(componentType.getMaxTimeSinceDonation());
  }

  private void populateComponentTypeSearchViewModelFields(
      ComponentType componentType, ComponentTypeSearchViewModel componentTypeSearchViewModel) {
    componentTypeSearchViewModel.setExpiresAfter(componentType.getExpiresAfter());
    componentTypeSearchViewModel.setCanBeIssued(componentType.getCanBeIssued());
    componentTypeSearchViewModel.setIsDeleted(componentType.getIsDeleted());
    componentTypeSearchViewModel.setExpiresAfterUnits(componentType.getExpiresAfterUnits());
    componentTypeSearchViewModel.setContainsPlasma(componentType.getContainsPlasma());
  }

  private void populateComponentTypeFullViewModelFields(
      ComponentType componentType, ComponentTypeFullViewModel componentTypeFullViewModel) {
    componentTypeFullViewModel.setHasBloodGroup(componentType.getHasBloodGroup());
    componentTypeFullViewModel.setLowStorageTemperature(componentType.getLowStorageTemperature());
    componentTypeFullViewModel.setHighStorageTemperature(componentType.getHighStorageTemperature());
    componentTypeFullViewModel.setPreparationInfo(componentType.getPreparationInfo());
    componentTypeFullViewModel.setTransportInfo(componentType.getTransportInfo());
    componentTypeFullViewModel.setStorageInfo(componentType.getStorageInfo());
    componentTypeFullViewModel.setMaxBleedTime(componentType.getMaxBleedTime());
    componentTypeFullViewModel.setMaxTimeSinceDonation(componentType.getMaxTimeSinceDonation());
    componentTypeFullViewModel.setGravity(componentType.getGravity());
  }

  private void populateProducedComponentTypeCombinationViewModels(ComponentTypeFullViewModel viewModel, ComponentType componentType) {
    Set<ComponentTypeCombination> producedComponentTypeCombinations = componentType.getProducedComponentTypeCombinations();
    if (producedComponentTypeCombinations != null) {
      List<ComponentTypeCombinationViewModel> producedComponentTypeCombinationViewModels =
          componentTypeCombinationFactory.createViewModels(new ArrayList<ComponentTypeCombination>(producedComponentTypeCombinations));
      viewModel.setProducedComponentTypeCombinations(producedComponentTypeCombinationViewModels);
    }
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
