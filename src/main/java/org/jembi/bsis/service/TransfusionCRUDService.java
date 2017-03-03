package org.jembi.bsis.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;
import org.apache.commons.lang3.StringUtils;
import org.jembi.bsis.model.transfusion.Transfusion;
import org.jembi.bsis.model.transfusion.TransfusionOutcome;
import org.jembi.bsis.repository.TransfusionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class TransfusionCRUDService {

  @Autowired
  private TransfusionRepository transfusionRepository;

  @Autowired
  private ComponentCRUDService componentCRUDService;
  
  /**
   * Create Transfusion data that links a specific Component with a Patient who received a blood
   * transfusion. The Component status will be set to TRANSFUSED
   * 
   * @param transfusion Transfusion entity to be saved
   * @return Transfusion persisted record
   */
  public Transfusion createTransfusion(Transfusion transfusion) {
    // Update status of transfused Component
    componentCRUDService.transfuseComponent(transfusion.getComponent());

    transfusionRepository.save(transfusion);

    return transfusion;
  }

  public List<Transfusion> findTransfusions(String din, String componentCode, Long componentTypeId,
      Long receivedFromId, TransfusionOutcome transfusionOutcome, Date startDate, Date endDate) {
    List<Transfusion> transfusions = new ArrayList<>();
    // Check if din is present
    if (StringUtils.isNotEmpty(din)) {
      transfusions = transfusionRepository.findTransfusionsByDINAndComponentCode(din, componentCode);
    } else {
      transfusions = transfusionRepository.findTransfusionByComponentTypeAndSiteAndOutcome(componentTypeId, receivedFromId, transfusionOutcome, startDate, endDate);
    }
    return transfusions;
  }
}
