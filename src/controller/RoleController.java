package controller;

import backingform.RoleBackingForm;
import backingform.validator.RoleBackingFormValidator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.persistence.EntityExistsException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import model.user.Permission;
import model.user.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import repository.RoleRepository;
import utils.PermissionConstants;
import viewmodel.RoleViewModel;

@Controller
@RequestMapping("/role")
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

	@RequestMapping(value = "/configureForm", method = RequestMethod.GET)
	  @PreAuthorize("hasRole('"+PermissionConstants.MANAGE_ROLES+"')")
	public @ResponseBody Map<String, Object> configureRolesFormGenerator(HttpServletRequest request) {

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

	@RequestMapping(value = "/editForm", method = RequestMethod.GET)
	@PreAuthorize("hasRole('"+PermissionConstants.MANAGE_ROLES+"')")
	public @ResponseBody Map<String, Object> editRoleFormGenerator(HttpServletRequest request,
			@RequestParam(value = "roleId", required = false) Long roleId) {

		RoleBackingForm form = new RoleBackingForm();
                Map<String, Object> map = new HashMap<String, Object>();
		map.put("requestUrl", utilController.getUrl(request));
		if (roleId != null) {
			form.setId(roleId);
			Role role = roleRepository.findRoleDetailById(roleId);
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

	@RequestMapping(method = RequestMethod.PUT)
    @PreAuthorize("hasRole('"+PermissionConstants.MANAGE_ROLES+"')")
	public @ResponseBody Map<String, Object> updateRole(
			HttpServletRequest request,
			HttpServletResponse response,
                        @Valid @RequestBody RoleBackingForm form) {
		
                Map<String, Object> map = new HashMap<String, Object>();
		boolean success = false;
		String message = "";
		map.put("existingRole", true);
		
		try {

			Set<Permission> permissions = setPermissions(form.getPermissionValues());
			Role role = form.getRole();
			role.setName(form.getName());
			role.setPermissions(permissions);
        	Role existingRole = roleRepository.updateRole(role);
			if (existingRole == null) {
				map.put("hasErrors", true);
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				success = false;
				map.put("existingRole", false);
				message = "Role does not already exist.";
			} else {
				map.put("hasErrors", false);
				success = true;
				message = "Role Successfully Updated";
			}
		} catch (EntityExistsException ex) {
			ex.printStackTrace();
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			success = false;
			message = "Role Already exists.";
		} catch (Exception ex) {
			ex.printStackTrace();
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			success = false;
			message = "Internal Error. Please try again or report a Problemap.";
		}

		addAllPermissionsToModel(map);
		map.put("editRoleForm", form);
		map.put("success", success);
		map.put("errorMessage", message);
		return map;
	}

	@RequestMapping(method = RequestMethod.POST)
	@PreAuthorize("hasRole('"+PermissionConstants.MANAGE_ROLES+"')")
	public @ResponseBody Map<String, Object> addRole(HttpServletRequest request,
			HttpServletResponse response,
                        @Valid @RequestBody RoleBackingForm form) {
		
            Map<String, Object> map = new HashMap<String, Object>();
            boolean success = false;

	    Role savedRole = null;
	  
			try {
				
                        	Set<Permission> permissions = setPermissions(form.getPermissionValues());
				Role role =new Role();
				role.setName(form.getName());
				role.setDescription(form.getDescription());
				role.setPermissions(permissions);
				savedRole = roleRepository.addRole(role);
				map.put("hasErrors", false);
				success = true;
				form = new RoleBackingForm();
			} catch (EntityExistsException ex) {
				ex.printStackTrace();
				success = false;
			} catch (Exception ex) {
				ex.printStackTrace();
				success = false;
			}
		addAllPermissionsToModel(map);

            if (success) {
                map.put("editRoleForm", form);
                map.put("existingRole", false);
                map.put("refreshUrl", "configureRolesFormGenerator.html");

            } else {

                map.put("editRoleForm", form);
                map.put("refreshUrl", "admin/editRoleForm");
                map.put("errorMessage", "Error creating new Role. Please fix the errors noted below.");

            }
		map.put("success", success);
        return map;
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
        
    @ExceptionHandler
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ResponseBody
    public Map<String, String> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException errors) {
        Map<String, String> errorMap = new HashMap<String, String>();
        errorMap.put("hasErrors", "true");
        errorMap.put("errorMessage", errors.getMessage());
        errors.printStackTrace();
        for (FieldError error : errors.getBindingResult().getFieldErrors()) {
            errorMap.put(error.getField(), error.getDefaultMessage());
        }
        return errorMap;
    }
}