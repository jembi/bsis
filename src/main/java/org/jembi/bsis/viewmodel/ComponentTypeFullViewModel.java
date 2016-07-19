package org.jembi.bsis.viewmodel;

import java.util.ArrayList;
import java.util.List;

import org.jembi.bsis.model.componenttype.ComponentType;
import org.jembi.bsis.model.componenttype.ComponentTypeCombination;
import org.jembi.bsis.model.componenttype.ComponentTypeTimeUnits;

public class ComponentTypeFullViewModel extends ComponentTypeViewModel {

  public ComponentTypeFullViewModel(ComponentType componentType) {
    super(componentType);
  }

  public boolean isHasBloodGroup() {
    return componentType.getHasBloodGroup();
  }

  public Integer getExpiresAfter() {
    return componentType.getExpiresAfter();
  }

  public ComponentTypeTimeUnits getExpiresAfterUnits() {
    return componentType.getExpiresAfterUnits();
  }

  public Integer getLowStorageTemperature() {
    return componentType.getLowStorageTemperature();
  }

  public Integer getHighStorageTemperature() {
    return componentType.getHighStorageTemperature();
  }

  public String getPreparationInfo() {
    return componentType.getPreparationInfo();
  }

  public String getTransportInfo() {
    return componentType.getTransportInfo();
  }

  public String getStorageInfo() {
    return componentType.getStorageInfo();
  }

  public boolean getCanBeIssued() {
    return componentType.getCanBeIssued();
  }

  public List<ComponentTypeCombinationViewModel> getProducedComponentTypeCombinations() {
    return getComponentTypeCombinationViewModels(componentType.getProducedComponentTypeCombinations());
  }

  private List<ComponentTypeCombinationViewModel> getComponentTypeCombinationViewModels(
      List<ComponentTypeCombination> componentTypeCombinations) {

    List<ComponentTypeCombinationViewModel> componentTypeCombinationViewModels =
        new ArrayList<ComponentTypeCombinationViewModel>();
    for (ComponentTypeCombination componentTypeCombination : componentTypeCombinations)
      componentTypeCombinationViewModels.add(new ComponentTypeCombinationViewModel(componentTypeCombination));

    return componentTypeCombinationViewModels;
  }
}
