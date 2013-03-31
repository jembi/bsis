package repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import model.worksheet.WorksheetType;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class WorksheetTypeRepository {

  @PersistenceContext
  private EntityManager em;

  public List<WorksheetType> getAllWorksheetTypes() {
    TypedQuery<WorksheetType> query;
    query = em.createQuery("SELECT w from WorksheetType w where w.isDeleted=:isDeleted", WorksheetType.class);
    query.setParameter("isDeleted", false);
    return query.getResultList();
  }

  public WorksheetType getWorksheetTypeById(Integer id) {
    TypedQuery<WorksheetType> query;
    query = em.createQuery("SELECT wt from WorksheetType wt " +
            "where wt.id=:id AND wt.isDeleted=:isDeleted", WorksheetType.class);
    query.setParameter("isDeleted", false);
    query.setParameter("id", id);
    if (query.getResultList().size() == 0)
      return null;
    return query.getSingleResult();
  }

}
