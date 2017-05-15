package org.jembi.bsis.repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.jembi.bsis.model.user.Role;
import org.jembi.bsis.model.user.User;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class UserRepository extends AbstractRepository<User> {

  @PersistenceContext
  private EntityManager em;

  public User updateUser(User user, boolean modifyPassword) {
    User existingUser = findUserById(user.getId());
    existingUser.copy(user);
    existingUser.setIsDeleted(false);
    if (modifyPassword)
      existingUser.setPassword(user.getPassword());
    em.merge(existingUser);
    em.flush();
    return existingUser;
  }

  public User updateBasicUserInfo(User user, boolean modifyPassword) {
    User existingUser = findUserById(user.getId());
    existingUser.setFirstName(user.getFirstName());
    existingUser.setLastName(user.getLastName());
    existingUser.setEmailId(user.getEmailId());
    if (modifyPassword) {
      existingUser.setPassword(user.getPassword());
      existingUser.setPasswordReset(user.isPasswordReset());
    }
    return em.merge(existingUser);
  }

  public User findUserById(UUID id) throws NoResultException, NonUniqueResultException {
    if (id == null)
      return null;
    String queryString = "SELECT u FROM User u WHERE u.id = :userId and u.isDeleted = :isDeleted";
    TypedQuery<User> query = em.createQuery(queryString, User.class);
    query.setParameter("isDeleted", Boolean.FALSE);
    query.setParameter("userId", id);
    return query.getSingleResult();
  }

  public User findUser(String username) {
    TypedQuery<User> query = em
        .createQuery(
            "SELECT u FROM User u WHERE u.username = :username and u.isDeleted= :isDeleted",
            User.class);
    query.setParameter("isDeleted", Boolean.FALSE);
    List<User> userList = query.setParameter("username", username)
        .getResultList();
    if (userList != null && userList.size() > 0) {
      return userList.get(0);
    }
    return null;
  }

  public List<User> getAllUsers() {
    TypedQuery<User> query = em
        .createQuery("SELECT u FROM User u where u.isDeleted= :isDeleted", User.class);
    query.setParameter("isDeleted", Boolean.FALSE);
    List<User> users = query.getResultList();
    return users;
  }

  public void updateLastLogin(User user) {
    if (user == null)
      return;
    User userObj = null;
    if (em.contains(user)) {
      userObj = user;
    } else if (user.getUsername() != null) {
      userObj = findUser(user.getUsername());
    }
    if (userObj != null) {
      userObj.setLastLogin(new Date());
      em.merge(userObj);
      em.flush();
    }
  }

  public User addUser(User user) {
    em.persist(user);
    em.flush();
    em.refresh(user);
    return user;
  }

  public Role findRoleById(UUID id) throws NoResultException, NonUniqueResultException {
    if (id == null)
      return null;
    String queryString = "SELECT r FROM Role r WHERE r.id = :roleId";
    TypedQuery<Role> query = em.createQuery(queryString, Role.class);
    query.setParameter("roleId", id);
    return query.getSingleResult();
  }
}