package repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import model.worksheet.Worksheet;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class WorksheetRepository {

  @PersistenceContext
  private EntityManager em;

  public Worksheet addWorksheet(Worksheet worksheet) {
    em.persist(worksheet);
    em.flush();
    em.refresh(worksheet);
    return worksheet;
  }

}
