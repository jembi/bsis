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
  
  public List<TransfusionSummaryDTO> findTransfusionsRecorded(Location receivedFrom, Date startDate, Date endDate) {
    return entityManager.createNamedQuery(TransfusionNamedQueryConstants.NAME_FIND_TRANSFUSIONS_RECORDED, 
        TransfusionSummaryDTO.class)
        .setParameter("receivedFrom", receivedFrom)
        .setParameter("startDate", startDate)
        .setParameter("endDate", endDate)
        .setParameter("transfusionDeleted", false)
        .getResultList();
  }
}