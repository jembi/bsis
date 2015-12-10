package repository;

import model.componentmovement.ComponentStatusChangeReason;
import model.componentmovement.ComponentStatusChangeReasonCategory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
@Transactional

public class DiscardReasonRepository {

  @PersistenceContext
  private EntityManager em;

  public List<ComponentStatusChangeReason> getAllDiscardReasons() {
    TypedQuery<ComponentStatusChangeReason> query;
    query = em.createQuery("SELECT p from ComponentStatusChangeReason p WHERE p.category = :category", ComponentStatusChangeReason.class);
    query.setParameter("category", ComponentStatusChangeReasonCategory.DISCARDED);
    return query.getResultList();
  }

  public ComponentStatusChangeReason findDiscardReason(String reason) {
    String queryString = "SELECT p FROM ComponentStatusChangeReason p WHERE p.statusChangeReason = :reason AND p.category = :category";
    TypedQuery<ComponentStatusChangeReason> query = em.createQuery(queryString, ComponentStatusChangeReason.class);
    query.setParameter("reason", reason);
    query.setParameter("category", ComponentStatusChangeReasonCategory.DISCARDED);
    ComponentStatusChangeReason result = null;
    try {
      result = query.getSingleResult();
    } catch (NoResultException ex) {
    }
    return result;
  }

  public ComponentStatusChangeReason getDiscardReasonById(Integer DiscardReasonId) {
    TypedQuery<ComponentStatusChangeReason> query;
    query = em.createQuery("SELECT p from ComponentStatusChangeReason p " +
            "where p.id=:id AND p.category= :category", ComponentStatusChangeReason.class);
    query.setParameter("id", DiscardReasonId);
    query.setParameter("category", ComponentStatusChangeReasonCategory.DISCARDED);
    if (query.getResultList().size() == 0)
      return null;
    return query.getSingleResult();
  }

  public void saveAllDiscardReasons(List<ComponentStatusChangeReason> allDiscardReasons) {
    for (ComponentStatusChangeReason dr : allDiscardReasons) {
      ComponentStatusChangeReason existingDiscardReason = getDiscardReasonById(dr.getId());
      if (existingDiscardReason != null) {
        existingDiscardReason.setStatusChangeReason(dr.getStatusChangeReason());
        em.merge(existingDiscardReason);
      } else {
        em.persist(dr);
      }
    }
    em.flush();
  }

  public ComponentStatusChangeReason saveDiscardReason(ComponentStatusChangeReason deferralReason) {
    em.persist(deferralReason);
    em.flush();
    return deferralReason;
  }

  public ComponentStatusChangeReason updateDiscardReason(ComponentStatusChangeReason deferralReason) {
    ComponentStatusChangeReason existingDiscardReason = getDiscardReasonById(deferralReason.getId());
    if (existingDiscardReason == null) {
      return null;
    }
    existingDiscardReason.copy(deferralReason);
    em.merge(existingDiscardReason);
    em.flush();
    return existingDiscardReason;
  }
}