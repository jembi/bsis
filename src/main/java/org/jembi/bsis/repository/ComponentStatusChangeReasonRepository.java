package org.jembi.bsis.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.jembi.bsis.model.componentmovement.ComponentStatusChangeReason;
import org.jembi.bsis.model.componentmovement.ComponentStatusChangeReasonCategory;
import org.jembi.bsis.model.componentmovement.ComponentStatusChangeReasonType;
import org.jembi.bsis.repository.constant.ComponentStatusChangeReasonNamedQueryConstants;
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
  
  public ComponentStatusChangeReason findFirstComponentStatusChangeReasonForCategory(
      ComponentStatusChangeReasonCategory category) {
    return em.createNamedQuery(
        ComponentStatusChangeReasonNamedQueryConstants.NAME_FIND_FIRST_COMPONENT_STATUS_CHANGE_REASON_FOR_CATEGORY,
        ComponentStatusChangeReason.class)
        .setParameter("category", category)
        .setParameter("deleted", false)
        .getResultList()
        .get(0);
  }
  
  public ComponentStatusChangeReason findComponentStatusChangeReasonByCategoryAndType(
      ComponentStatusChangeReasonCategory category, ComponentStatusChangeReasonType type) {
    return em.createNamedQuery(
        ComponentStatusChangeReasonNamedQueryConstants.NAME_FIND_COMPONENT_STATUS_CHANGE_REASON_BY_CATEGORY_AND_TYPE,
        ComponentStatusChangeReason.class)
        .setParameter("category", category)
        .setParameter("type", type)
        .setParameter("deleted", false)
        .getSingleResult();
  }
}
