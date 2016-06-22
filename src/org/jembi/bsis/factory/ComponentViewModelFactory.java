package org.jembi.bsis.factory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.jembi.bsis.model.component.Component;
import org.jembi.bsis.viewmodel.ComponentViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ComponentViewModelFactory {
  
  @Autowired
  LocationViewModelFactory locationViewModelFactory;

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
    ComponentViewModel viewModel = new ComponentViewModel(component);
    if (component.getLocation() != null) {
      viewModel.setLocation(locationViewModelFactory.createLocationViewModel(component.getLocation()));
    }
    return viewModel;
  }
}
