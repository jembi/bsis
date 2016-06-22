package org.jembi.bsis.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.jembi.bsis.model.user.User;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class LoginRepository {
  @PersistenceContext
  private EntityManager em;

  public User getUser(String username) {
    String queryString = "SELECT u FROM User u WHERE u.username= :username and u.isDeleted= :isDeleted";
    TypedQuery<User> query = em.createQuery(queryString, User.class);
    query.setParameter("isDeleted", Boolean.FALSE);
    List<User> users = query.setParameter("username", username)
        .getResultList();
    if (users == null || users.size() == 0) {
      return null;
    }
    return users.get(0);
  }
}
