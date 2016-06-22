package org.jembi.bsis.controller;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.jembi.bsis.backingform.RoleBackingForm;
import org.jembi.bsis.backingform.validator.RoleBackingFormValidator;
import org.jembi.bsis.model.user.Permission;
import org.jembi.bsis.model.user.Role;
import org.jembi.bsis.repository.RoleRepository;
import org.jembi.bsis.utils.PermissionConstants;
import org.jembi.bsis.viewmodel.RoleViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/roles")
public class RoleController {

  @Autowired
  private RoleRepository roleRepository;

  @Autowired
  private RoleBackingFormValidator roleBackingFormValidator;

  public RoleController() {
  }

  @InitBinder
  protected void initBinder(WebDataBinder binder) {
    binder.setValidator(roleBackingFormValidator);
  }

  @RequestMapping(method = RequestMethod.GET)
  @PreAuthorize("hasRole('" + PermissionConstants.MANAGE_ROLES + "')")
  public Map<String, Object> configureRolesFormGenerator(HttpServletRequest request) {

    Map<String, Object> map = new HashMap<String, Object>();
    addAllRolesToModel(map);
    return map;
  }

  @RequestMapping(value = "/permissions", method = RequestMethod.GET)
  @PreAuthorize("hasRole('" + PermissionConstants.MANAGE_ROLES + "')")
  public Map<String, Object> editRoleFormGenerator() {
    Map<String, Object> map = new HashMap<String, Object>();
    addAllPermissionsToModel(map);
    return map;
  }

  @RequestMapping(value = "{id}", method = RequestMethod.PUT)
  @PreAuthorize("hasRole('" + PermissionConstants.MANAGE_ROLES + "')")
  public ResponseEntity updateRole(
      @Valid @RequestBody RoleBackingForm form, @PathVariable Long id) {

    Role updatedRole = null;
    form.setId(id);
    updatedRole = roleRepository.updateRole(form.getRole());
    return new ResponseEntity(new RoleViewModel(updatedRole), HttpStatus.OK);

  }

  @RequestMapping(value = "{id}", method = RequestMethod.GET)
  @PreAuthorize("hasRole('" + PermissionConstants.MANAGE_ROLES + "')")
  public ResponseEntity getRoleBydId(@PathVariable Long id) {
    Map<String, Object> map = new HashMap<String, Object>();
    Role role = roleRepository.findRoleDetailById(id);
    map.put("role", new RoleViewModel(role));
    return new ResponseEntity(map, HttpStatus.OK);

  }

  @RequestMapping(method = RequestMethod.POST)
  @PreAuthorize("hasRole('" + PermissionConstants.MANAGE_ROLES + "')")
  public ResponseEntity addRole(
      @Valid @RequestBody RoleBackingForm form) {

    Role role = new Role();
    role.setName(form.getName());
    role.setDescription(form.getDescription());
    role.setPermissions(form.getPermissions());
    role = roleRepository.addRole(role);
    return new ResponseEntity(new RoleViewModel(role), HttpStatus.CREATED);
  }

  @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
  @PreAuthorize("hasRole('" + PermissionConstants.MANAGE_ROLES + "')")
  public ResponseEntity deleteRoleBydId(@PathVariable Long id) {

    roleRepository.deleteRole(id);
    return new ResponseEntity<Role>(HttpStatus.NO_CONTENT);

  }

  private void addAllRolesToModel(Map<String, Object> map) {
    List<RoleViewModel> roles = roleRepository.getAllRoles();
    map.put("roles", roles);
  }

  private void addAllPermissionsToModel(Map<String, Object> map) {
    List<Permission> permissions = roleRepository.getAllPermissionsByName();
    map.put("permissions", permissions);
  }

}