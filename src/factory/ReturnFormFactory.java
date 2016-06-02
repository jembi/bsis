package factory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import backingform.ReturnFormBackingForm;
import model.location.Location;
import model.returnform.ReturnForm;
import repository.LocationRepository;
import viewmodel.ReturnFormViewModel;

@Service
public class ReturnFormFactory {

  @Autowired
  private LocationRepository locationRepository;

  @Autowired
  private LocationViewModelFactory locationViewModelFactory;

  public ReturnForm createEntity(ReturnFormBackingForm backingForm) {
    ReturnForm entity = new ReturnForm();
    Location from = locationRepository.getLocation(backingForm.getReturnedFrom().getId());
    Location to = locationRepository.getLocation(backingForm.getReturnedTo().getId());
    entity.setId(backingForm.getId());
    entity.setReturnedFrom(from);
    entity.setReturnedTo(to);
    entity.setReturnDate(backingForm.getReturnDate());
    entity.setStatus(backingForm.getStatus());
    return entity;
  }

  public ReturnFormViewModel createViewModel(ReturnForm entity) {
    ReturnFormViewModel viewModel = new ReturnFormViewModel();
    viewModel.setId(entity.getId());
    viewModel.setReturnedFrom(locationViewModelFactory.createLocationViewModel(entity.getReturnedFrom()));
    viewModel.setReturnedTo(locationViewModelFactory.createLocationViewModel(entity.getReturnedTo()));
    viewModel.setReturnDate(entity.getReturnDate());
    viewModel.setStatus(entity.getStatus());
    return viewModel;
  }
}