package repository;

import model.bloodtesting.WellType;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
@Transactional
public class WellTypeRepository {

  @PersistenceContext
  private EntityManager em;

  public List<WellType> getAllWellTypes() {
    TypedQuery<WellType> query;
    query = em.createQuery("SELECT w from WellType w where w.isDeleted=:isDeleted", WellType.class);
    query.setParameter("isDeleted", false);
    return query.getResultList();
  }

  public boolean isWellTypeValid(String checkWellType) {
    String queryString = "SELECT w from WellType w where w.isDeleted=:isDeleted";
    TypedQuery<WellType> query = em.createQuery(queryString, WellType.class);
    query.setParameter("isDeleted", false);
    for (WellType wellType : query.getResultList()) {
      if (wellType.getWellType().equals(checkWellType))
        return true;
    }
    return false;
  }

  public WellType getWellTypeById(Integer id) {
    TypedQuery<WellType> query;
    query = em.createQuery("SELECT wt from WellType wt " +
            "where wt.id=:id AND wt.isDeleted=:isDeleted", WellType.class);
    query.setParameter("isDeleted", false);
    query.setParameter("id", id);
    if (query.getResultList().size() == 0)
      return null;
    return query.getSingleResult();
  }

  public void saveAllWellTypes(List<WellType> allWellTypes) {
    for (WellType wt : allWellTypes) {
      WellType existingWellType;
      existingWellType = getWellTypeById(wt.getId());
      if (existingWellType != null) {
        existingWellType.setWellType(wt.getWellType());
        em.merge(existingWellType);
      } else {
        em.persist(wt);
      }
    }
    em.flush();
  }
}
