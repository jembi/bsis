package org.jembi.bsis.repository;

import java.util.Date;
import java.util.List;

import org.jembi.bsis.dto.TransfusionSummaryDTO;
import org.jembi.bsis.model.transfusion.Transfusion;
import org.jembi.bsis.model.transfusion.TransfusionOutcome;
import org.jembi.bsis.repository.constant.TransfusionNamedQueryConstants;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class TransfusionRepository extends AbstractRepository<Transfusion> {
  
  public Transfusion findTransfusionByDINAndComponentCode(String donationIdentificationNumber, String componentCode) {
    return entityManager.createNamedQuery(TransfusionNamedQueryConstants.NAME_FIND_TRANSFUSION_BY_DIN_AND_COMPONENT_CODE, Transfusion.class)
        .setParameter("donationIdentificationNumber", donationIdentificationNumber)
        .setParameter("componentCode", componentCode)
        .setParameter("isDeleted", false)
        .getSingleResult();
  }

  public Transfusion findTransfusionById(long transfusionId) {
    return entityManager.createNamedQuery(TransfusionNamedQueryConstants.NAME_FIND_TRANSFUSION_BY_ID, Transfusion.class)
        .setParameter("transfusionId", transfusionId)
        .getSingleResult();
  }

  public List<Transfusion> findTransfusions(Long componentTypeId, Long receivedFromId,
      TransfusionOutcome transfusionOutcome, Date startDate, Date endDate) {

    boolean includeTransfusionOutcome = true;
    if (transfusionOutcome == null) {
      includeTransfusionOutcome = false;
    }

    return entityManager.createNamedQuery(
        TransfusionNamedQueryConstants.NAME_FIND_TRANSFUSIONS, Transfusion.class)
        .setParameter("componentTypeId", componentTypeId)
        .setParameter("receivedFromId", receivedFromId)
        .setParameter("includeTransfusionOutcome", includeTransfusionOutcome)
        .setParameter("transfusionOutcome", transfusionOutcome)
        .setParameter("startDate", startDate)
        .setParameter("endDate", endDate)
        .setParameter("isDeleted", false)
        .getResultList();
  }

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