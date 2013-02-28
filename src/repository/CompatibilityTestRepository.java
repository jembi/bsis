package repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import model.compatibility.CompatibilityTest;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

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
