package factory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import model.component.Component;
import viewmodel.InventoryViewModel;

@Service
public class InventoryFactory {

  @Autowired
  private ComponentTypeFactory componentTypeFactory;

  @Autowired
  private LocationViewModelFactory locationViewModelFactory;

  public List<InventoryViewModel> createInventoryViewModels(List<Component> components) {
    List<InventoryViewModel> viewModels = new ArrayList<>();
    if (components != null) {
      for (Component component : components) {
        viewModels.add(createInventoryViewModel(component));
      }
    }
    return viewModels;
  }

  public InventoryViewModel createInventoryViewModel(Component component) {
    InventoryViewModel viewModel = new InventoryViewModel();
    viewModel.setId(component.getId());
    viewModel.setComponentCode(component.getComponentCode());
    viewModel.setComponentType(componentTypeFactory.createViewModel(component.getComponentType()));
    viewModel.setCreatedOn(component.getCreatedOn());
    viewModel.setLocation(locationViewModelFactory.createLocationViewModel(component.getLocation()));
    viewModel.setExpiryStatus(getExpiryStatus(component));
    viewModel.setDonationIdentificationNumber(component.getDonationIdentificationNumber());
    viewModel.setInventoryStatus(component.getInventoryStatus());
    viewModel.setBloodGroup(component.getDonation().getBloodAbo() + component.getDonation().getBloodRh());
    return viewModel;
  }

  private String getExpiryStatus(Component component) {
    if (component.getExpiresOn() != null) {
      Date today = new Date();
      if (today.equals(component.getExpiresOn()) || today.before(component.getExpiresOn())) {
        DateTime expiresOn = new DateTime(component.getExpiresOn().getTime());
        Long age = (long) Days.daysBetween(expiresOn, new DateTime()).getDays();
        return Math.abs(age) + " days to expire";
      } else {
        return "Already expired";
      }
    } else {
      return "";
    }
  }
}
