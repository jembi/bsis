package org.jembi.bsis.factory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.jembi.bsis.model.component.Component;
import org.jembi.bsis.viewmodel.InventoryViewModel;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InventoryFactory {

  @Autowired
  private ComponentTypeFactory componentTypeFactory;

  @Autowired
  private LocationViewModelFactory locationViewModelFactory;

  public List<InventoryViewModel> createViewModels(List<Component> components) {
    List<InventoryViewModel> viewModels = new ArrayList<>();
    if (components != null) {
      for (Component component : components) {
        viewModels.add(createViewModel(component));
      }
    }
    return viewModels;
  }

  public InventoryViewModel createViewModel(Component component) {
    InventoryViewModel viewModel = new InventoryViewModel();
    viewModel.setId(component.getId());
    viewModel.setComponentCode(component.getComponentCode());
    viewModel.setComponentType(componentTypeFactory.createViewModel(component.getComponentType()));
    viewModel.setCreatedOn(component.getCreatedOn());
    viewModel.setLocation(locationViewModelFactory.createFullViewModel(component.getLocation()));
    viewModel.setExpiryStatus(getExpiryStatus(component));
    viewModel.setDonationIdentificationNumber(component.getDonationIdentificationNumber());
    viewModel.setInventoryStatus(component.getInventoryStatus());
    String bloodGroup = component.getDonation().getBloodAbo() + component.getDonation().getBloodRh();
    viewModel.setBloodGroup(bloodGroup.replace("null", ""));
    return viewModel;
  }

  private String getExpiryStatus(Component component) {
    if (component.getExpiresOn() == null) {
      return "";
    }
    Date today = new Date();
    if (today.equals(component.getExpiresOn()) || today.before(component.getExpiresOn())) {
      DateTime expiresOn = new DateTime(component.getExpiresOn().getTime());
      Long age = (long) Days.daysBetween(expiresOn, new DateTime()).getDays();
      return Math.abs(age) + " days to expire";
    } else {
      return "Already expired";
    }

  }
}
