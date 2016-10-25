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

  public boolean isUniqueCombinationName(Long id, String combinationName) {
    // passing null as the ID parameter does not work because the IDs in mysql are never null. So if
    // id is null, the below rather uses -1 which achieves the same result in the case of this
    // query.
    return em.createNamedQuery(ComponentTypeCombinationsQueryConstants.NAME_VERIFY_UNIQUE_COMPONENT_TYPE_COMBINATION_NAME, Boolean.class)
        .setParameter("id", id != null ? id : -1L)
        .setParameter("combinationName", combinationName)
        .getSingleResult();
  }
}