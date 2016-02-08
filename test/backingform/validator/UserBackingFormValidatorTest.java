package backingform.validator;

import static org.mockito.Mockito.when;

import helpers.builders.UserBuilder;

import java.util.Arrays;
import java.util.HashMap;

import model.user.Role;
import model.user.User;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.validation.Errors;
import org.springframework.validation.MapBindingResult;

import repository.FormFieldRepository;
import repository.UserRepository;
import suites.UnitTestSuite;
import utils.PermissionConstants;
import backingform.UserBackingForm;

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
    form.setRoles(Arrays.asList(new Role[]{new Role()}));

    // set up mocks
    when(formFieldRepository.getRequiredFormFields("user")).thenReturn(Arrays.asList(new String[]{}));
    when(formFieldRepository.getFieldMaxLengths("user")).thenReturn(new HashMap<String, Integer>());
    when(userRepository.findUser(USERNAME)).thenReturn(loggedInUser);

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "user");
    validator.validate(form, errors);

    // check asserts
    Assert.assertEquals("Errors exist", 1, errors.getErrorCount());
    Assert.assertNotNull("Error: duplicate username", errors.getFieldError("user.username"));
  }

  @Test
  public void testInvalidUserName1() throws Exception {
    // set up data
    UserBackingForm form = new UserBackingForm();
    form.setUsername("a");
    form.setPassword("password");
    form.setConfirmPassword("password");
    form.setRoles(Arrays.asList(new Role[]{new Role()}));

    // set up mocks
    when(formFieldRepository.getRequiredFormFields("user")).thenReturn(Arrays.asList(new String[]{}));
    when(formFieldRepository.getFieldMaxLengths("user")).thenReturn(new HashMap<String, Integer>());
    when(userRepository.findUser(USERNAME)).thenReturn(null);

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "user");
    validator.validate(form, errors);

    // check asserts
    Assert.assertEquals("Errors exist", 1, errors.getErrorCount());
    Assert.assertNotNull("Error: username too short", errors.getFieldError("user.username"));
  }

  @Test
  public void testInvalidUserName2() throws Exception {
    // set up data
    UserBackingForm form = new UserBackingForm();
    form.setUsername("adminadminadminadminadminadminadminadminadminadminadminadminadminadminadminadminadminadminadminadminadminadminadminadminadminadminadminadminadminadminadminadmin");
    form.setPassword("password");
    form.setConfirmPassword("password");
    form.setRoles(Arrays.asList(new Role[]{new Role()}));

    // set up mocks
    when(formFieldRepository.getRequiredFormFields("user")).thenReturn(Arrays.asList(new String[]{}));
    when(formFieldRepository.getFieldMaxLengths("user")).thenReturn(new HashMap<String, Integer>());
    when(userRepository.findUser(USERNAME)).thenReturn(null);

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "user");
    validator.validate(form, errors);

    // check asserts
    Assert.assertEquals("Errors exist", 1, errors.getErrorCount());
    Assert.assertNotNull("Error: username too long", errors.getFieldError("user.username"));
  }

  @Test
  public void testInvalidUserName3() throws Exception {
    // set up data
    UserBackingForm form = new UserBackingForm();
    form.setUsername("adm%n");
    form.setPassword("password");
    form.setConfirmPassword("password");
    form.setRoles(Arrays.asList(new Role[]{new Role()}));

    // set up mocks
    when(formFieldRepository.getRequiredFormFields("user")).thenReturn(Arrays.asList(new String[]{}));
    when(formFieldRepository.getFieldMaxLengths("user")).thenReturn(new HashMap<String, Integer>());
    when(userRepository.findUser(USERNAME)).thenReturn(null);

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "user");
    validator.validate(form, errors);

    // check asserts
    Assert.assertEquals("Errors exist", 1, errors.getErrorCount());
    Assert.assertNotNull("Error: username contains %", errors.getFieldError("user.username"));
  }

  @Test
  public void testValidUserName() throws Exception {
    // set up data
    UserBackingForm form = new UserBackingForm();
    form.setUsername(USERNAME);
    form.setPassword("password");
    form.setConfirmPassword("password");
    form.setRoles(Arrays.asList(new Role[]{new Role()}));

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
    form.setRoles(Arrays.asList(new Role[]{}));

    // set up mocks
    when(formFieldRepository.getRequiredFormFields("user")).thenReturn(Arrays.asList(new String[]{}));
    when(formFieldRepository.getFieldMaxLengths("user")).thenReturn(new HashMap<String, Integer>());
    when(userRepository.findUser(USERNAME)).thenReturn(null);

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "user");
    validator.validate(form, errors);

    // check asserts
    Assert.assertEquals("Errors", 1, errors.getErrorCount());
    Assert.assertNotNull("Error: no roles defined", errors.getFieldError("user.roles"));
  }

  @Test
  public void testPasswordDontMatch() throws Exception {
    // set up data
    UserBackingForm form = new UserBackingForm();
    form.setUsername(USERNAME);
    form.setPassword("password1");
    form.setConfirmPassword("password2");
    form.setRoles(Arrays.asList(new Role[]{new Role()}));

    // set up mocks
    when(formFieldRepository.getRequiredFormFields("user")).thenReturn(Arrays.asList(new String[]{}));
    when(formFieldRepository.getFieldMaxLengths("user")).thenReturn(new HashMap<String, Integer>());
    when(userRepository.findUser(USERNAME)).thenReturn(null);

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "user");
    validator.validate(form, errors);

    // check asserts
    Assert.assertEquals("Errors", 1, errors.getErrorCount());
    Assert.assertNotNull("Error: passwords don't match", errors.getFieldError("user.password"));
  }

  @Test
  public void testPasswordEmpty1() throws Exception {
    // set up data
    UserBackingForm form = new UserBackingForm();
    form.setUsername(USERNAME);
    form.setPassword("");
    form.setConfirmPassword("password2");
    form.setRoles(Arrays.asList(new Role[]{new Role()}));

    // set up mocks
    when(formFieldRepository.getRequiredFormFields("user")).thenReturn(Arrays.asList(new String[]{}));
    when(formFieldRepository.getFieldMaxLengths("user")).thenReturn(new HashMap<String, Integer>());
    when(userRepository.findUser(USERNAME)).thenReturn(null);

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "user");
    validator.validate(form, errors);

    // check asserts
    Assert.assertEquals("Errors", 1, errors.getErrorCount());
    Assert.assertNotNull("Error: passwords cannot be empty", errors.getFieldError("user.password"));
  }

  @Test
  public void testPasswordEmpty2() throws Exception {
    // set up data
    UserBackingForm form = new UserBackingForm();
    form.setUsername(USERNAME);
    form.setPassword("password1");
    form.setConfirmPassword("");
    form.setRoles(Arrays.asList(new Role[]{new Role()}));

    // set up mocks
    when(formFieldRepository.getRequiredFormFields("user")).thenReturn(Arrays.asList(new String[]{}));
    when(formFieldRepository.getFieldMaxLengths("user")).thenReturn(new HashMap<String, Integer>());
    when(userRepository.findUser(USERNAME)).thenReturn(null);

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "user");
    validator.validate(form, errors);

    // check asserts
    Assert.assertEquals("Errors", 1, errors.getErrorCount());
    Assert.assertNotNull("Error: passwords cannot be empty", errors.getFieldError("user.password"));
  }

  @Test
  public void testUpdateNoPasswordChange() throws Exception {
    // set up data
    UserBackingForm form = new UserBackingForm();
    form.setId(USER_ID);
    form.setUsername(USERNAME);
    form.setRoles(Arrays.asList(new Role[]{new Role()}));

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
    form.setRoles(Arrays.asList(new Role[]{new Role()}));
    form.setModifyPassword(true);

    User anotherAdminUser = UserBuilder.aUser()
        .withId(2l)
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
    form.setRoles(Arrays.asList(new Role[]{new Role()}));
    form.setModifyPassword(true);

    User anotherAdminUser = UserBuilder.aUser()
        .withId(2l)
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
    Assert.assertNotNull("Error: current password doesn't match", errors.getFieldError("user.password"));
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
    form.setRoles(Arrays.asList(new Role[]{new Role()}));
    form.setModifyPassword(true);

    User anotherAdminUser = UserBuilder.aUser()
        .withId(2l)
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
    Assert.assertNotNull("Error: current password doesn't match", errors.getFieldError("user.password"));
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
    form.setRoles(Arrays.asList(new Role[]{new Role()}));
    form.setModifyPassword(true);

    User anotherAdminUser = UserBuilder.aUser()
        .withId(2l)
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
    Assert.assertNotNull("Error: new password doesn't match", errors.getFieldError("user.password"));
  }

  @Test
  public void testUpdateWithManagePasswordChange() throws Exception {
    // set up data
    UserBackingForm form = new UserBackingForm();
    form.setId(2l);
    form.setUsername("datacapturer");
    form.setPassword("newPassword");
    form.setConfirmPassword("newPassword");
    form.setRoles(Arrays.asList(new Role[]{new Role()}));
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
