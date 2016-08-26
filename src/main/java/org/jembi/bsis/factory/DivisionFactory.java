package org.jembi.bsis.factory;

import java.util.ArrayList;
import java.util.List;

import org.jembi.bsis.model.location.Division;
import org.jembi.bsis.viewmodel.DivisionViewModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class DivisionFactory {
  
  public DivisionViewModel createDivisionViewModel(Division division) {
    return createDivisionViewModel(division, true);
  }

  public DivisionViewModel createDivisionViewModel(Division division, boolean includeParent) {
    DivisionViewModel viewModel = new DivisionViewModel();
    viewModel.setId(division.getId());
    viewModel.setName(division.getName());
    viewModel.setLevel(division.getLevel());
    
    if (includeParent && division.getParent() != null) {
      viewModel.setParent(createDivisionViewModel(division.getParent(), false));
    }
    return viewModel;
  }
  
  public List<DivisionViewModel> createDivisionViewModels(List<Division> divisions) {
    List<DivisionViewModel> viewModels = new ArrayList<>();
    for (Division division : divisions) {
      viewModels.add(createDivisionViewModel(division));
    }
    return viewModels;
  }

}
