package org.jembi.bsis.repository;

import org.jembi.bsis.model.donordeferral.DeferralReason;
import org.jembi.bsis.model.donordeferral.DeferralReasonType;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import java.util.UUID;
import java.util.List;

@Repository
@Transactional

/**
 * Created by duma on 2015/07/21.
 */
public class DeferralReasonRepository {

  @PersistenceContext
  private EntityManager em;

  public List<DeferralReason> getAllDeferralReasonsIncludDeleted() {
    TypedQuery<DeferralReason> query;
    query = em.createQuery("SELECT d from DeferralReason d", DeferralReason.class);
    return query.getResultList();
  }
  
  public List<DeferralReason> getAllDeferralReasons() {
    return em.createNamedQuery(DeferralReasonNamedQueryConstants.NAME_FIND_ALL_DEFERRAL_REASONS, DeferralReason.class)
        .setParameter("deleted", false)
        .getResultList();
  }

  public DeferralReason findDeferralReason(String reason) {
    String queryString = "SELECT d FROM DeferralReason d WHERE d.reason = :reason";
    TypedQuery<DeferralReason> query = em.createQuery(queryString, DeferralReason.class);
    query.setParameter("reason", reason);
    DeferralReason result = null;
    try {
      result = query.getSingleResult();
    } catch (NoResultException ex) {
    }
    return result;
  }

  public DeferralReason getDeferralReasonById(UUID DeferralReasonId) {
    TypedQuery<DeferralReason> query;
    query = em.createQuery("SELECT d from DeferralReason d " +
        "where d.id=:id", DeferralReason.class);
    query.setParameter("id", DeferralReasonId);
    if (query.getResultList().size() == 0)
      return null;
    return query.getSingleResult();
  }

  public DeferralReason saveDeferralReason(DeferralReason deferralReason) {
    em.persist(deferralReason);
    em.flush();
    return deferralReason;
  }

  public DeferralReason updateDeferralReason(DeferralReason deferralReason) {
    DeferralReason existingDeferralReason = getDeferralReasonById(deferralReason.getId());
    if (existingDeferralReason == null) {
      return null;
    }
    existingDeferralReason.copy(deferralReason);
    em.merge(existingDeferralReason);
    em.flush();
    return existingDeferralReason;
  }

  public DeferralReason findDeferralReasonByType(DeferralReasonType deferralReasonType)
      throws NonUniqueResultException, NoResultException {

    return em.createNamedQuery(
        DeferralReasonNamedQueryConstants.NAME_FIND_DEFERRAL_REASON_BY_TYPE,
        DeferralReason.class)
        .setParameter("type", deferralReasonType)
        .setParameter("deleted", false)
        .getSingleResult();
  }
  
  public boolean verifyDeferralReasonExists(UUID id) {
    long count = em.createNamedQuery(DeferralReasonNamedQueryConstants.NAME_COUNT_DEFERRAL_REASONS_FOR_ID, Number.class)
        .setParameter("id", id)
        .setParameter("deleted", false)
        .getSingleResult()
        .longValue();
    return count > 0;
  }
}
