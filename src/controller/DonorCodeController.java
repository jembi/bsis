package controller;

import backingform.DonorCodeBackingForm;
import backingform.validator.DonorCodeBackingFormValidator;
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


public class DonorCodeController {
	
	  @Autowired
	  private DonorRepository donorRepository;
	  
	  @Autowired
	  private UtilController utilController;
	  
         @InitBinder
         protected void initBinder(WebDataBinder binder) {
                binder.setValidator(new DonorCodeBackingFormValidator(binder.getValidator(), donorRepository));
          }

	 @Deprecated 
         @RequestMapping(value = "/updateDonorCodesFormGenerator", method = RequestMethod.GET)
	  @PreAuthorize("hasRole('"+PermissionConstants.EDIT_DONOR_CODE+"')")
	  public ModelAndView updateDonorCodesForm(HttpServletRequest request  ,@RequestParam(value="donorId") Long donorId){
		  ModelAndView modelAndView = new ModelAndView("donors/donorCodes/updateDonorCodes");  
		  Donor donor = donorRepository.findDonorById(donorId);
		  modelAndView.addObject("donor", donor);
		  modelAndView.addObject("donorFields", utilController.getFormFieldsForForm("donor"));	  
		  return modelAndView;
		  
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
