package org.jembi.bsis.repository;

import java.util.List;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.jembi.bsis.model.user.Permission;
import org.jembi.bsis.model.user.Role;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
@Transactional
public class RoleRepository {

  @PersistenceContext
  private EntityManager em;

  public List<Role> getAllRoles() {
    TypedQuery<Role> query = em.createQuery("FROM Role", Role.class);
    List<Role> roles = query.getResultList();
    return roles;
  }

  public Role findRoleByName(String name) {
    String queryString = "SELECT r FROM Role r WHERE r.name = :roleName";
    TypedQuery<Role> query = em.createQuery(queryString, Role.class);
    query.setParameter("roleName", name);
    try {
      return query.getSingleResult();
    } catch (NoResultException ex) {
      return null;
    } catch (NonUniqueResultException ex) {
      throw new NonUniqueResultException("More than on erole exists with name :" + name);
    }
  }

  public Role findRoleDetailById(UUID id) throws NoResultException, NonUniqueResultException {

    String queryString = "SELECT r FROM Role r WHERE r.id = :roleId";
    TypedQuery<Role> query = em.createQuery(queryString, Role.class)
        .setMaxResults(1);
    query.setParameter("roleId", id);
    return query.getSingleResult();
  }

  public List<Permission> getAllPermissions() {
    TypedQuery<Permission> query = em.createQuery("FROM Permission",
        Permission.class);
    List<Permission> permission = query.getResultList();
    return permission;
  }

  public List<Permission> getAllPermissionsByName() {
    TypedQuery<Permission> query = em.createQuery("FROM Permission ORDER BY name ASC",
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

  public Role addRole(Role role) throws IllegalArgumentException {
    em.persist(role);
    em.flush();
    em.refresh(role);
    return role;
  }

  public void deleteRole(UUID id) {
    Role role = findRoleDetailById(id);
    em.remove(role);
  }

  public Permission findPermissionByPermissionId(long permissionId) {
    // TODO Auto-generated method stub
    TypedQuery<Permission> query = em.createQuery("Select p FROM Permission p where p.id = :permissionId",
        Permission.class).setMaxResults(1);
    query.setParameter("permissionId", permissionId);
    List<Permission> permission = query.getResultList();
    return permission.isEmpty() ? null : permission.get(0);
  }

}
