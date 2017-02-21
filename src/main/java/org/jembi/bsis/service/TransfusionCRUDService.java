package org.jembi.bsis.service;

import javax.transaction.Transactional;

import org.jembi.bsis.model.transfusion.Transfusion;
import org.jembi.bsis.repository.TransfusionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class TransfusionCRUDService {

  @Autowired
  private TransfusionRepository transfusionRepository;
  
  public Transfusion createTransfusion(Transfusion transfusion) {
    // FIXME: to be implemented
    return transfusion;
  }
}
