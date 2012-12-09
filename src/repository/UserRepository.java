package repository;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import model.user.User;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class UserRepository {
  @PersistenceContext
  private EntityManager em;

  public void saveUser(User user) {
    em.persist(user);
    em.flush();
  }

  public void deleteUser(String username) {
    User existingUser = findUser(username);
    existingUser.setIsDeleted(Boolean.TRUE);
    em.merge(existingUser);
    em.flush();
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
    return query.getResultList();
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
}