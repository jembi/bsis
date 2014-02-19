package backingform.validator;

import java.util.Arrays;

import model.user.User;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import repository.UserRepository;
import viewmodel.UserViewModel;
import backingform.UserBackingForm;
import controller.UtilController;

public class UserBackingFormValidator implements Validator {

  private Validator validator;
	private UtilController utilController;
	private UserRepository userRepository;
	boolean editingUser=false;

  public UserBackingFormValidator(Validator validator, UtilController utilController, UserRepository userRepository) {
    super();
    this.validator = validator;
    this.utilController = utilController;
    this.userRepository=userRepository;
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
    if(editingUser){
       if(form.isModifyPassword())
          comparePassword(form,errors);
    }
    else  
    	  comparePassword(form,errors);
     
     checkRoles(form,errors);
  }
  
 
  private void comparePassword(UserBackingForm form, Errors errors) {
	if( (form.getPassword()==null||form.getPassword().isEmpty() ) && (form.getUserConfirPassword()==null||form.getUserConfirPassword().isEmpty()) )
  	    errors.rejectValue("user.password","user.incorrect" ,"Password cannot be blank");
  	    else
  	if(!form.getPassword().equals(form.getUserConfirPassword())){
  		errors.rejectValue("user.password","user.incorrect" ,"Passwords do not match");
  	}
  
  }
  
 
  
  
  private void checkRoles(UserBackingForm form, Errors errors) {
	  if(form.getUserRoles()==null)
		  errors.rejectValue("userRoles","user.selectRole" ,"Must select at least one Role");
	  return;
  }
  
  private void checkUserName(UserBackingForm form, Errors errors) {
  	boolean flag=false;
  	
  	String userName=form.getUsername();
  	
  	User existingUser=null;
  	if(!userName.equals(""))
  	{
  		existingUser=userRepository.findUser(userName);
  		 if(existingUser != null && existingUser.getId().equals(form.getId()))
  			 editingUser=true;
  		
  		 if(existingUser != null && !existingUser.getId().equals(form.getId())){
  			 errors.rejectValue("user.username", "userName.nonunique",
           "Username  already exists.");
  		 return;
  		 
       }
  	}
  	if(userName.length() <= 2 ||  userName.length() >= 50){
  		flag=true;
  	}
  	
  	if(!userName.matches("^[a-zA-Z0-9_.-]*$")){
  		flag=true;
  	}
  	
  	if(flag && userName.length() > 0){
  		errors.rejectValue("user.username","user.incorrect" ,"Username invalid. Use only alphanumeric characters, underscore (_), hyphen (-), and period (.).");
  	}
  
  }
}
