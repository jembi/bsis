package org.jembi.bsis.backingform.validator;

import static org.jembi.bsis.helpers.builders.RoleBackingFormBuilder.aRoleBackingForm;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.HashMap;
import java.util.UUID;

import org.jembi.bsis.backingform.RoleBackingForm;
import org.jembi.bsis.backingform.UserBackingForm;
import org.jembi.bsis.helpers.builders.UserBuilder;
import org.jembi.bsis.model.user.User;
import org.jembi.bsis.repository.FormFieldRepository;
import org.jembi.bsis.repository.UserRepository;
import org.jembi.bsis.suites.UnitTestSuite;
import org.jembi.bsis.utils.PermissionConstants;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.validation.Errors;
import org.springframework.validation.MapBindingResult;

public class UserBackingFormValidatorTest extends UnitTestSuite {

  @InjectMocks
  UserBackingFormValidator validator;
  @Mock
  UserRepository userRepository;
  @Mock
  FormFieldRepository formFieldRepository;

  @Test
  public void testDuplicateUserName() throws Exception {
    // set up data
    UserBackingForm form = new UserBackingForm();
    form.setUsername(USERNAME);
    form.setPassword("password");
    form.setConfirmPassword("password");
    form.setRoles(Arrays.asList(aRoleBackingForm().build()));

    // set up mocks
    when(formFieldRepository.getRequiredFormFields("user")).thenReturn(Arrays.asList(new String[]{}));
    when(formFieldRepository.getFieldMaxLengths("user")).thenReturn(new HashMap<String, Integer>());
    when(userRepository.findUser(USERNAME)).thenReturn(loggedInUser);

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "user");
    validator.validate(form, errors);

