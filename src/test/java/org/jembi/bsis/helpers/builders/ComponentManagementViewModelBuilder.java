package org.jembi.bsis.helpers.builders;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.jembi.bsis.model.component.ComponentStatus;
import org.jembi.bsis.viewmodel.ComponentManagementViewModel;
import org.jembi.bsis.viewmodel.ComponentTypeViewModel;

public class ComponentManagementViewModelBuilder {

  private Long id;
  private ComponentTypeViewModel componentType;
  private Date createdOn;
  private ComponentStatus status;
  private String expiryStatus;
  private String componentCode;
  private Integer weight;
  private Map<String, Boolean> permissions = new HashMap<>();

  public ComponentManagementViewModelBuilder withId(Long id) {
    this.id = id;
    return this;
  }

  public ComponentManagementViewModelBuilder withComponentType(ComponentTypeViewModel componentType) {
    this.componentType = componentType;
    return this;
  }

  public ComponentManagementViewModelBuilder withCreatedOn(Date createdOn) {
    this.createdOn = createdOn;
    return this;
  }

  public ComponentManagementViewModelBuilder withStatus(ComponentStatus status) {
    this.status = status;
    return this;
  }

  public ComponentManagementViewModelBuilder withExpiryStatus(String expiryStatus) {
    this.expiryStatus = expiryStatus;
    return this;
  }

  public ComponentManagementViewModelBuilder withComponentCode(String componentCode) {
    this.componentCode = componentCode;
    return this;
  }

  public ComponentManagementViewModelBuilder withWeigth(Integer weigth) {
    this.weight = weigth;
    return this;
  }

  public ComponentManagementViewModelBuilder withPermission(String name, boolean value) {
    permissions.put(name, value);
    return this;
  }

  public ComponentManagementViewModel build() {
    ComponentManagementViewModel viewModel = new ComponentManagementViewModel();
    viewModel.setId(id);
    viewModel.setCreatedOn(createdOn);
    viewModel.setExpiryStatus(expiryStatus);
    viewModel.setStatus(status);
    viewModel.setComponentCode(componentCode);
    viewModel.setWeight(weight);
    viewModel.setComponentType(componentType);
    viewModel.setPermissions(permissions);
    return viewModel;
  }
  
  public static ComponentManagementViewModelBuilder aComponentManagementViewModel() {
    return new ComponentManagementViewModelBuilder();
  }

}
