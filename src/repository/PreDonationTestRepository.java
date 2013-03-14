package repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import model.predonationtests.ConfiguredPreDonationTest;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class PreDonationTestRepository {

  @PersistenceContext
  EntityManager em;

  public List<ConfiguredPreDonationTest> getAllConfiguredPreDonationTests() {
    String queryStr = "SELECT c FROM ConfiguredPreDonationTest c where enabled=:enabled";
    TypedQuery<ConfiguredPreDonationTest> query = em.createQuery(queryStr, ConfiguredPreDonationTest.class);
    query.setParameter("enabled", true);
    return query.getResultList();
  }
}
