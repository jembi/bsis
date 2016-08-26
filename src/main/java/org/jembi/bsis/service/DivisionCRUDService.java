package org.jembi.bsis.service;

import javax.transaction.Transactional;

import org.jembi.bsis.model.location.Division;
import org.jembi.bsis.repository.DivisionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Transactional
@Service
public class DivisionCRUDService {

  @Autowired
  private DivisionRepository divisionRepository;
  @Autowired
  private DivisionConstraintChecker divisionConstraintChecker;

  public Division createDivision(Division division) {
    divisionRepository.save(division);
    return division;
  }

  public Division updateDivision(Division division) {
    
    Division existingDivision = divisionRepository.findDivisionById(division.getId());

    // Validate constraint if the level has changed
    boolean levelChanged = existingDivision.getLevel() != division.getLevel();
    if (levelChanged && !divisionConstraintChecker.canEditLevel(existingDivision)) {
      throw new IllegalArgumentException("Cannot edit division level");
    }

    existingDivision.setName(division.getName());
    existingDivision.setLevel(division.getLevel());
    existingDivision.setParent(division.getParent());
    return divisionRepository.update(existingDivision);
  }

}
