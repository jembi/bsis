package org.jembi.bsis.controllerservice;

import java.util.List;

import org.jembi.bsis.backingform.DivisionBackingForm;
import org.jembi.bsis.factory.DivisionFactory;
import org.jembi.bsis.model.location.Division;
import org.jembi.bsis.repository.DivisionRepository;
import org.jembi.bsis.service.DivisionCRUDService;
import org.jembi.bsis.viewmodel.DivisionViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class DivisionControllerService {

  @Autowired
  private DivisionFactory divisionFactory;

  @Autowired
  private DivisionRepository divisionRepository;
  
  @Autowired
  private DivisionCRUDService divisionCRUDService;

  public DivisionViewModel createDivision(DivisionBackingForm form) {
    Division createdDivision = divisionCRUDService.createDivision(divisionFactory.createEntity(form));
    return divisionFactory.createDivisionViewModel(createdDivision);
  }

  public List<DivisionViewModel> findDivisions(String name, boolean includeSimilarResults, Integer level) {
    List<Division> divisions = divisionRepository.findDivisions(name, includeSimilarResults, level);
    return divisionFactory.createDivisionViewModels(divisions);
  }

  public DivisionViewModel findDivisionById(long id) {
    Division division = divisionRepository.findDivisionById(id);
    return divisionFactory.createDivisionViewModel(division);
  }
  
  public DivisionViewModel updateDivision(DivisionBackingForm backingForm) {
    Division division = divisionFactory.createEntity(backingForm);
    division = divisionCRUDService.updateDivision(division);
    return divisionFactory.createDivisionViewModel(division);
  }

}
