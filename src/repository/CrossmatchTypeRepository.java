package repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import model.compatibility.CrossmatchType;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class CrossmatchTypeRepository {

  @PersistenceContext
  private EntityManager em;

  public List<CrossmatchType> getAllCrossmatchTypes() {
    TypedQuery<CrossmatchType> query;
    query = em.createQuery("SELECT ct from CrossmatchType ct where ct.isDeleted=:isDeleted", CrossmatchType.class);
    query.setParameter("isDeleted", false);
    return query.getResultList();
  }
  
  public boolean isCrossmatchTypeValid(String checkCrossmatchType) {
    String queryString = "SELECT ct from CrossmatchType ct where ct.isDeleted=:isDeleted";
    TypedQuery<CrossmatchType> query = em.createQuery(queryString, CrossmatchType.class);
    query.setParameter("isDeleted", false);
    for (CrossmatchType crossmatchType : query.getResultList()) {
      if (crossmatchType.getCrossmatchType().equals(checkCrossmatchType))
        return true;
    }
    return false;
  }

  public CrossmatchType getCrossmatchTypeById(Long requestTypeId) {
    TypedQuery<CrossmatchType> query;
    query = em.createQuery("SELECT ct from CrossmatchType ct " +
            "where ct.id=:id AND ct.isDeleted=:isDeleted", CrossmatchType.class);
    query.setParameter("isDeleted", false);
    query.setParameter("id", requestTypeId);
    if (query.getResultList().size() == 0)
      return null;
    return query.getSingleResult();
  }

  public void saveAllCrossmatchTypes(List<CrossmatchType> allCrossmatchTypes) {
    for (CrossmatchType ct: allCrossmatchTypes) {
        CrossmatchType existingCrossmatchType = getCrossmatchTypeById(ct.getId());
        if (existingCrossmatchType != null) {
          existingCrossmatchType.setCrossmatchType(ct.getCrossmatchType());
          em.merge(existingCrossmatchType);
        }
        else {
          em.persist(ct);
        }
    }
    em.flush();
  }
}
