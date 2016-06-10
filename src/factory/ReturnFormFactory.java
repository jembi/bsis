package factory;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import backingform.ComponentBackingForm;
import backingform.ReturnFormBackingForm;
import model.component.Component;
import model.location.Location;
import model.returnform.ReturnForm;
import repository.ComponentRepository;
import repository.LocationRepository;
import viewmodel.ReturnFormFullViewModel;
import viewmodel.ReturnFormViewModel;

@Service
public class ReturnFormFactory {

  @Autowired
  private LocationRepository locationRepository;

  @Autowired
  private LocationViewModelFactory locationViewModelFactory;

  @Autowired
  private ComponentViewModelFactory componentViewModelFactory;

  @Autowired
  private ComponentRepository componentRepository;

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
    System.out.println(entity.getComponents());
    viewModel.setComponents(componentViewModelFactory.createComponentViewModels(entity.getComponents()));
    System.out.println(viewModel.getComponents());
    return viewModel;
  }

  public ReturnFormViewModel createViewModel(ReturnForm entity) {
    ReturnFormViewModel viewModel = new ReturnFormViewModel();
    populateBasicViewModel(entity, viewModel);
    return viewModel;
  }

  private void populateBasicViewModel(ReturnForm entity, ReturnFormViewModel viewModel) {
    viewModel.setId(entity.getId());
    viewModel.setReturnedFrom(locationViewModelFactory.createLocationViewModel(entity.getReturnedFrom()));
    viewModel.setReturnedTo(locationViewModelFactory.createLocationViewModel(entity.getReturnedTo()));
    viewModel.setReturnDate(entity.getReturnDate());
    viewModel.setStatus(entity.getStatus());
  }
}