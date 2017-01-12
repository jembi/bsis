package org.jembi.bsis.factory;

import java.util.ArrayList;
import java.util.List;

import org.jembi.bsis.backingform.DivisionBackingForm;
import org.jembi.bsis.model.location.Division;
import org.jembi.bsis.repository.DivisionRepository;
import org.jembi.bsis.service.DivisionConstraintChecker;
import org.jembi.bsis.viewmodel.DivisionFullViewModel;
import org.jembi.bsis.viewmodel.DivisionViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class DivisionFactory {

  @Autowired
  private DivisionRepository divisionRepository;
  @Autowired
  private DivisionConstraintChecker divisionConstraintChecker;

  public DivisionViewModel createDivisionViewModel(Division division) {
    return createDivisionViewModel(division, true);
  }

  public DivisionViewModel createDivisionViewModel(Division division, boolean includeParent) {
    DivisionViewModel viewModel = new DivisionViewModel();
    populateBasicFields(viewModel, division, includeParent);
    return viewModel;
  }
  
  public DivisionFullViewModel createDivisionFullViewModel(Division division) {
    DivisionFullViewModel viewModel = new DivisionFullViewModel();
    populateBasicFields(viewModel, division, true);
    viewModel.setPermission("canEditLevel", divisionConstraintChecker.canEditLevel(division));
    return viewModel;
  }
  
  private void populateBasicFields(DivisionViewModel viewModel, Division division, boolean includeParent) {
    viewModel.setId(division.getId());
    viewModel.setName(division.getName());
    viewModel.setLevel(division.getLevel());
    
    if (includeParent && division.getParent() != null) {
      viewModel.setParent(createDivisionViewModel(division.getParent(), false));
    }
  }

  public List<DivisionViewModel> createDivisionViewModels(List<Division> divisions) {
    List<DivisionViewModel> viewModels = new ArrayList<>();
    for (Division division : divisions) {
      viewModels.add(createDivisionViewModel(division));
    }
    return viewModels;
  }

  public List<DivisionViewModel> createDivisionViewModels(List<Division> divisions, boolean includeParent) {
    List<DivisionViewModel> viewModels = new ArrayList<>();
    for (Division division : divisions) {
      viewModels.add(createDivisionViewModel(division, includeParent));
    }
    return viewModels;
  }

  public Division createEntity(DivisionBackingForm form) {
    Division division = new Division();
    division.setId(form.getId());
    division.setName(form.getName());
    division.setLevel(form.getLevel());

    DivisionBackingForm parentBackingForm = form.getParent();
    if(parentBackingForm != null) {
      Division parent = divisionRepository.findDivisionById(parentBackingForm.getId());
      division.setParent(parent);
    }

    return division;
  }

}