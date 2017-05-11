package org.jembi.bsis.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.jembi.bsis.backingform.RoleBackingForm;
import org.jembi.bsis.backingform.validator.RoleBackingFormValidator;
import org.jembi.bsis.factory.PermissionFactory;
import org.jembi.bsis.factory.RoleFactory;
import org.jembi.bsis.model.user.Role;
import org.jembi.bsis.repository.RoleRepository;
import org.jembi.bsis.utils.PermissionConstants;
import org.jembi.bsis.viewmodel.RoleViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/roles")
public class RoleController {

  @Autowired
  private RoleRepository roleRepository;

  @Autowired
  private RoleBackingFormValidator roleBackingFormValidator;

  @Autowired
  private RoleFactory roleFactory;

  @Autowired
  private PermissionFactory permissionFactory;
  

  @InitBinder
  protected void initBinder(WebDataBinder binder) {
    binder.setValidator(roleBackingFormValidator);
  }

  @RequestMapping(method = RequestMethod.GET)
  @PreAuthorize("hasRole('" + PermissionConstants.MANAGE_ROLES + "')")
  public Map<String, Object> configureRolesFormGenerator(HttpServletRequest request) {
    Map<String, Object> map = new HashMap<String, Object>();
    map.put("roles", roleFactory.createViewModels(roleRepository.getAllRoles()));
    return map;
  }

  @RequestMapping(value = "/permissions", method = RequestMethod.GET)
  @PreAuthorize("hasRole('" + PermissionConstants.MANAGE_ROLES + "')")
  public Map<String, Object> editRoleFormGenerator() {
    Map<String, Object> map = new HashMap<String, Object>();
    map.put("permissions", permissionFactory.createViewModels(roleRepository.getAllPermissionsByName()));
    return map;
  }

  @RequestMapping(value = "{id}", method = RequestMethod.PUT)
  @PreAuthorize("hasRole('" + PermissionConstants.MANAGE_ROLES + "')")
  public RoleViewModel updateRole(@Valid @RequestBody RoleBackingForm form, @PathVariable UUID id) {
    form.setId(id);
    Role updatedRole = roleRepository.updateRole(roleFactory.createEntity(form));
    return roleFactory.createViewModel(updatedRole);

  }

  @RequestMapping(value = "{id}", method = RequestMethod.GET)
  @PreAuthorize("hasRole('" + PermissionConstants.MANAGE_ROLES + "')")
  public Map<String, Object> getRoleBydId(@PathVariable UUID id) {
    Map<String, Object> map = new HashMap<String, Object>();
    Role role = roleRepository.findRoleDetailById(id);
    map.put("role", roleFactory.createViewModel(role));
    return map;
  }

  @RequestMapping(method = RequestMethod.POST)
  @PreAuthorize("hasRole('" + PermissionConstants.MANAGE_ROLES + "')")
  @ResponseStatus(HttpStatus.CREATED)
  public RoleViewModel addRole(@Valid @RequestBody RoleBackingForm form) {
    Role role = roleFactory.createEntity(form);
    role = roleRepository.addRole(role);
    return roleFactory.createViewModel(role);
  }

  @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
  @PreAuthorize("hasRole('" + PermissionConstants.MANAGE_ROLES + "')")
  public void deleteRoleBydId(@PathVariable UUID id) {
    roleRepository.deleteRole(id);
  }
}