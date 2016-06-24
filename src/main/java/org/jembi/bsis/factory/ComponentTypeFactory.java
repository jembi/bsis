package org.jembi.bsis.factory;

import java.util.ArrayList;
import java.util.List;

import org.jembi.bsis.backingform.ComponentTypeBackingForm;
import org.jembi.bsis.model.componenttype.ComponentType;
import org.jembi.bsis.viewmodel.ComponentTypeFullViewModel;
import org.jembi.bsis.viewmodel.ComponentTypeViewModel;
import org.springframework.stereotype.Service;

@Service
public class ComponentTypeFactory {
  
  public ComponentType createEntity(ComponentTypeBackingForm backingForm) {
    return backingForm.getComponentType();
  }
  
  public ComponentTypeViewModel createViewModel(ComponentType entity) {
    ComponentTypeViewModel viewModel = new ComponentTypeViewModel(entity);
    /*viewModel.setId(entity.getId());
    viewModel.setComponentTypeName(entity.getComponentTypeName());
    viewModel.setComponentTypeNameShort(entity.getComponentTypeNameShort());
    viewModel.setDescription(entity.getDescription());*/
    return viewModel;
  }
  
  public ComponentTypeFullViewModel createFullViewModel(ComponentType entity) {
    ComponentTypeFullViewModel viewModel = new ComponentTypeFullViewModel(entity);
    /*viewModel.setId(entity.getId());
    viewModel.setComponentTypeName(entity.getComponentTypeName());
    viewModel.setComponentTypeNameShort(entity.getComponentTypeNameShort());
    viewModel.setDescription(entity.getDescription());
    viewModel.setExpiresAfter(entity.getExpiresAfter());
    viewModel.setExpiresAfterUnits(entity.getExpiresAfterUnits());
    viewModel.setHasBloodGroup(entity.getHasBloodGroup());
    viewModel.setHighStorageTemperature(entity.getHighStorageTemperature());
    viewModel.setLowStorageTemperature(entity.getLowStorageTemperature());
    viewModel.setPreparationInfo(entity.getPreparationInfo());
    List<ComponentTypeCombination> producedComponentTypeCombinations = entity.getProducedComponentTypeCombinations();
    List<ComponentTypeCombinationViewModel>  producedComponentTypeCombinationViewModels = new ArrayList<>();
    if (producedComponentTypeCombinations != null) {
      for (ComponentTypeCombination producedComponentTypeCombination : producedComponentTypeCombinations) {
        producedComponentTypeCombinationViewModels.add(new ComponentTypeCombinationViewModel(producedComponentTypeCombination));
      }
    }
    viewModel.setProducedComponentTypeCombinations(producedComponentTypeCombinationViewModels);*/
    return viewModel;
  }
  
  public List<ComponentTypeViewModel> createViewModels(List<ComponentType> entities) {
    List<ComponentTypeViewModel> viewModels = new ArrayList<>();
    if (entities != null) {
      for (ComponentType entity : entities) {
        viewModels.add(createViewModel(entity));
      }
    }
    return viewModels;
  }
}
