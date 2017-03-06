package org.jembi.bsis.repository;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.jembi.bsis.model.transfusion.Transfusion;
import org.jembi.bsis.model.transfusion.TransfusionOutcome;
import org.jembi.bsis.repository.constant.TranfusionNamedQueryConstants;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class TransfusionRepository extends AbstractRepository<Transfusion> {
  
  @PersistenceContext
  private EntityManager em;
  
  public Transfusion findTransfusionByDINAndComponentCode(String donationIdentificationNumber, String componentCode) {
    return em.createNamedQuery(TranfusionNamedQueryConstants.NAME_FIND_TRANSFUSION_BY_DIN_AND_COMPONENT_CODE, Transfusion.class)
        .setParameter("donationIdentificationNumber", donationIdentificationNumber)
        .setParameter("componentCode", componentCode)
        .setParameter("isDeleted", false)
        .getSingleResult();
  }

  public List<Transfusion> findTransfusions(Long componentTypeId, Long receivedFromId,
      TransfusionOutcome transfusionOutcome, Date startDate, Date endDate) {

    boolean includeTransfusionOutcome = true;
    if (transfusionOutcome == null) {
      includeTransfusionOutcome = false;
    }

    return em.createNamedQuery(
        TranfusionNamedQueryConstants.NAME_FIND_TRANSFUSIONS, Transfusion.class)
        .setParameter("componentTypeId", componentTypeId)
        .setParameter("receivedFromId", receivedFromId)
        .setParameter("includeTransfusionOutcome", includeTransfusionOutcome)
        .setParameter("transfusionOutcome", transfusionOutcome)
        .setParameter("startDate", startDate)
        .setParameter("endDate", endDate)
        .setParameter("isDeleted", false)
        .getResultList();
  }
}