    // check asserts
    Assert.assertEquals("Errors exist", 1, errors.getErrorCount());
    Assert.assertNotNull("Error: duplicate username", errors.getFieldError("username"));
  }

  @Test
  public void testInvalidUserName1() throws Exception {
    // set up data
    UserBackingForm form = new UserBackingForm();
    form.setUsername("a");
    form.setPassword("password");
    form.setConfirmPassword("password");
    form.setRoles(Arrays.asList(aRoleBackingForm().build()));

    // set up mocks
    when(formFieldRepository.getRequiredFormFields("user")).thenReturn(Arrays.asList(new String[]{}));
    when(formFieldRepository.getFieldMaxLengths("user")).thenReturn(new HashMap<String, Integer>());
    when(userRepository.findUser(USERNAME)).thenReturn(null);

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "user");
    validator.validate(form, errors);

    // check asserts
    Assert.assertEquals("Errors exist", 1, errors.getErrorCount());
    Assert.assertNotNull("Error: username too short", errors.getFieldError("username"));
  }

  @Test
  public void testInvalidUserName2() throws Exception {
    // set up data
    UserBackingForm form = new UserBackingForm();
    form.setUsername("adminadminadminadminadminadminadminadminadminadminadminadminadminadminadminadminadminadminadminadminadminadminadminadminadminadminadminadminadminadminadminadmin");
    form.setPassword("password");
    form.setConfirmPassword("password");
    form.setRoles(Arrays.asList(aRoleBackingForm().build()));

    // set up mocks
    when(formFieldRepository.getRequiredFormFields("user")).thenReturn(Arrays.asList(new String[]{}));
    when(formFieldRepository.getFieldMaxLengths("user")).thenReturn(new HashMap<String, Integer>());
    when(userRepository.findUser(USERNAME)).thenReturn(null);

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "user");
    validator.validate(form, errors);

    // check asserts
    Assert.assertEquals("Errors exist", 1, errors.getErrorCount());
    Assert.assertNotNull("Error: username too long", errors.getFieldError("username"));
  }

  @Test
  public void testInvalidUserName3() throws Exception {
    // set up data
    UserBackingForm form = new UserBackingForm();
    form.setUsername("adm%n");
    form.setPassword("password");
    form.setConfirmPassword("password");
    form.setRoles(Arrays.asList(aRoleBackingForm().build()));

    // set up mocks
    when(formFieldRepository.getRequiredFormFields("user")).thenReturn(Arrays.asList(new String[]{}));
    when(formFieldRepository.getFieldMaxLengths("user")).thenReturn(new HashMap<String, Integer>());
    when(userRepository.findUser(USERNAME)).thenReturn(null);

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "user");
    validator.validate(form, errors);

    // check asserts
    Assert.assertEquals("Errors exist", 1, errors.getErrorCount());
    Assert.assertNotNull("Error: username contains %", errors.getFieldError("username"));
  }

  @Test
  public void testValidUserName() throws Exception {
    // set up data
    UserBackingForm form = new UserBackingForm();
    form.setUsername(USERNAME);
    form.setPassword("password");
    form.setConfirmPassword("password");
    form.setRoles(Arrays.asList(aRoleBackingForm().build()));

    // set up mocks
    when(formFieldRepository.getRequiredFormFields("user")).thenReturn(Arrays.asList(new String[]{}));
    when(formFieldRepository.getFieldMaxLengths("user")).thenReturn(new HashMap<String, Integer>());
    when(userRepository.findUser(USERNAME)).thenReturn(null);

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "user");
    validator.validate(form, errors);

    // check asserts
    Assert.assertEquals("No errors", 0, errors.getErrorCount());
  }

  @Test
  public void testInvalidRoles() throws Exception {
    // set up data
    UserBackingForm form = new UserBackingForm();
    form.setUsername(USERNAME);
    form.setPassword("password");
    form.setConfirmPassword("password");
    form.setRoles(Arrays.asList(new RoleBackingForm[]{}));

    // set up mocks
    when(formFieldRepository.getRequiredFormFields("user")).thenReturn(Arrays.asList(new String[]{}));
    when(formFieldRepository.getFieldMaxLengths("user")).thenReturn(new HashMap<String, Integer>());
    when(userRepository.findUser(USERNAME)).thenReturn(null);

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "user");
    validator.validate(form, errors);

    // check asserts
    Assert.assertEquals("Errors", 1, errors.getErrorCount());
    Assert.assertNotNull("Error: no roles defined", errors.getFieldError("roles"));
  }

  @Test
  public void testPasswordDontMatch() throws Exception {
    // set up data
    UserBackingForm form = new UserBackingForm();
    form.setUsername(USERNAME);
    form.setPassword("password1");
    form.setConfirmPassword("password2");
    form.setRoles(Arrays.asList(aRoleBackingForm().build()));

    // set up mocks
    when(formFieldRepository.getRequiredFormFields("user")).thenReturn(Arrays.asList(new String[]{}));
    when(formFieldRepository.getFieldMaxLengths("user")).thenReturn(new HashMap<String, Integer>());
    when(userRepository.findUser(USERNAME)).thenReturn(null);

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "user");
    validator.validate(form, errors);

    // check asserts
    Assert.assertEquals("Errors", 1, errors.getErrorCount());
    Assert.assertNotNull("Error: passwords don't match", errors.getFieldError("password"));
  }

  @Test
  public void testPasswordEmpty1() throws Exception {
    // set up data
    UserBackingForm form = new UserBackingForm();
    form.setUsername(USERNAME);
    form.setPassword("");
    form.setConfirmPassword("password2");
    form.setRoles(Arrays.asList(aRoleBackingForm().build()));

    // set up mocks
    when(formFieldRepository.getRequiredFormFields("user")).thenReturn(Arrays.asList(new String[]{}));
    when(formFieldRepository.getFieldMaxLengths("user")).thenReturn(new HashMap<String, Integer>());
    when(userRepository.findUser(USERNAME)).thenReturn(null);

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "user");
    validator.validate(form, errors);

    // check asserts
    Assert.assertEquals("Errors", 1, errors.getErrorCount());
    Assert.assertNotNull("Error: passwords cannot be empty", errors.getFieldError("password"));
  }

  @Test
  public void testPasswordEmpty2() throws Exception {
    // set up data
    UserBackingForm form = new UserBackingForm();
    form.setUsername(USERNAME);
    form.setPassword("password1");
    form.setConfirmPassword("");
    form.setRoles(Arrays.asList(aRoleBackingForm().build()));

    // set up mocks
    when(formFieldRepository.getRequiredFormFields("user")).thenReturn(Arrays.asList(new String[]{}));
    when(formFieldRepository.getFieldMaxLengths("user")).thenReturn(new HashMap<String, Integer>());
    when(userRepository.findUser(USERNAME)).thenReturn(null);

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "user");
    validator.validate(form, errors);

    // check asserts
    Assert.assertEquals("Errors", 1, errors.getErrorCount());
    Assert.assertNotNull("Error: passwords cannot be empty", errors.getFieldError("password"));
  }

  @Test
  public void testUpdateNoPasswordChange() throws Exception {
    // set up data
    UserBackingForm form = new UserBackingForm();
    form.setId(USER_ID);
    form.setUsername(USERNAME);
    form.setRoles(Arrays.asList(aRoleBackingForm().build()));

    // set up mocks
    when(formFieldRepository.getRequiredFormFields("user")).thenReturn(Arrays.asList(new String[]{}));
    when(formFieldRepository.getFieldMaxLengths("user")).thenReturn(new HashMap<String, Integer>());

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "user");
    validator.validate(form, errors);

    // check asserts
    Assert.assertEquals("No errors", 0, errors.getErrorCount());
  }

  @Test
  public void testUpdateWithOwnPasswordChange() throws Exception {
    // set up data
    UserBackingForm form = new UserBackingForm();
    form.setId(USER_ID);
    form.setUsername(USERNAME);
    form.setCurrentPassword("password");
    form.setPassword("newPassword");
    form.setConfirmPassword("newPassword");
    form.setRoles(Arrays.asList(aRoleBackingForm().build()));
    form.setModifyPassword(true);

    User anotherAdminUser = UserBuilder.aUser()
        .withId(UUID.randomUUID())
        .withUsername("aadmin")
        .withPasswordReset()
        .build();

    // set up mocks
    when(formFieldRepository.getRequiredFormFields("user")).thenReturn(Arrays.asList(new String[]{}));
    when(formFieldRepository.getFieldMaxLengths("user")).thenReturn(new HashMap<String, Integer>());
    when(userRepository.findUserById(USER_ID)).thenReturn(anotherAdminUser);

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "user");
    validator.validate(form, errors);

    // check asserts
    Assert.assertEquals("No errors", 0, errors.getErrorCount());
  }

  @Test
  public void testUpdateWithOwnPasswordChangeInvalidCurrentPassword() throws Exception {
    // set up data
    UserBackingForm form = new UserBackingForm();
    form.setId(USER_ID);
    form.setUsername(USERNAME);
    form.setCurrentPassword("oldpassword");
    form.setPassword("newPassword");
    form.setConfirmPassword("newPassword");
    form.setRoles(Arrays.asList(aRoleBackingForm().build()));
    form.setModifyPassword(true);

    User anotherAdminUser = UserBuilder.aUser()
        .withId(UUID.randomUUID())
        .withUsername("aadmin")
        .withPasswordReset()
        .build();

    // set up mocks
    when(formFieldRepository.getRequiredFormFields("user")).thenReturn(Arrays.asList(new String[]{}));
    when(formFieldRepository.getFieldMaxLengths("user")).thenReturn(new HashMap<String, Integer>());
    when(userRepository.findUserById(USER_ID)).thenReturn(anotherAdminUser);

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "user");
    validator.validate(form, errors);

    // check asserts
    Assert.assertEquals("Errors", 1, errors.getErrorCount());
    Assert.assertNotNull("Error: current password doesn't match", errors.getFieldError("password"));
  }

  @Test
  public void testUpdateWithOwnPasswordChangeNullCurrentPassword() throws Exception {
    // set up data
    UserBackingForm form = new UserBackingForm();
    form.setId(USER_ID);
    form.setUsername(USERNAME);
    form.setCurrentPassword(null);
    form.setPassword("newPassword");
    form.setConfirmPassword("newPassword");
    form.setRoles(Arrays.asList(aRoleBackingForm().build()));
    form.setModifyPassword(true);

    User anotherAdminUser = UserBuilder.aUser()
        .withId(UUID.randomUUID())
        .withUsername("aadmin")
        .withPasswordReset()
        .build();

    // set up mocks
    when(formFieldRepository.getRequiredFormFields("user")).thenReturn(Arrays.asList(new String[]{}));
    when(formFieldRepository.getFieldMaxLengths("user")).thenReturn(new HashMap<String, Integer>());
    when(userRepository.findUserById(USER_ID)).thenReturn(anotherAdminUser);

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "user");
    validator.validate(form, errors);

    // check asserts
    Assert.assertEquals("Errors", 1, errors.getErrorCount());
    Assert.assertNotNull("Error: current password doesn't match", errors.getFieldError("password"));
  }

  @Test
  public void testUpdateWithOwnPasswordChangeNewPasswordMismatch() throws Exception {
    // set up data
    UserBackingForm form = new UserBackingForm();
    form.setId(USER_ID);
    form.setUsername(USERNAME);
    form.setCurrentPassword("password");
    form.setPassword("newPassword1");
    form.setConfirmPassword("newPassword2");
    form.setRoles(Arrays.asList(aRoleBackingForm().build()));
    form.setModifyPassword(true);

    User anotherAdminUser = UserBuilder.aUser()
        .withId(UUID.randomUUID())
        .withUsername("aadmin")
        .withPasswordReset()
        .build();

    // set up mocks
    when(formFieldRepository.getRequiredFormFields("user")).thenReturn(Arrays.asList(new String[]{}));
    when(formFieldRepository.getFieldMaxLengths("user")).thenReturn(new HashMap<String, Integer>());
    when(userRepository.findUserById(USER_ID)).thenReturn(anotherAdminUser);

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "user");
    validator.validate(form, errors);

    // check asserts
    Assert.assertEquals("Errors", 1, errors.getErrorCount());
    Assert.assertNotNull("Error: new password doesn't match", errors.getFieldError("password"));
  }

  @Test
  public void testUpdateWithManagePasswordChange() throws Exception {
    // set up data
    UserBackingForm form = new UserBackingForm();
    form.setId(UUID.randomUUID());
    form.setUsername("datacapturer");
    form.setPassword("newPassword");
    form.setConfirmPassword("newPassword");
    form.setRoles(Arrays.asList(aRoleBackingForm().build()));
    form.setModifyPassword(true);

    // set up security
    setSecurityUser(loggedInUser, PermissionConstants.MANAGE_ROLES, PermissionConstants.MANAGE_USERS);

    // set up mocks
    when(formFieldRepository.getRequiredFormFields("user")).thenReturn(Arrays.asList(new String[]{}));
    when(formFieldRepository.getFieldMaxLengths("user")).thenReturn(new HashMap<String, Integer>());
    when(userRepository.findUserById(USER_ID)).thenReturn(loggedInUser);

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "user");
    validator.validate(form, errors);

    // check asserts
    Assert.assertEquals("No errors", 0, errors.getErrorCount());
  }
}
