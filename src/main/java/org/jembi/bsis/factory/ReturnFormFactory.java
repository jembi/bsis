package org.jembi.bsis.factory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jembi.bsis.backingform.ComponentBackingForm;
import org.jembi.bsis.backingform.ReturnFormBackingForm;
import org.jembi.bsis.model.component.Component;
import org.jembi.bsis.model.location.Location;
import org.jembi.bsis.model.returnform.ReturnForm;
import org.jembi.bsis.repository.ComponentRepository;
import org.jembi.bsis.repository.LocationRepository;
import org.jembi.bsis.service.ReturnFormConstraintChecker;
import org.jembi.bsis.viewmodel.ReturnFormFullViewModel;
import org.jembi.bsis.viewmodel.ReturnFormViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReturnFormFactory {

  @Autowired
  private LocationRepository locationRepository;

  @Autowired
  private LocationFactory locationFactory;

  @Autowired
  private ComponentFactory componentFactory;

  @Autowired
  private ComponentRepository componentRepository;
  
  @Autowired
  private ReturnFormConstraintChecker returnFormConstraintChecker;

  public ReturnForm createEntity(ReturnFormBackingForm backingForm) {
    ReturnForm entity = new ReturnForm();
    Location from = locationRepository.getLocation(backingForm.getReturnedFrom().getId());
    Location to = locationRepository.getLocation(backingForm.getReturnedTo().getId());
    entity.setId(backingForm.getId());
    entity.setReturnedFrom(from);
    entity.setReturnedTo(to);
    entity.setReturnDate(backingForm.getReturnDate());
    entity.setStatus(backingForm.getStatus());
    List<Component> components = new ArrayList<>();
    if (backingForm.getComponents() != null) {
      for (ComponentBackingForm component : backingForm.getComponents()) {
        components.add(componentRepository.findComponent(component.getId()));
      }
    }
    entity.setComponents(components);
    return entity;
  }

  public ReturnFormFullViewModel createFullViewModel(ReturnForm entity) {
    ReturnFormFullViewModel viewModel = new ReturnFormFullViewModel();
    populateBasicViewModel(entity, viewModel);
    viewModel.setComponents(componentFactory.createComponentFullViewModels(entity.getComponents()));

    // Set permissions
    Map<String, Boolean> permissions = new HashMap<>();
    permissions.put("canEdit", returnFormConstraintChecker.canEdit(entity));
    permissions.put("canReturn", returnFormConstraintChecker.canReturn(entity));
    permissions.put("canDiscard", returnFormConstraintChecker.canDiscard(entity));
    permissions.put("canDelete", returnFormConstraintChecker.canDelete(entity));
    viewModel.setPermissions(permissions);

    return viewModel;
  }

  public ReturnFormViewModel createViewModel(ReturnForm entity) {
    ReturnFormViewModel viewModel = new ReturnFormViewModel();
    populateBasicViewModel(entity, viewModel);
    return viewModel;
  }
  
  public List<ReturnFormViewModel> createViewModels(List<ReturnForm> returnForms) {
    List<ReturnFormViewModel> viewModels = new ArrayList<>();
    if (returnForms != null) {
      for (ReturnForm returnForm : returnForms) {
        viewModels.add(createViewModel(returnForm));
      }
    }
    return viewModels;
  }

  private void populateBasicViewModel(ReturnForm entity, ReturnFormViewModel viewModel) {
    viewModel.setId(entity.getId());
    viewModel.setReturnedFrom(locationFactory.createFullViewModel(entity.getReturnedFrom()));
    viewModel.setReturnedTo(locationFactory.createFullViewModel(entity.getReturnedTo()));
    viewModel.setReturnDate(entity.getReturnDate());
    viewModel.setStatus(entity.getStatus());
  }
}