package controller;

import backingform.UserBackingForm;
import backingform.validator.UserBackingFormValidator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityExistsException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import model.user.Role;
import model.user.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import repository.RoleRepository;
import repository.UserRepository;
import utils.PermissionConstants;
import viewmodel.UserViewModel;

@Controller
@RequestMapping("/user")
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

  @RequestMapping(value="/configureForm", method=RequestMethod.GET)
  @PreAuthorize("hasRole('"+PermissionConstants.MANAGE_USERS+"')")
  public @ResponseBody Map<String, Object> configureUsersFormGenerator(HttpServletRequest request) {

    Map<String, Object> map = new HashMap<String, Object>();
    addAllUsersToModel(map);
    map.put("refreshUrl", utilController.getUrl(request));
    map.put("userRoles", roleRepository.getAllRoles());
    return map;
  }

  private void addAllUsersToModel(Map<String, Object> m) {
    List<UserViewModel> users = userRepository.getAllUsers();
    m.put("allUsers", users);
  }

  @RequestMapping(value = "/editForm", method = RequestMethod.GET)
  @PreAuthorize("hasRole('"+PermissionConstants.MANAGE_USERS+"')")
  public @ResponseBody Map<String, Object> editUserFormGenerator(HttpServletRequest request,
      @RequestParam(value = "userId", required = false) Integer userId) {
	 UserBackingForm form = new UserBackingForm();
    Map<String, Object> map = new HashMap<String, Object>();
    map.put("requestUrl", utilController.getUrl(request));
    if (userId != null) {
      form.setId(userId);
      User user = userRepository.findUserById(userId);
      if (user != null) {
        form = new UserBackingForm(user);
        form.setCurrentPassword(user.getPassword());
        map.put("userRoles", roleRepository.getAllRoles());
        map.put("existingUser", true);
      }
      else {
    	form = new UserBackingForm();
        map.put("existingUser", false);
      }
    }
    map.put("allRoles",roleRepository.getAllRoles());
    map.put("userRoles", form.getRoles());
    map.put("editUserForm", form);
    map.put("refreshUrl", utilController.getUrl(request));
    // to ensure custom field names are displayed in the form
    return map;
  }

  @RequestMapping(method = RequestMethod.POST)
  @PreAuthorize("hasRole('"+PermissionConstants.MANAGE_USERS+"')")
  public @ResponseBody Map<String, Object>
        addUser(HttpServletRequest request,
                 HttpServletResponse response,
                 @Valid UserBackingForm form) {
    Map<String, Object> map = new HashMap<String, Object>();
    boolean success = false;
    
    String message = "";
   
      try {
        User user = form.getUser();
        user.setIsDeleted(false);
        user.setRoles(assignUserRoles(form));
        user.setIsActive(true);
        userRepository.addUser(user);
        map.put("hasErrors", false);
        success = true;
        message = "User Successfully Added";
        form = new UserBackingForm();
      } catch (EntityExistsException ex) {
        ex.printStackTrace();
        success = false;
        message = "User Already exists.";
    }
    map.put("allRoles", roleRepository.getAllRoles());
    map.put("editUserForm", form);
    map.put("existingUser", false);
    map.put("refreshUrl", "editUserFormGenerator.html");
    map.put("success", success);
    map.put("errorMessage", message);

    return map;
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
  @RequestMapping( method = RequestMethod.PUT)
  @PreAuthorize("hasRole('"+PermissionConstants.MANAGE_USERS+"')")
  public Map<String, Object> updateUser(
      HttpServletResponse response,
      @Valid UserBackingForm form) {

    Map<String, Object> map = new HashMap<String, Object>();
    boolean success = false;
    String message = "";
    // only when the collection is correctly added the existingCollectedSample
    // property will be changed
    map.put("existingUser", true);
  
   
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
          map.put("hasErrors", true);
          response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
          success = false;
          map.put("existingUser", false);
      }
        else {
          map.put("hasErrors", false);
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
    
 //   m.put("userRoles", form.getUserRole());
    map.put("editUserForm", form);
    map.put("success", success);
    map.put("errorMessage", message);

    return map;
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
        
    @ExceptionHandler
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ResponseBody
    public Map<String, String> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException errors) {
        Map<String, String> errorMap = new HashMap<String, String>();
        errorMap.put("hasErrors", "true");
        errorMap.put("errorMessage", errors.getMessage());
        errors.printStackTrace();
        for (FieldError error : errors.getBindingResult().getFieldErrors()){
                errorMap.put(error.getField(), error.getDefaultMessage());
            }
      
        return errorMap;
    }
}