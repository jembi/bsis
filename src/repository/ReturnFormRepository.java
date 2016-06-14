package repository;

import java.util.List;

import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;
import repository.ReturnFormNamedQueryConstants;
import model.returnform.ReturnForm;
import model.returnform.ReturnStatus;

@Repository
public class ReturnFormRepository extends AbstractRepository<ReturnForm> {

  public ReturnForm findById(Long id) {
    TypedQuery<ReturnForm> query = entityManager.createNamedQuery(ReturnFormNamedQueryConstants.NAME_FIND_BY_ID, ReturnForm.class);
    query.setParameter("id", id);
    query.setParameter("isDeleted", false);
    return query.getSingleResult();
  }
  
  public List<ReturnForm> findCreatedReturnForms() {
    return entityManager.createNamedQuery(ReturnFormNamedQueryConstants.NAME_FIND_CREATED_RETURN_FORMS, ReturnForm.class)
        .setParameter("status", ReturnStatus.CREATED)
        .setParameter("deleted", false)
        .getResultList();
  }

}
