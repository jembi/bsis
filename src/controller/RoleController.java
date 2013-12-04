package controller;

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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import repository.RoleRepository;
import viewmodel.RoleViewModel;
import backingform.RoleBackingForm;

@Controller
public class RoleController {

	@Autowired
	private UtilController utilController;

	@Autowired
	private RoleRepository roleRepository;

	@RequestMapping(value = "/configureRolesFormGenerator", method = RequestMethod.GET)
	public ModelAndView configureRolesFormGenerator(HttpServletRequest request,
			HttpServletResponse response, Model model) {

		ModelAndView mv = new ModelAndView("admin/configureRoles");
		Map<String, Object> m = model.asMap();
		addAllRolesrsToModel(m);
		m.put("refreshUrl", utilController.getUrl(request));
		mv.addObject("model", model);
		return mv;
	}

	private void addAllRolesrsToModel(Map<String, Object> m) {
		List<RoleViewModel> roles = roleRepository.getAllRules();
		m.put("allRules", roles);
	}

	private void addAllPermissionToModel(Map<String, Object> m) {
		List<Permission> permissions = roleRepository.getAllPermission();
		m.put("allPermission", permissions);
	}

	@RequestMapping(value = "/editRoleFormGenerator", method = RequestMethod.GET)
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
			form.setRole(role);
			m.put("existingRole", true);
		} else {
			form = new RoleBackingForm();
			m.put("existingRole", false);

		}
		addAllPermissionToModel(m);
		m.put("editRoleForm", form);
		m.put("refreshUrl", utilController.getUrl(request));
		// to ensure custom field names are displayed in the form
		mv.addObject("model", m);
		return mv;
	}

	@RequestMapping(value = "/updateRole", method = RequestMethod.POST)
	public ModelAndView updateUser(
			HttpServletRequest request,
			HttpServletResponse response,
			@ModelAttribute(value = "editRoleForm") @Valid RoleBackingForm form,
			BindingResult result,
			Model model,
			@RequestParam(value = "permissiondata", required = false) String permissiondata) {
		String[] splitpermission = permissiondata.split("~");
		Set<Permission> permissions = setPermissionValues(splitpermission);
		ModelAndView mv = new ModelAndView("admin/editRoleForm");
		boolean success = false;
		String message = "";
		Map<String, Object> m = model.asMap();
		m.put("existingRole", true);
		try {

			Role role = form.getRole();
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

		addAllPermissionToModel(m);
		m.put("editRoleForm", form);
		m.put("success", success);
		m.put("message", message);

		mv.addObject("model", m);

		return mv;
	}

	@RequestMapping(value = "/addRole", method = RequestMethod.POST)
	public @ResponseBody Map<String, ? extends Object> addRole(HttpServletRequest request,
			HttpServletResponse response,
			@ModelAttribute("editRoleForm") @Valid RoleBackingForm form,
			BindingResult result, Model model,@RequestParam(value = "permissiondata", required = false) String permissiondata) {

		boolean success = false;
		String message = "";
		Map<String, Object> m = model.asMap();
		String[] splitpermission = permissiondata.split("~");
		Set<Permission> permissions = setPermissionValues(splitpermission);
		try {
			Role role = form.getRole();
			role.setPermissions(permissions);
			roleRepository.addRole(role);
			m.put("hasErrors", false);
			success = true;
			message = "Role Successfully Added";
			form = new RoleBackingForm();
		} catch (EntityExistsException ex) {
			ex.printStackTrace();
			success = false;
			message = "Role Already exists.";
		} catch (Exception ex) {
			ex.printStackTrace();
			success = false;
			message = "Internal Error. Please try again or report a Problem.";
		}

		m.put("editRoleForm", form);
		m.put("existingRole", false);
		m.put("refreshUrl", "configureRolesFormGenerator.html");
		m.put("success", success);
		m.put("message", message);

		
		return m;
	}

	private Set<Permission> setPermissionValues(String[] splitPermissionArr) {
		Set<Permission> permissions = new HashSet<Permission>();
		for (String permissionId : splitPermissionArr) {
			Permission permission = roleRepository
					.findPermissionByPermissionId(Long.parseLong(permissionId));
			permissions.add(permission);
		}
		return permissions;
	}
}