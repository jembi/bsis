package org.jembi.bsis.controllerservice;

import java.util.Collections;
import java.util.List;

import org.jembi.bsis.factory.DivisionFactory;
import org.jembi.bsis.model.location.Division;
import org.jembi.bsis.viewmodel.DivisionViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class DivisionControllerService {
  
  @Autowired
  private DivisionFactory divisionFactory;
  
  public List<DivisionViewModel> findDivisions(String name, boolean includeSimilarResults, Integer level) {
    // TODO: Use query to find location divisions
    List<Division> divisions = Collections.<Division>emptyList();
    return divisionFactory.createDivisionViewModels(divisions);
  }

}
