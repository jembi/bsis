package controller;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityExistsException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import model.collectedsample.CollectedSample;
import model.user.Permission;
import model.user.Role;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import repository.RoleRepository;
import utils.PermissionConstants;
import viewmodel.RoleViewModel;
import backingform.RoleBackingForm;
import backingform.validator.DonorBackingFormValidator;
import backingform.validator.RoleBackingFormValidator;

@Controller
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

	@RequestMapping(value = "/configureRolesFormGenerator", method = RequestMethod.GET)
	  @PreAuthorize("hasRole('"+PermissionConstants.MANAGE_ROLES+"')")
	public ModelAndView configureRolesFormGenerator(HttpServletRequest request,
			HttpServletResponse response, Model model) {

		ModelAndView mv = new ModelAndView("admin/configureRoles");
		Map<String, Object> m = model.asMap();
		addAllRolesToModel(m);
		m.put("refreshUrl", utilController.getUrl(request));
		mv.addObject("model", model);
		return mv;
	}

	private void addAllRolesToModel(Map<String, Object> m) {
		List<RoleViewModel> roles = roleRepository.getAllRoles();
		m.put("allRoles", roles);
	}

	private void addAllPermissionsToModel(Map<String, Object> m) {
		List<Permission> permissions = roleRepository.getAllPermissionsByName();
		m.put("allPermissions", permissions);
	}

	@RequestMapping(value = "/editRoleFormGenerator", method = RequestMethod.GET)
	@PreAuthorize("hasRole('"+PermissionConstants.MANAGE_ROLES+"')")
	public ModelAndView editRoleFormGenerator(HttpServletRequest request,
			Model model,
			@RequestParam(value = "roleId", required = false) Long roleId) {

		RoleBackingForm form = new RoleBackingForm();
		ModelAndView mv = new ModelAndView("admin/editRoleForm");
		Map<String, Object> m = model.asMap();
		m.put("requestUrl", utilController.getUrl(request));
		if (roleId != null) {
			form.setId(roleId);
			Role role = roleRepository.findRoleDetailById(roleId);
			form.setName(role.getName());
			form.setRole(role);
			m.put("existingRole", true);
		} else {
			form = new RoleBackingForm();
			m.put("existingRole", false);

		}
		addAllPermissionsToModel(m);
		m.put("editRoleForm", form);
		m.put("refreshUrl", utilController.getUrl(request));
		// to ensure custom field names are displayed in the form
		mv.addObject("model", m);
		return mv;
	}

	@RequestMapping(value = "/updateRole", method = RequestMethod.POST)
    @PreAuthorize("hasRole('"+PermissionConstants.MANAGE_ROLES+"')")
	public ModelAndView updateRole(
			HttpServletRequest request,
			HttpServletResponse response,
			@ModelAttribute(value = "editRoleForm") @Valid RoleBackingForm form,
			BindingResult result,
			Model model) {
		
		ModelAndView mv = new ModelAndView("admin/editRoleForm");
		boolean success = false;
		String message = "";
		Map<String, Object> m = model.asMap();
		m.put("existingRole", true);
		 if (result.hasErrors()) {
		      m.put("hasErrors", true);
		      message = "Error Updating  Role. Please fix the errors noted below.";
		      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		      success = false;
		    } else
		try {

			Set<Permission> permissions = setPermissions(form.getPermissionValues());
			Role role = form.getRole();
			role.setName(form.getName());
			role.setPermissions(permissions);
        	Role existingRole = roleRepository.updateRole(role);
			if (existingRole == null) {
				m.put("hasErrors", true);
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				success = false;
				m.put("existingRole", false);
				message = "Role does not already exist.";
			} else {
				m.put("hasErrors", false);
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
			message = "Internal Error. Please try again or report a Problem.";
		}

		addAllPermissionsToModel(m);
		m.put("editRoleForm", form);
		m.put("success", success);
		m.put("errorMessage", message);

		mv.addObject("model", m);

		return mv;
	}

	@RequestMapping(value = "/addRole", method = RequestMethod.POST)
	@PreAuthorize("hasRole('"+PermissionConstants.MANAGE_ROLES+"')")
	public ModelAndView addRole(HttpServletRequest request,
			HttpServletResponse response,
			@ModelAttribute("editRoleForm") @Valid RoleBackingForm form,
			BindingResult result, Model model) {
		
		ModelAndView mv = new ModelAndView("admin/editRoleForm");
		Map<String, Object> m = model.asMap();
	    boolean success = false;
		
	    Role savedRole = null;
	    if (result.hasErrors()) {
	      m.put("hasErrors", true);
	      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
	      success = false;
	    } else {
			try {
				
	    		Set<Permission> permissions = setPermissions(form.getPermissionValues());
				Role role =new Role();
				role.setName(form.getName());
				role.setDescription(form.getDescription());
				role.setPermissions(permissions);
				savedRole = roleRepository.addRole(role);
				m.put("hasErrors", false);
				success = true;
				form = new RoleBackingForm();
			} catch (EntityExistsException ex) {
				ex.printStackTrace();
				success = false;
			} catch (Exception ex) {
				ex.printStackTrace();
				success = false;
			}
		}
		addAllPermissionsToModel(m);

		if (success) {
			m.put("editRoleForm", form);
			m.put("existingRole", false);
			m.put("refreshUrl", "configureRolesFormGenerator.html");
		
	    } else {
	    
	    	m.put("editRoleForm", form);
	    	m.put("refreshUrl", "admin/editRoleForm");
	     	m.put("errorMessage", "Error creating new Role. Please fix the errors noted below.");
	    

	    }
		mv.addObject("model", m);
		m.put("success", success);
        return mv;
	}

	private Set<Permission> setPermissions(Set<String> permissionValues) {
         Set<Permission> permissions = new HashSet<Permission>();
		for (String permissionId : permissionValues) {
		    System.out.println("\tpermissionId: "+permissionId);
			if(!permissionId.equals("")){
			Permission permission = roleRepository
					.findPermissionByPermissionId(Long.parseLong(permissionId));
			permissions.add(permission);
			}
		}
		return permissions;
	}
}