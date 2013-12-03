package controller;

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

  @InitBinder
  protected void initBinder(WebDataBinder binder) {
    binder.setValidator(new UserBackingFormValidator(binder.getValidator(), utilController));
  }

  @RequestMapping(value="/configureUsersFormGenerator", method=RequestMethod.GET)
  public ModelAndView configureUsersFormGenerator(
      HttpServletRequest request, HttpServletResponse response,
      Model model) {

    ModelAndView mv = new ModelAndView("admin/configureUsers");
    Map<String, Object> m = model.asMap();
    addAllUsersToModel(m);
    m.put("refreshUrl", utilController.getUrl(request));
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
        m.put("userRoles", form.getUserRole());
        m.put("existingUser", true);
      }
      else {
        form = new UserBackingForm();
        m.put("existingUser", false);
      }
    }
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
      message = "Please fix the errors noted above.";
    } else {
      try {
        User user = form.getUser();
        user.setIsDeleted(false);
        user.setRoles(userRepository.getUserRole(userRole(form)));
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

    m.put("editUserForm", form);
    m.put("existingUser", false);
    m.put("refreshUrl", "editUserFormGenerator.html");
    m.put("success", success);
    m.put("message", message);

    mv.addObject("model", m);
    return mv;
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
      m.put("hasErrors", true);
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      success = false;
      message = "Please fix the errors noted above";
    }
    else {
      try {
        form.setIsDeleted(false);
        User user = form.getUser();
        if (form.getModifyPassword())
          user.setPassword(form.getPassword());
        user.setRoles(userRepository.getUserRole(userRole(form)));
        User existingUser = userRepository.updateUser(user, true);
        if (existingUser == null) {
          m.put("hasErrors", true);
          response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
          success = false;
          m.put("existingUser", false);
          message = "User does not already exist.";
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
    
    m.put("userRoles", form.getUserRole());
    m.put("editUserForm", form);
    m.put("success", success);
    m.put("message", message);

    mv.addObject("model", m);

    return mv;
  }
  
	public String[] userRole(UserBackingForm form){
  	String []str = new String[4];
    if(form.getRoleAdmin()!=null && StringUtils.isNotEmpty(form.getRoleAdmin()) && !form.getRoleAdmin().isEmpty()){
    	str[0]=form.getRoleAdmin();
    }
    if(form.getRoleDonorLab()!=null && StringUtils.isNotEmpty(form.getRoleDonorLab()) && !form.getRoleDonorLab().isEmpty()){
    	str[1]=form.getRoleDonorLab();
    }
    String strRoleTestLab=form.getRoleTestLab();
    if(strRoleTestLab !=null && StringUtils.isNotEmpty(form.getRoleTestLab()) && !form.getRoleTestLab().isEmpty()){
    	str[2]=form.getRoleTestLab();
    }
    if(StringUtils.isNotEmpty(form.getRoleUser()) && !form.getRoleUser().isEmpty()){
    	str[3]=form.getRoleUser();
    }
		return str;
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