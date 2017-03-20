package org.jembi.bsis.controllerservice;

import java.util.List;
import java.util.UUID;

import org.jembi.bsis.backingform.DivisionBackingForm;
import org.jembi.bsis.factory.DivisionFactory;
import org.jembi.bsis.model.location.Division;
import org.jembi.bsis.repository.DivisionRepository;
import org.jembi.bsis.service.DivisionCRUDService;
import org.jembi.bsis.viewmodel.DivisionFullViewModel;
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

  public DivisionFullViewModel createDivision(DivisionBackingForm form) {
    Division createdDivision = divisionCRUDService.createDivision(divisionFactory.createEntity(form));
    return divisionFactory.createDivisionFullViewModel(createdDivision);
  }

  public List<DivisionViewModel> findDivisions(String name, boolean includeSimilarResults, Integer level,
      UUID parentId) {
    List<Division> divisions = divisionRepository.findDivisions(name, includeSimilarResults, level, parentId);
    return divisionFactory.createDivisionViewModels(divisions);
  }

  public DivisionFullViewModel findDivisionById(UUID id) {
    Division division = divisionRepository.findDivisionById(id);
    return divisionFactory.createDivisionFullViewModel(division);
  }
  
  public DivisionFullViewModel updateDivision(DivisionBackingForm backingForm) {
    Division division = divisionFactory.createEntity(backingForm);
    division = divisionCRUDService.updateDivision(division);
    return divisionFactory.createDivisionFullViewModel(division);
  }

}
