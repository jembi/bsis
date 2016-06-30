package org.jembi.bsis.factory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.jembi.bsis.backingform.ComponentBackingForm;
import org.jembi.bsis.model.component.Component;
import org.jembi.bsis.repository.ComponentRepository;
import org.jembi.bsis.service.ComponentConstraintChecker;
import org.jembi.bsis.viewmodel.ComponentManagementViewModel;
import org.jembi.bsis.viewmodel.ComponentViewModel;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ComponentFactory {
  
  @Autowired
  private LocationViewModelFactory locationViewModelFactory;

  @Autowired
  private ComponentTypeFactory componentTypeFactory;

  @Autowired
  private PackTypeFactory packTypeFactory;

  @Autowired
  private ComponentConstraintChecker componentConstraintChecker;
  
  @Autowired
  private ComponentRepository componentRepository;
  
  public Component createEntity(ComponentBackingForm backingForm) {
    Component component = new Component();
    
    // set values from backing form
    component.setId(backingForm.getId());
    component.setWeight(backingForm.getWeight());
    
    // set values from existing Component
    Component existingComponent = componentRepository.findComponent(backingForm.getId());
    component.setComponentCode(existingComponent.getComponentCode());
    component.setComponentType(existingComponent.getComponentType());
    component.setStatus(existingComponent.getStatus());
    component.setInventoryStatus(existingComponent.getInventoryStatus());
    component.setParentComponent(existingComponent.getParentComponent());
    component.setStatusChanges(existingComponent.getStatusChanges());
    component.setDonation(existingComponent.getDonation());
    component.setLocation(existingComponent.getLocation());
    component.setComponentBatch(existingComponent.getComponentBatch());
    component.setCreatedOn(existingComponent.getCreatedOn());
    component.setExpiresOn(existingComponent.getExpiresOn());
    component.setDiscardedOn(existingComponent.getDiscardedOn());
    component.setIssuedOn(existingComponent.getIssuedOn());
    component.setNotes(existingComponent.getNotes());

    return component;
  }

  public List<ComponentManagementViewModel> createManagementViewModels(Collection<Component> components) {
    List<ComponentManagementViewModel> viewModels = new ArrayList<>();
    if (components != null) {
      Iterator<Component> it = components.iterator();
      while (it.hasNext()) {
        viewModels.add(createManagementViewModel(it.next()));
      }
    }
    return viewModels;
  }

  public ComponentManagementViewModel createManagementViewModel(Component component) {
    ComponentManagementViewModel viewModel = new ComponentManagementViewModel();
    viewModel.setComponentCode(component.getComponentCode());
    viewModel.setComponentType(componentTypeFactory.createFullViewModel(component.getComponentType()));
    viewModel.setCreatedOn(component.getCreatedOn());
    viewModel.setExpiryStatus(getExpiryStatus(component));
    viewModel.setId(component.getId());
    viewModel.setStatus(component.getStatus());
    viewModel.setWeight(component.getWeight());
    viewModel.setPackType(packTypeFactory.createFullViewModel(component.getDonation().getPackType()));

    // Set permissions
    Map<String, Boolean> permissions = new HashMap<>();
    permissions.put("canDiscard", componentConstraintChecker.canDiscard(component));
    permissions.put("canProcess", componentConstraintChecker.canProcess(component));
    permissions.put("canRecordWeight", componentConstraintChecker.canRecordWeight(component));
    permissions.put("canRollback", componentConstraintChecker.canRollback(component));
    viewModel.setPermissions(permissions);
    return viewModel;
  }

  public List<ComponentViewModel> createComponentViewModels(Collection<Component> components) {
    List<ComponentViewModel> viewModels = new ArrayList<>();
    if (components != null) {
      Iterator<Component> it = components.iterator();
      while (it.hasNext()) {
        viewModels.add(createComponentViewModel(it.next()));
      }
    }
    return viewModels;
  }
  
  public ComponentViewModel createComponentViewModel(Component component) {
    ComponentViewModel viewModel = new ComponentViewModel();
    viewModel.setBloodAbo(component.getDonation().getBloodAbo());
    viewModel.setBloodRh(component.getDonation().getBloodRh());
    viewModel.setComponentCode(component.getComponentCode());
    viewModel.setComponentType(componentTypeFactory.createViewModel(component.getComponentType()));
    viewModel.setCreatedDate(component.getCreatedDate());
    viewModel.setCreatedOn(component.getCreatedOn());
    viewModel.setDiscardedOn(component.getDiscardedOn());
    viewModel.setDonationIdentificationNumber(component.getDonationIdentificationNumber());
    viewModel.setExpiresOn(component.getExpiresOn());
    viewModel.setExpiryStatus(getExpiryStatus(component));
    viewModel.setId(component.getId());
    viewModel.setInventoryStatus(component.getInventoryStatus());
    viewModel.setIssuedOn(component.getIssuedOn());
    viewModel.setLocation(locationViewModelFactory.createLocationViewModel(component.getLocation()));
    viewModel.setNotes(component.getNotes());
    viewModel.setPackType(packTypeFactory.createFullViewModel(component.getDonation().getPackType()));
    viewModel.setStatus(component.getStatus());
    return viewModel;
  }

  private String getExpiryStatus(Component component) {
    Date today = new Date();
    if (component.getExpiresOn() == null) {
      return "";
    }
    if (today.equals(component.getExpiresOn()) || today.before(component.getExpiresOn())) {
      DateTime expiresOn = new DateTime(component.getExpiresOn().getTime());
      Long age = (long) Days.daysBetween(expiresOn, new DateTime()).getDays();
      return Math.abs(age) + " days to expire";
    } else {
      return "Already expired";
    }
  }
}
