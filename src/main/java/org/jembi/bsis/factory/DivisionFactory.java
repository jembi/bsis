package org.jembi.bsis.factory;

import java.util.ArrayList;
import java.util.List;

import org.jembi.bsis.model.location.LocationDivision;
import org.jembi.bsis.viewmodel.DivisionViewModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class DivisionFactory {
  
  public DivisionViewModel createDivisionViewModel(LocationDivision division) {
    DivisionViewModel viewModel = new DivisionViewModel();
    viewModel.setId(division.getId());
    viewModel.setName(division.getName());
    viewModel.setLevel(division.getLevel());
    return viewModel;
  }
  
  public List<DivisionViewModel> createDivisionViewModels(List<LocationDivision> divisions) {
    List<DivisionViewModel> viewModels = new ArrayList<>();
    for (LocationDivision division : divisions) {
      viewModels.add(createDivisionViewModel(division));
    }
    return viewModels;
  }

}
