package repository;

import org.springframework.stereotype.Repository;

import model.returnform.ReturnForm;

@Repository
public class ReturnFormRepository extends AbstractRepository<ReturnForm> {

  public ReturnForm findById(Long id) {
    return entityManager.find(ReturnForm.class, id);
  }

}
