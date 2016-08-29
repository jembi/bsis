package org.jembi.bsis.service;

import org.jembi.bsis.model.location.Division;
import org.jembi.bsis.repository.DivisionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DivisionConstraintChecker {
  
  @Autowired
  private DivisionRepository divisionRepository;
  
  public boolean canEditLevel(Division division) {
    // Can't edit level if the division has child divisions
    return divisionRepository.countDivisionsWithParent(division) == 0;
  }

}
