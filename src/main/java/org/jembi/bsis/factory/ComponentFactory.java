package org.jembi.bsis.factory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.jembi.bsis.model.component.Component;
import org.jembi.bsis.model.componentmovement.ComponentStatusChange;
import org.jembi.bsis.model.componentmovement.ComponentStatusChangeReason;
import org.jembi.bsis.repository.component.ComponentStatusChangeRepository;
import org.jembi.bsis.service.ComponentConstraintChecker;
import org.jembi.bsis.service.ComponentStatusCalculator;
import org.jembi.bsis.viewmodel.ComponentFullViewModel;
import org.jembi.bsis.viewmodel.ComponentManagementViewModel;
import org.jembi.bsis.viewmodel.ComponentViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ComponentFactory {
  
  @Autowired
  private LocationFactory locationFactory;

  @Autowired
  private ComponentTypeFactory componentTypeFactory;

  @Autowired
  private PackTypeFactory packTypeFactory;

  @Autowired
  private ComponentConstraintChecker componentConstraintChecker;
  
  @Autowired
  private ComponentStatusCalculator componentStatusCalculator;

  @Autowired
  private ComponentStatusChangeRepository statusChangeRepository;

  public List<ComponentManagementViewModel> createManagementViewModels(Collection<Component> components) {
    List<ComponentManagementViewModel> viewModels = new ArrayList<>();
    if (components != null) {
      for (Component component : components) {
        viewModels.add(createManagementViewModel(component));
      }
    }
    return viewModels;
  }

  public ComponentManagementViewModel createManagementViewModel(Component component) {
    ComponentManagementViewModel viewModel = new ComponentManagementViewModel();
    viewModel.setComponentCode(component.getComponentCode());
    viewModel.setComponentType(componentTypeFactory.createFullViewModel(component.getComponentType()));
    viewModel.setCreatedOn(component.getCreatedOn());
    viewModel.setExpiresOn(component.getExpiresOn());
    viewModel.setDaysToExpire(componentStatusCalculator.getDaysToExpire(component));
    viewModel.setId(component.getId());
    viewModel.setStatus(component.getStatus());
    viewModel.setWeight(component.getWeight());
    viewModel.setPackType(packTypeFactory.createFullViewModel(component.getDonation().getPackType()));
    viewModel.setBatched(component.hasComponentBatch());
    viewModel.setInventoryStatus(component.getInventoryStatus());
    viewModel.setBleedStartTime(component.getDonation().getBleedStartTime());
    viewModel.setBleedEndTime(component.getDonation().getBleedEndTime());
    viewModel.setDonationDateTime(component.getDonation().getInitialComponent().getCreatedOn());
    Optional<ComponentStatusChange> optionalDiscardReason = statusChangeRepository.findLatestDiscardReasonForComponent(component);
    if (optionalDiscardReason.isPresent()) {
      ComponentStatusChange discardReason = optionalDiscardReason.get();
      viewModel.setDiscardReason(discardReason.getStatusChangeReason().getStatusChangeReason());
      viewModel.setDiscardReasonComment(discardReason.getStatusChangeReasonText());
    }
    if (component.getParentComponent() != null) {
      viewModel.setParentComponentId(component.getParentComponent().getId());
    }

    // Set permissions
    Map<String, Boolean> permissions = new HashMap<>();
    permissions.put("canDiscard", componentConstraintChecker.canDiscard(component));
    permissions.put("canProcess", componentConstraintChecker.canProcess(component));
    permissions.put("canPreProcess", componentConstraintChecker.canPreProcess(component));
    permissions.put("canUnprocess", componentConstraintChecker.canUnprocess(component));
    permissions.put("canUndiscard", componentConstraintChecker.canUndiscard(component));
    permissions.put("canRecordChildComponentWeight", componentConstraintChecker.canRecordChildComponentWeight(component));
    viewModel.setPermissions(permissions);

    return viewModel;
  }

  public List<ComponentFullViewModel> createComponentFullViewModels(Collection<Component> components) {
    List<ComponentFullViewModel> viewModels = new ArrayList<>();
    if (components != null) {
      for (Component component : components) {
        viewModels.add(createComponentFullViewModel(component));
      }
    }
    return viewModels;
  }
  
  public List<ComponentViewModel> createComponentViewModels(Collection<Component> components) {
    List<ComponentViewModel> viewModels = new ArrayList<>();
    if (components != null) {
      for (Component component : components) {
        viewModels.add(createComponentViewModel(component));
      }
    }
    return viewModels;
  }

  public ComponentViewModel createComponentViewModel(Component component) {
    ComponentViewModel viewModel = new ComponentViewModel();
    return populateViewModel(viewModel, component);
  }

  public ComponentFullViewModel createComponentFullViewModel(Component component) {
    ComponentFullViewModel viewModel = new ComponentFullViewModel();
    populateViewModel(viewModel, component);

    viewModel.setBloodAbo(component.getDonation().getBloodAbo());
    viewModel.setBloodRh(component.getDonation().getBloodRh());
    viewModel.setCreatedDate(component.getCreatedDate());
    viewModel.setDiscardedOn(component.getDiscardedOn());
    viewModel.setExpiresOn(component.getExpiresOn());
    viewModel.setInventoryStatus(component.getInventoryStatus());
    viewModel.setIssuedOn(component.getIssuedOn());
    viewModel.setNotes(component.getNotes());
    viewModel.setPackType(packTypeFactory.createFullViewModel(component.getDonation().getPackType()));
    viewModel.setIsInitialComponent(component.isInitialComponent());
    return viewModel;
  }

  private ComponentViewModel populateViewModel(ComponentViewModel viewModel, Component component) {
    viewModel.setComponentCode(component.getComponentCode());
    viewModel.setComponentType(componentTypeFactory.createViewModel(component.getComponentType()));
    viewModel.setCreatedOn(component.getCreatedOn());
    viewModel.setExpiresOn(component.getExpiresOn());
    viewModel.setDonationIdentificationNumber(component.getDonationIdentificationNumber());
    viewModel.setDonationFlagCharacters(component.getDonation().getFlagCharacters());
    viewModel.setDaysToExpire(componentStatusCalculator.getDaysToExpire(component));
    viewModel.setId(component.getId());
    viewModel.setLocation(locationFactory.createViewModel(component.getLocation()));
    viewModel.setStatus(component.getStatus());
    return viewModel;
  }
}
