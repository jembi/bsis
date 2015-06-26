package repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import model.user.Role;
import model.user.User;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import viewmodel.UserViewModel;

@Repository
@Transactional
public class UserRepository {

  @PersistenceContext
  private EntityManager em;

  public User updateUser(User user, boolean modifyPassword){
    User existingUser = findUserById(user.getId());
      existingUser.copy(user);
      existingUser.setIsDeleted(false);
      if(modifyPassword)
          existingUser.setPassword(user.getPassword());
      em.merge(existingUser);
      em.flush();
      return existingUser;
  }
  
  public User updateBasicUserInfo(User user, boolean modifyPassword){
      User existingUser = findUserById(user.getId());
      existingUser.setFirstName(user.getFirstName());
      existingUser.setLastName(user.getLastName());
      existingUser.setEmailId(user.getEmailId());
      if(modifyPassword)
          existingUser.setPassword(user.getPassword());
      return em.merge(existingUser);
  }

  public User findUserById(Integer id) throws NoResultException, NonUniqueResultException{
    if (id == null)
      return null;
    String queryString = "SELECT u FROM User u WHERE u.id = :userId and u.isDeleted = :isDeleted";
    TypedQuery<User> query = em.createQuery(queryString, User.class);
    query.setParameter("isDeleted", Boolean.FALSE);
    query.setParameter("userId", id);
    return query.getSingleResult();
  }

  public void deleteUser(String username)throws IllegalArgumentException {
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

  public List<UserViewModel> getAllUsers() {
    TypedQuery<User> query = em
        .createQuery("SELECT u FROM User u where u.isDeleted= :isDeleted", User.class);
    query.setParameter("isDeleted", Boolean.FALSE);
    List<User> users = query.getResultList();
    List<UserViewModel> userViewModels = new ArrayList<UserViewModel>();
    for (User user : users) {
      userViewModels.add(new UserViewModel(user));
    }
    return userViewModels;
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
  
	public List<Role> getUserRole(String []str) {
		Role role=null;
		List<Role> roles=new ArrayList<Role>();
  	if(str!=null){
  		for(String s:str){
  			if(s!= null && !s.isEmpty()){
	  			role=findRoleById(Long.parseLong(s));
	  			roles.add(role);
  			}
  		}
  	}
	return roles;
  }
  
  public Role findRoleById(Long id) throws NoResultException, NonUniqueResultException{
    if (id == null)
      return null;
    String queryString = "SELECT r FROM Role r WHERE r.id = :roleId";
    TypedQuery<Role> query = em.createQuery(queryString, Role.class);
    query.setParameter("roleId", id);
    return query.getSingleResult();
  }
  
  public void deleteUserById(Integer id)throws NoResultException, IllegalArgumentException{
      User user = findUserById(id);
      em.remove(user);
      
  }
}