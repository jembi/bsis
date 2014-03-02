package controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityExistsException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import model.user.Role;
import model.user.User;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import repository.RoleRepository;
import repository.UserRepository;
import viewmodel.UserViewModel;
import backingform.UserBackingForm;
import backingform.validator.UserBackingFormValidator;

@Controller
public class UserController {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private UtilController utilController;

  @Autowired
  private RoleRepository roleRepository;
  
  @InitBinder
  protected void initBinder(WebDataBinder binder) {
    binder.setValidator(new UserBackingFormValidator(binder.getValidator(), utilController,userRepository));
  }

  @RequestMapping(value="/configureUsersFormGenerator", method=RequestMethod.GET)
  public ModelAndView configureUsersFormGenerator(
      HttpServletRequest request, HttpServletResponse response,
      Model model) {

    ModelAndView mv = new ModelAndView("admin/configureUsers");
    Map<String, Object> m = model.asMap();
    addAllUsersToModel(m);
    m.put("refreshUrl", utilController.getUrl(request));
    m.put("userRoles", roleRepository.getAllRoles());
    mv.addObject("model", model);
    return mv;
  }

  private void addAllUsersToModel(Map<String, Object> m) {
    List<UserViewModel> users = userRepository.getAllUsers();
    m.put("allUsers", users);
  }

  @RequestMapping(value = "/editUserFormGenerator", method = RequestMethod.GET)
  public ModelAndView editUserFormGenerator(HttpServletRequest request, Model model,
      @RequestParam(value = "userId", required = false) Integer userId) {
	 UserBackingForm form = new UserBackingForm();
    ModelAndView mv = new ModelAndView("admin/editUserForm");
    Map<String, Object> m = model.asMap();
    m.put("requestUrl", utilController.getUrl(request));
    if (userId != null) {
      form.setId(userId);
      User user = userRepository.findUserById(userId);
      if (user != null) {
        form = new UserBackingForm(user);
        form.setCurrentPassword(user.getPassword());
        m.put("userRoles", roleRepository.getAllRoles());
        m.put("existingUser", true);
      }
      else {
    	form = new UserBackingForm();
        m.put("existingUser", false);
      }
    }
    m.put("allRoles",roleRepository.getAllRoles());
    m.put("userRoles", form.getRoles());
    m.put("editUserForm", form);
    m.put("refreshUrl", utilController.getUrl(request));
    // to ensure custom field names are displayed in the form
    mv.addObject("model", m);
    return mv;
  }

  @RequestMapping(value = "/addUser", method = RequestMethod.POST)
  public ModelAndView
        addUser(HttpServletRequest request,
                 HttpServletResponse response,
                 @ModelAttribute("editUserForm") @Valid UserBackingForm form,
                 BindingResult result, Model model) {
    ModelAndView mv = new ModelAndView("admin/editUserForm");
    boolean success = false;
    
    String message = "";
    Map<String, Object> m = model.asMap();

    if (result.hasErrors()) {
      m.put("hasErrors", true);
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);      
      success = false;
      message = "Error creating new User. Please fix the errors noted below.";
    } else {
      try {
        User user = form.getUser();
        user.setIsDeleted(false);
        user.setRoles(assignUserRoles(form));
        user.setIsActive(true);
        userRepository.addUser(user);
        m.put("hasErrors", false);
        success = true;
        message = "User Successfully Added";
        form = new UserBackingForm();
      } catch (EntityExistsException ex) {
        ex.printStackTrace();
        success = false;
        message = "User Already exists.";
      } catch (Exception ex) {
        ex.printStackTrace();
        success = false;
        message = "Internal Error. Please try again or report a Problem.";
      }
    }
    m.put("allRoles", roleRepository.getAllRoles());
    m.put("editUserForm", form);
    m.put("existingUser", false);
    m.put("refreshUrl", "editUserFormGenerator.html");
    m.put("success", success);
    m.put("errorMessage", message);

    mv.addObject("model", m);
    return mv;
  }
   
  public List<Role> assignUserRoles(UserBackingForm userForm)
  {
	   List<String> userRoles=userForm.getUserRoles();
       List<Role> roles=new ArrayList<Role>();
       for(String roleId : userRoles)
       {
       	roles.add(userRepository.findRoleById(Long.parseLong(roleId)));
       }
       return roles;
  }
  @RequestMapping(value = "/updateUser", method = RequestMethod.POST)
  public ModelAndView updateUser(
      HttpServletResponse response,
      @ModelAttribute(value="editUserForm") @Valid UserBackingForm form,
      BindingResult result, Model model) {

    ModelAndView mv = new ModelAndView("admin/editUserForm");
    boolean success = false;
    String message = "";
    Map<String, Object> m = model.asMap();
    // only when the collection is correctly added the existingCollectedSample
    // property will be changed
    m.put("existingUser", true);
    if (result.hasErrors()) {
      m.put("allRoles", roleRepository.getAllRoles());
      m.put("hasErrors", true);
      form.setModifyPassword(false);
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      success = false;
      message = "Error Updating user. Please fix the errors noted below";
    }
    else {
      try {
        form.setIsDeleted(false);
        User user = form.getUser();
        if (form.isModifyPassword())
          user.setPassword(form.getPassword());
        else
        	user.setPassword(form.getCurrentPassword());
        user.setRoles(assignUserRoles(form));
        user.setIsActive(true);
        User existingUser = userRepository.updateUser(user, true);
        if (existingUser == null) {
          m.put("hasErrors", true);
          response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
          success = false;
          m.put("existingUser", false);
      }
        else {
          m.put("hasErrors", false);
          success = true;
          message = "User Successfully Updated";
        }
      } catch (EntityExistsException ex) {
        ex.printStackTrace();
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        success = false;
        message = "User Already exists.";
      } catch (Exception ex) {
        ex.printStackTrace();
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        success = false;
        message = "Internal Error. Please try again or report a Problem.";
      }
   }
    
 //   m.put("userRoles", form.getUserRole());
    m.put("editUserForm", form);
    m.put("success", success);
    m.put("errorMessage", message);

    mv.addObject("model", m);

    return mv;
  }
  
	
	
	public String userRole(Integer id) {
		String userRole = "";
		User user=userRepository.findUserById(id);
		List<Role> roles=user.getRoles();
		if(roles!=null && roles.size() > 0){
			for(Role r:roles){
				userRole= userRole +" "+ r.getId();
			}
			
		}
		return userRole;
	}
}