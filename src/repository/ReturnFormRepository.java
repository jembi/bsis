package repository;

import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;
import repository.ReturnFormNamedQueryConstants;
import model.returnform.ReturnForm;

@Repository
public class ReturnFormRepository extends AbstractRepository<ReturnForm> {

  public ReturnForm findById(Long id) {
    TypedQuery<ReturnForm> query = entityManager.createNamedQuery(ReturnFormNamedQueryConstants.NAME_FIND_BY_ID, ReturnForm.class);
    query.setParameter("id", id);
    query.setParameter("isDeleted", false);
    return query.getSingleResult();
  }

}
