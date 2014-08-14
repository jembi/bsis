package controller;

import backingform.DonorCodeBackingForm;
import backingform.validator.DonorCodeBackingFormValidator;
import com.wordnik.swagger.annotations.Api;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import model.donor.Donor;
import model.donorcodes.DonorCodeGroup;
import model.donorcodes.DonorDonorCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import repository.DonorRepository;
import utils.PermissionConstants;

@Controller
@RequestMapping
@Api(value = "Donor Codes")
public class DonorCodeController {
	
	  @Autowired
	  private DonorRepository donorRepository;
	  
	  @Autowired
	  private UtilController utilController;
	  
         @InitBinder
         protected void initBinder(WebDataBinder binder) {
                binder.setValidator(new DonorCodeBackingFormValidator(binder.getValidator(), donorRepository));
          }

	 
         @RequestMapping(value = "/updateDonorCodesFormGenerator", method = RequestMethod.GET)
	  @PreAuthorize("hasRole('"+PermissionConstants.EDIT_DONOR_CODE+"')")
	  public @ResponseBody Map<String, Object>  updateDonorCodesForm(HttpServletRequest request  ,@RequestParam(value="donorId") Long donorId){
                  
                  Map<String, Object> map = new HashMap<String, Object>();
                  Donor donor = donorRepository.findDonorById(donorId);
		  map.put("donor", donor);
		  map.put("donorFields", utilController.getFormFieldsForForm("donor"));	  
		  return map;
		  
	  }
           @RequestMapping(value = "/donorCOde", method = RequestMethod.GET)
	  @PreAuthorize("hasRole('"+PermissionConstants.ADD_DONOR_CODE+"')")
              public @ResponseBody List<DonorCodeGroup> addDonorCodeFormGenerator(){
              return  donorRepository.getAllDonorCodeGroups();
          }
	  
	
	  @RequestMapping(value = "/donorCOde" ,method = RequestMethod.POST)
	  @PreAuthorize("hasRole('"+PermissionConstants.ADD_DONOR_CODE+"')")
	  public @ResponseBody Map<String, Object> addDonorCode(HttpServletRequest request,HttpServletResponse response, 
			@ModelAttribute("addDonorCodeForm")  @Valid DonorCodeBackingForm form,
                         BindingResult result){
              Map<String, Object> map = new HashMap<String, Object>();
              
             
                  if (result.hasErrors()) {
                      map.put("addDonorCodeForm",form);          
                      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
	             return map;
		
                     }
	         DonorDonorCode  donorDonorCode =  new DonorDonorCode();
		 donorDonorCode.setDonorId(donorRepository.findDonorById(form.getDonorId()));
		 donorDonorCode.setDonorCodeId(donorRepository.findDonorCodeById(form.getDonorCodeId()));
		 donorRepository.saveDonorDonorCode(donorDonorCode);
		 map.put("donorDonorCodes", donorRepository.findDonorDonorCodesOfDonorByDonorId(form.getDonorId()));
	         return map;
		  
	  }
	  
	  @RequestMapping(value = "/donorCOde",method = RequestMethod.GET,params = {"donorId"})
	  @PreAuthorize("hasRole('"+PermissionConstants.VIEW_DONOR_CODE+"')")
	  public @ResponseBody List<DonorDonorCode> donorCodesTable(HttpServletRequest request,Long donorId){
		
		  return   donorRepository.findDonorDonorCodesOfDonorByDonorId(donorId);
		  
		  
	  }
	  
	  @RequestMapping(value = "/donorCOde" , method = RequestMethod.DELETE)
	  @PreAuthorize("hasRole('"+PermissionConstants.VOID_DONOR_CODE+"')")
	  public @ResponseBody List<DonorDonorCode> deleteDomorCode(@RequestParam(value="id") Long id){
		  Donor donor = donorRepository.deleteDonorCode(id);
                  return donorRepository.findDonorDonorCodesOfDonorByDonorId(donor.getId());
	  }
	  
	  
}
