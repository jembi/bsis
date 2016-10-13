package org.jembi.bsis.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.jembi.bsis.model.componenttype.ComponentTypeCombination;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class ComponentTypeCombinationRepository {

  @PersistenceContext
  private EntityManager em;

  public List<ComponentTypeCombination> getComponentTypeCombinations(Boolean isDeleted) {
    TypedQuery<ComponentTypeCombination> query;
    query = em.createQuery("select c from ComponentTypeCombination c left join fetch c.componentTypes left join fetch c.sourceComponentTypes  where c.isDeleted=:isDeleted", ComponentTypeCombination.class);
    query.setParameter("isDeleted", isDeleted);
    return query.getResultList();
  } 
  
  public List<ComponentTypeCombination> getAllComponentTypeCombinations() {
    TypedQuery<ComponentTypeCombination> query;
    query = em.createQuery("select c from ComponentTypeCombination c left join fetch c.componentTypes left join fetch c.sourceComponentTypes", ComponentTypeCombination.class);
    return query.getResultList();
  }  
}