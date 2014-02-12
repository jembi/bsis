package repository;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import model.user.Role;
import model.user.User;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import viewmodel.UserViewModel;

@Repository
@Transactional
public class RoleRepository {
	 
	 @PersistenceContext
	  private EntityManager em;
	
	public List<Role> getAllRoles()
	{
		 TypedQuery<Role> query = em
			        .createQuery("SELECT role FROM Role role ", Role.class);
			    List<Role> roles = query.getResultList();
			    return roles;
	}

}
