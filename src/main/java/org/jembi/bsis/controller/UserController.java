package org.jembi.bsis.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.jembi.bsis.backingform.UserBackingForm;
import org.jembi.bsis.backingform.validator.UserBackingFormValidator;
import org.jembi.bsis.factory.RoleFactory;
import org.jembi.bsis.factory.UserFactory;
import org.jembi.bsis.model.user.User;
import org.jembi.bsis.repository.RoleRepository;
import org.jembi.bsis.repository.UserRepository;
import org.jembi.bsis.service.UserCRUDService;
import org.jembi.bsis.utils.PermissionConstants;
import org.jembi.bsis.viewmodel.UserViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private RoleRepository roleRepository;

  @Autowired
  private UserBackingFormValidator userBackingFormValidator;

  @Autowired
  private UserCRUDService userCRUDService;

  @Autowired
  private UserFactory userFactory;

  @Autowired
  private RoleFactory roleFactory;

  @InitBinder
  protected void initBinder(WebDataBinder binder) {
    binder.setValidator(userBackingFormValidator);
  }

  @RequestMapping(method = RequestMethod.GET)
  @PreAuthorize("hasRole('" + PermissionConstants.MANAGE_USERS + "')")
  public Map<String, Object> configureUsersFormGenerator(HttpServletRequest request) {
    Map<String, Object> map = new HashMap<String, Object>();
    map.put("users", userFactory.createViewModels(userRepository.getAllUsers()));
    map.put("roles", roleFactory.createViewModels(roleRepository.getAllRoles()));
    return map;
  }

  @RequestMapping(value = "{id}", method = RequestMethod.GET)
  @PreAuthorize("hasRole('" + PermissionConstants.MANAGE_USERS + "')")
  public Map<String, Object> getUserDetails(@PathVariable UUID id) {
    Map<String, Object> map = new HashMap<String, Object>();
    User user = userRepository.findUserById(id);
    map.put("user", userFactory.createViewModel(user));
    return map;
  }


  @RequestMapping(method = RequestMethod.POST)
  @PreAuthorize("hasRole('" + PermissionConstants.MANAGE_USERS + "')")
  @ResponseStatus(HttpStatus.CREATED)
  public UserViewModel addUser(@Valid @RequestBody UserBackingForm form) {
    User user = userFactory.createEntity(form);
    String hashedPassword = getHashedPassword(user.getPassword());
    user.setPassword(hashedPassword);
    user.setIsDeleted(false);
    user.setIsActive(true);
    user = userRepository.addUser(user);
    return userFactory.createViewModel(user);
  }

  @RequestMapping(value = "{id}", method = RequestMethod.PUT)
  @PreAuthorize("hasRole('" + PermissionConstants.MANAGE_USERS + "')")
  public UserViewModel updateUser(@Valid @RequestBody UserBackingForm form, @PathVariable UUID id) {
    form.setIsDeleted(false);
    User user = userFactory.createEntity(form);
    user.setId(id);
    boolean modifyPassword = form.isModifyPassword();
    if (modifyPassword) {
      String hashedPassword = getHashedPassword(user.getPassword());
      user.setPassword(hashedPassword);
      user.setPasswordReset(false);
    }
    user.setIsActive(true);
    userRepository.updateUser(user, modifyPassword);
    return userFactory.createViewModel(user);
  }


  @RequestMapping(method = RequestMethod.PUT)
  @PreAuthorize("hasRole('" + PermissionConstants.AUTHENTICATED + "')")
  public UserViewModel updateLoginUserInfo(@Valid @RequestBody UserBackingForm form) {
    User user = userFactory.createEntity(form);
    user.setId(getLoginUser().getId());
    boolean modifyPassword = form.isModifyPassword();
    if (modifyPassword) {
      String hashedPassword = getHashedPassword(user.getPassword());
      user.setPassword(hashedPassword);
      user.setPasswordReset(false);
    }
    userRepository.updateBasicUserInfo(user, modifyPassword);
    return userFactory.createViewModel(user);
  }


  @RequestMapping(value = "/login-user-details", method = RequestMethod.GET)
  @PreAuthorize("hasRole('" + PermissionConstants.AUTHENTICATED + "' )")
  public UserViewModel getUserDetails() {
    return userFactory.createViewModel(getLoginUser());
  }

  @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
  @PreAuthorize("hasRole('" + PermissionConstants.MANAGE_USERS + "')")
  @ResponseStatus(value = HttpStatus.NO_CONTENT)
  public void deleteUser(@PathVariable UUID id) {
    userCRUDService.deleteUser(id);
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
