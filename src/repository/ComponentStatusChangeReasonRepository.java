package repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import model.componentmovement.ComponentStatusChangeReason;
import model.componentmovement.ComponentStatusChangeReasonCategory;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class ComponentStatusChangeReasonRepository {

  @PersistenceContext
  private EntityManager em;

  public List<ComponentStatusChangeReason> getAllComponentStatusChangeReasons() {
    TypedQuery<ComponentStatusChangeReason> query;
    query = em.createQuery("SELECT p from ComponentStatusChangeReason p where p.isDeleted=:isDeleted",
        ComponentStatusChangeReason.class);
    query.setParameter("isDeleted", false);
    return query.getResultList();
  }

  public List<ComponentStatusChangeReason> getComponentStatusChangeReasons(
      ComponentStatusChangeReasonCategory category) {
    TypedQuery<ComponentStatusChangeReason> query;
    query = em.createQuery("SELECT p from ComponentStatusChangeReason p where " +
        "p.category=:category AND p.isDeleted=:isDeleted",
        ComponentStatusChangeReason.class);
    query.setParameter("isDeleted", false);
    query.setParameter("category", category);
    return query.getResultList();
  }
  
  public ComponentStatusChangeReason getComponentStatusChangeReasonById(Long id) {
    TypedQuery<ComponentStatusChangeReason> query;
    query = em.createQuery("SELECT p from ComponentStatusChangeReason p " +
            "where p.id=:id AND p.isDeleted=:isDeleted", ComponentStatusChangeReason.class);
    query.setParameter("isDeleted", false);
    query.setParameter("id", id);
    if (query.getResultList().size() == 0)
      return null;
    return query.getSingleResult();
  }

  public Map<ComponentStatusChangeReasonCategory, ComponentStatusChangeReason> getAllComponentStatusChangeReasonsAsMap() {
    TypedQuery<ComponentStatusChangeReason> query;
    query = em.createQuery("SELECT p from ComponentStatusChangeReason p where p.isDeleted=:isDeleted",
        ComponentStatusChangeReason.class);
    query.setParameter("isDeleted", false);
    Map<ComponentStatusChangeReasonCategory, ComponentStatusChangeReason> statusChangeReasonMap =
        new HashMap<ComponentStatusChangeReasonCategory, ComponentStatusChangeReason>();
    for (ComponentStatusChangeReason statusChangeReason : query.getResultList()) {
      statusChangeReasonMap.put(statusChangeReason.getCategory(), statusChangeReason);
    }
    return statusChangeReasonMap;
  }
}
