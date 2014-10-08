package controller;

import backingform.RoleBackingForm;
import backingform.validator.RoleBackingFormValidator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import model.user.Permission;
import model.user.Role;
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
import repository.RoleRepository;
import utils.PermissionConstants;
import viewmodel.RoleViewModel;

@RestController
@RequestMapping("/roles")
public class RoleController {

	@Autowired
	private UtilController utilController;

	@Autowired
	private RoleRepository roleRepository;
	
	public RoleController() {
	}
	
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
	  binder.setValidator(new RoleBackingFormValidator(binder.getValidator(), utilController,roleRepository));
	}

        @RequestMapping(value = "/configure", method = RequestMethod.GET)
	@PreAuthorize("hasRole('"+PermissionConstants.MANAGE_ROLES+"')")
	public  Map<String, Object> configureRolesFormGenerator(HttpServletRequest request) {

                Map<String, Object> map = new  HashMap<String, Object>();
		addAllRolesToModel(map);
		map.put("refreshUrl", utilController.getUrl(request));
		return map;
	}

	private void addAllRolesToModel(Map<String, Object> map) {
		List<RoleViewModel> roles = roleRepository.getAllRoles();
		map.put("allRoles", roles);
	}

	private void addAllPermissionsToModel(Map<String, Object> map) {
		List<Permission> permissions = roleRepository.getAllPermissionsByName();
		map.put("allPermissions", permissions);
	}

	@RequestMapping(value = "{id}/edit/form", method = RequestMethod.GET)
	@PreAuthorize("hasRole('"+PermissionConstants.MANAGE_ROLES+"')")
	public  Map<String, Object> editRoleFormGenerator(HttpServletRequest request,
			@PathVariable Long id) {

		RoleBackingForm form = new RoleBackingForm();
                Map<String, Object> map = new HashMap<String, Object>();
		map.put("requestUrl", utilController.getUrl(request));
		if (id != null) {
			form.setId(id);
			Role role = roleRepository.findRoleDetailById(id);
			form.setName(role.getName());
			form.setRole(role);
			map.put("existingRole", true);
		} else {
			map.put("existingRole", false);

		}
		addAllPermissionsToModel(map);
		map.put("editRoleForm", form);
		map.put("refreshUrl", utilController.getUrl(request));
		// to ensure custom field names are displayed in the form
		return map;
	}

    @RequestMapping(value = "{id}", method = RequestMethod.PUT)
    @PreAuthorize("hasRole('" + PermissionConstants.MANAGE_ROLES + "')")
    public ResponseEntity updateRole(
            @Valid @RequestBody RoleBackingForm form, @PathVariable Long id) {
        Set<Permission> permissions = setPermissions(form.getPermissionValues());
        Role role = form.getRole();
        role.setId(id);
        role.setName(form.getName());
        role.setPermissions(permissions);
        roleRepository.updateRole(role);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @RequestMapping(method = RequestMethod.POST)
    @PreAuthorize("hasRole('" + PermissionConstants.MANAGE_ROLES + "')")
    public ResponseEntity addRole(
            @Valid @RequestBody RoleBackingForm form) {
        Set<Permission> permissions = setPermissions(form.getPermissionValues());
        Role role = new Role();
        role.setName(form.getName());
        role.setDescription(form.getDescription());
        role.setPermissions(permissions);
        roleRepository.addRole(role);
        form = new RoleBackingForm();
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

	private Set<Permission> setPermissions(Set<String> permissionValues) {
         Set<Permission> permissions = new HashSet<Permission>();
		for (String permissionId : permissionValues) {
			if(!permissionId.equals("")){
			Permission permission = roleRepository
					.findPermissionByPermissionId(Long.parseLong(permissionId));
			permissions.add(permission);
			}
		}
		return permissions;
	}
        

}