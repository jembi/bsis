package backingform.validator;

import java.util.Arrays;

import model.user.User;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import viewmodel.UserViewModel;
import backingform.UserBackingForm;
import controller.UtilController;

public class UserBackingFormValidator implements Validator {

  private Validator validator;
	private UtilController utilController;

  public UserBackingFormValidator(Validator validator, UtilController utilController) {
    super();
    this.validator = validator;
    this.utilController = utilController;
  }

  @SuppressWarnings("unchecked")
  @Override
  public boolean supports(Class<?> clazz) {
    return Arrays.asList(UserBackingForm.class, User.class, UserViewModel.class).contains(clazz);
  }

  @Override
  public void validate(Object obj, Errors errors) {
    if (obj == null || validator == null)
      return;
    ValidationUtils.invokeValidator(validator, obj, errors);
    UserBackingForm form = (UserBackingForm) obj;
     utilController.commonFieldChecks(form, "user", errors);
    checkUserName(form,errors);
    comparePassword(form,errors);
    compareUserPassword(form,errors);
    checkRoles(form,errors);
  }
  
  private void comparePassword(UserBackingForm form, Errors errors) {
  	if(form.getPassword() == null || form.getPassword().isEmpty() || !form.getPassword().equals(form.getUserConfirPassword())){
  		errors.rejectValue("user.password","user.incorrect" ,"Passwords do not match");
  	}
  	return;
  }
  
  private void compareUserPassword(UserBackingForm form, Errors errors) {
  	String pwd= utilController.getUserPassword(form.getId());
  	if(pwd!=null){
	  	if(form.getCurrentPassword() == null || form.getCurrentPassword() == "" || !form.getCurrentPassword().equals(pwd)){
	  		errors.rejectValue("user.isAdmin","user.incorrect" ,"Current Password does not match");
	  	}
  }
  	return;
  }
  
  private void checkRoles(UserBackingForm form, Errors errors) {
  	if(form.getRoleAdmin() ==null && form.getRoleDonorLab() ==null && form.getRoleTestLab() ==null && form.getRoleUser()==null){
  		errors.rejectValue("user.isStaff","user.incorrect" ,"Must select at least one role");
  		form.setUserRole("");
  	}
  	return;
  }
  
  private void checkUserName(UserBackingForm form, Errors errors) {
  	boolean flag=false;
  	
  	String userName=form.getUsername();
  	if(userName.length() <= 2 ||  userName.length() >= 50){
  		flag=true;
  	}
  	
  	if(!userName.matches("^[a-zA-Z0-9_.-]*$")){
  		flag=true;
  	}
  	
  	if(flag && userName.length() > 0){
  		errors.rejectValue("user.username","user.incorrect" ,"Username invalid. Use only alphanumeric characters, underscore (_), hyphen (-), and period (.).");
  	}
  	return;
  }
}
