package repository;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import model.user.User;
import model.user.Role;
import model.user.Permission;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import viewmodel.UserViewModel;
import viewmodel.RoleViewModel;

@Repository
@Transactional
public class RoleRepository {

	@PersistenceContext
	private EntityManager em;

	public List<RoleViewModel> getAllRoles() {
		TypedQuery<Role> query = em.createQuery("FROM Role", Role.class);
		List<Role> roles = query.getResultList();
		List<RoleViewModel> userViewModels = new ArrayList<RoleViewModel>();
		for (Role role : roles) {
			userViewModels.add(new RoleViewModel(role));
		}
		return userViewModels;
	}

	public Role findRoleByName(String name) {
		System.out.println("role not empty....");
		String queryString = "SELECT r FROM Role r WHERE r.name = :roleName";
		TypedQuery<Role> query = em.createQuery(queryString, Role.class);
		query.setParameter("roleName", name);
		List<Role> resultList = query.getResultList();
		return resultList.isEmpty() ? null : resultList.get(0);
	}
	
	public Role findRoleDetailById(Long id) {
		if (id == null) {
			return null;
		}
		String queryString = "SELECT r FROM Role r WHERE r.id = :roleId";
		TypedQuery<Role> query = em.createQuery(queryString, Role.class)
				.setMaxResults(1);
		query.setParameter("roleId", id);
		List<Role> resultList = query.getResultList();
		return resultList.isEmpty() ? null : resultList.get(0);
	}

	public List<Permission> getAllPermissions() {
		TypedQuery<Permission> query = em.createQuery("FROM Permission order by name ASC",
				Permission.class);
		List<Permission> permission = query.getResultList();
		return permission;
	}

	public Role updateRole(Role role) {
		Role existingRole = findRoleDetailById(role.getId());
		if (existingRole == null) {
			return null;
		}
		existingRole.setDescription(role.getDescription());
		existingRole.setName(role.getName());
		existingRole.setPermissions(role.getPermissions());
		em.merge(existingRole);
		em.flush();
		return existingRole;
	}
	
	public Role addRole(Role role) {
	    em.merge(role);
	    em.flush();
	    
	    return role;
	  }

	public Permission findPermissionByPermissionId(long permissionId) {
		// TODO Auto-generated method stub
		TypedQuery<Permission> query = em.createQuery("Select p FROM Permission p where p.id = :permissionId",
				Permission.class).setMaxResults(1);
		query.setParameter("permissionId", permissionId);
		List<Permission> permission =  query.getResultList();
		return permission.isEmpty() ? null : permission.get(0);
	}

}
