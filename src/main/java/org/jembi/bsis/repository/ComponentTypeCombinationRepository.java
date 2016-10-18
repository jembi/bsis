package org.jembi.bsis.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.jembi.bsis.model.componenttype.ComponentTypeCombination;
import org.jembi.bsis.repository.constant.ComponentTypeCombinationsQueryConstants;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class ComponentTypeCombinationRepository extends AbstractRepository<ComponentTypeCombination> {

  @PersistenceContext
  private EntityManager em;

  public List<ComponentTypeCombination> getAllComponentTypeCombinations(boolean includeDeleted) {
    TypedQuery<ComponentTypeCombination> query;
    query = em.createNamedQuery(ComponentTypeCombinationsQueryConstants.NAME_FIND_COMPONENT_TYPE_COMBINATION, ComponentTypeCombination.class);
    query.setParameter("includeDeleted", includeDeleted);
    return query.getResultList();
  }   

  public ComponentTypeCombination findComponentTypeCombinationById(long id) {
    return em.find(ComponentTypeCombination.class, id);
  }
}