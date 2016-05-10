package factory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import model.component.Component;

import org.springframework.stereotype.Service;

import viewmodel.ComponentViewModel;

@Service
public class ComponentViewModelFactory {

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
    return new ComponentViewModel(component);
  }
}
