package controller;

import backingform.UserBackingForm;
import backingform.validator.UserBackingFormValidator;
import model.user.Role;
import model.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import repository.RoleRepository;
import repository.UserRepository;
import utils.PermissionConstants;
import viewmodel.UserViewModel;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private UtilController utilController;

  @Autowired
  private RoleRepository roleRepository;

  @InitBinder
  protected void initBinder(WebDataBinder binder) {
    binder.setValidator(new UserBackingFormValidator(binder.getValidator(), utilController, userRepository));
  }

  @RequestMapping(method = RequestMethod.GET)
  @PreAuthorize("hasRole('" + PermissionConstants.MANAGE_USERS + "')")
  public Map<String, Object> configureUsersFormGenerator(HttpServletRequest request) {

    Map<String, Object> map = new HashMap<>();
    addAllUsersToModel(map);
    map.put("roles", roleRepository.getAllRoles());
    return map;
  }

  @RequestMapping(value = "/roles", method = RequestMethod.GET)
  @PreAuthorize("hasRole('" + PermissionConstants.MANAGE_USERS + "')")
  public ResponseEntity getRoles() {
    UserBackingForm form = new UserBackingForm();
    Map<String, Object> map = new HashMap<>();
    map.put("roles", roleRepository.getAllRoles());
    return new ResponseEntity(map, HttpStatus.OK);
  }

  @RequestMapping(value = "{id}", method = RequestMethod.GET)
  @PreAuthorize("hasRole('" + PermissionConstants.MANAGE_USERS + "')")
  public ResponseEntity getUserDetails(@PathVariable Integer id) {
    Map<String, Object> map = new HashMap<>();
    User user = userRepository.findUserById(id);
    map.put("user", new UserViewModel(user));
    return new ResponseEntity(map, HttpStatus.OK);
  }


  @RequestMapping(method = RequestMethod.POST)
  @PreAuthorize("hasRole('" + PermissionConstants.MANAGE_USERS + "')")
  public ResponseEntity
  addUser(@Valid @RequestBody UserBackingForm form) {

    User user = form.getUser();
    String hashedPassword = getHashedPassword(user.getPassword());
    user.setPassword(hashedPassword);
    user.setIsDeleted(false);
    user.setIsActive(true);
    user = userRepository.addUser(user);
    return new ResponseEntity(new UserViewModel(user), HttpStatus.CREATED);
  }

  @RequestMapping(value = "{id}", method = RequestMethod.PUT)
  @PreAuthorize("hasRole('" + PermissionConstants.MANAGE_USERS + "')")
  public ResponseEntity updateUser(
          @Valid @RequestBody UserBackingForm form,
          @PathVariable Integer id) {

    form.setIsDeleted(false);
    User user = form.getUser();
    user.setId(id);
    boolean modifyPassword = form.isModifyPassword();
    if (modifyPassword) {
      String hashedPassword = getHashedPassword(user.getPassword());
      user.setPassword(hashedPassword);
      user.setPasswordReset(false);
    }
    user.setIsActive(true);
    userRepository.updateUser(user, modifyPassword);

    return new ResponseEntity(user, HttpStatus.OK);
  }


  @RequestMapping(method = RequestMethod.PUT)
  @PreAuthorize("hasRole('" + PermissionConstants.AUTHENTICATED + "')")
  public ResponseEntity<UserViewModel> updateLoginUserInfo(
          @Valid @RequestBody UserBackingForm form) {

    User user = form.getUser();
    user.setId(getLoginUser().getId());
    boolean modifyPassword = form.isModifyPassword();
    if (modifyPassword) {
      String hashedPassword = getHashedPassword(user.getPassword());
      user.setPassword(hashedPassword);
      user.setPasswordReset(false);
    }
    userRepository.updateBasicUserInfo(user, modifyPassword);
    return new ResponseEntity<>(new UserViewModel(user), HttpStatus.OK);
  }


  @RequestMapping(value = "/login-user-details", method = RequestMethod.GET)
  @PreAuthorize("hasRole('" + PermissionConstants.AUTHENTICATED + "' )")
  public ResponseEntity getUserDetails() {
    User user = getLoginUser();
    return new ResponseEntity(new UserViewModel(user), HttpStatus.OK);
  }

  @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
  @PreAuthorize("hasRole('" + PermissionConstants.MANAGE_USERS + "')")
  public ResponseEntity deleteUser(@PathVariable Integer id) {

    userRepository.deleteUserById(id);
    return new ResponseEntity(HttpStatus.NO_CONTENT);
  }

  private void addAllUsersToModel(Map<String, Object> m) {
    List<UserViewModel> users = userRepository.getAllUsers();
    m.put("users", users);
  }

  public String userRole(Integer id) {
    String userRole = "";
    User user = userRepository.findUserById(id);
    List<Role> roles = user.getRoles();
    if (roles != null && roles.size() > 0) {
      for (Role r : roles) {
        userRole = userRole + " " + r.getId();
      }

    }
    return userRole;
  }

  private User getLoginUser() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    String userName = auth.getName(); //get logged in username
    return userRepository.findUser(userName);
  }

  private String getHashedPassword(String rawPassword) {
    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    String hashedPassword = passwordEncoder.encode(rawPassword);
    return hashedPassword;
  }

}
