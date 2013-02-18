package repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import model.crossmatch.CrossmatchTest;
import model.donor.Donor;
import model.util.BloodAbo;
import model.util.BloodGroup;
import model.util.BloodRhd;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class CrossmatchTestRepository {

  @PersistenceContext
  private EntityManager em;

  public void addCrossmatchTest(CrossmatchTest crossmatchTest) {
    em.persist(crossmatchTest);
    em.flush();
  }
}
