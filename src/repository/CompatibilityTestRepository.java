package repository;

import model.compatibility.CompatibilityTest;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
@Transactional
public class CompatibilityTestRepository {

  @PersistenceContext
  private EntityManager em;

  public void addCompatibilityTest(CompatibilityTest crossmatchTest) {
    em.persist(crossmatchTest);
    em.flush();
  }
}
