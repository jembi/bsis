/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package backingform.validator;

import backingform.DonorCodeBackingForm;
import java.util.Arrays;
import model.donor.Donor;
import model.donorcodes.DonorCode;
import model.donorcodes.DonorDonorCode;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import repository.DonorRepository;

/**
 *
 * @author srikanth
 */
public class DonorCodeBackingFormValidator implements Validator{
    
  private Validator validator;
  
  private DonorRepository donorRepository;

  public DonorCodeBackingFormValidator(Validator validator , DonorRepository donorRepository) {
    super();
    this.validator = validator;
    this.donorRepository = donorRepository;
  }

    @Override
    public boolean supports(Class<?> clazz) {
       return Arrays.asList(DonorCodeBackingForm.class,
        DonorDonorCode.class, Donor.class).contains(clazz);
    }

    @Override
    public void validate(Object object, Errors errors) {
        
        System.out.println("in validation.....");
        if (object == null || validator == null)
             return;
        DonorCodeBackingForm form = (DonorCodeBackingForm) object;
        DonorCode donorCode = donorRepository.findDonorCodeById(form.getDonorCodeId());
       if( donorRepository.findDonorById(form.getDonorId()).getDonorCodes().contains(donorCode))
           errors.rejectValue("","" ,"Donor Code is already assigned to donor");
        
    }
    
}
