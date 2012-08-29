package repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import model.User;

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

    public void updateUser(User user) {
        User existingUser = findUser(user.getUsername());
        existingUser.copy(user);
        em.merge(existingUser);
        em.flush();
    }

    public void deleteUser(String username) {
        User existingUser = findUser(username);
        existingUser.setIsDeleted(Boolean.TRUE);
        em.merge(existingUser);
        em.flush();
    }


    public User findUser(String username) {
        TypedQuery<User> query = em.createQuery(
                "SELECT u FROM User u WHERE u.username = :username and u.isDeleted= :isDeleted", User.class);
        query.setParameter("isDeleted", Boolean.FALSE);
        List<User> userList = query.setParameter("username", username).getResultList();
        if (userList != null && userList.size() > 0) {
            return userList.get(0);
        }
        return null;
    }


    public List<User> getAllUsers() {
        Query query = em.createQuery("SELECT u FROM User u where u.isDeleted= :isDeleted");
        query.setParameter("isDeleted",Boolean.FALSE);
        return query.getResultList();
    }
}
