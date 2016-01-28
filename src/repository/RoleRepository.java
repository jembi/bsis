package repository;

import model.user.Permission;
import model.user.Role;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import viewmodel.RoleViewModel;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Repository
@Transactional
public class RoleRepository {

  @PersistenceContext
  private EntityManager em;

  public List<RoleViewModel> getAllRoles() {
    TypedQuery<Role> query = em.createQuery("FROM Role", Role.class);
    List<Role> roles = query.getResultList();
    List<RoleViewModel> roleViewModels = new ArrayList<>();
    for (Role role : roles) {
      roleViewModels.add(new RoleViewModel(role));
    }
    return roleViewModels;
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

  public Role findRoleDetailById(Long id) throws NoResultException, NonUniqueResultException {

    String queryString = "SELECT r FROM Role r WHERE r.id = :roleId";
    TypedQuery<Role> query = em.createQuery(queryString, Role.class)
            .setMaxResults(1);
    query.setParameter("roleId", id);
    return query.getSingleResult();
  }

  public List<Permission> getAllPermissions() {
    TypedQuery<Permission> query = em.createQuery("FROM Permission",
            Permission.class);
    return query.getResultList();
  }

  public List<Permission> getAllPermissionsByName() {
    TypedQuery<Permission> query = em.createQuery("FROM Permission ORDER BY name ASC",
            Permission.class);
    return query.getResultList();
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

  public void deleteRole(Long id) {
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
