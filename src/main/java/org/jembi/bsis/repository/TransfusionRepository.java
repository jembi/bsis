package org.jembi.bsis.repository;

import java.util.Date;
import java.util.List;

import org.jembi.bsis.dto.TransfusionSummaryDTO;
import org.jembi.bsis.model.location.Location;
import org.jembi.bsis.model.transfusion.Transfusion;
import org.jembi.bsis.repository.constant.TransfusionNamedQueryConstants;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class TransfusionRepository extends AbstractRepository<Transfusion> {
  
  public List<TransfusionSummaryDTO> findTransfusionSummaryRecordedForUsageSiteForPeriod(Long receivedFromId, Date startDate, Date endDate) {
    return entityManager.createNamedQuery(TransfusionNamedQueryConstants.NAME_FIND_TRANSFUSION_SUMMARY_RECORDED_FOR_USAGE_SITE_FOR_PERIOD, 
        TransfusionSummaryDTO.class)
        .setParameter("receivedFromId", receivedFromId)
        .setParameter("startDate", startDate)
        .setParameter("endDate", endDate)
        .setParameter("transfusionDeleted", false)
        .getResultList();
  }
}