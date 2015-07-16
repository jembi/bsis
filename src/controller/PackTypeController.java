package controller;

import org.springframework.web.bind.annotation.RestController;
import org.apache.log4j.Logger;
import repository.BloodBagTypeRepository;
import backingform.PackTypeBackingForm;
import backingform.validator.PackTypeBackingFormValidator;
import viewmodel.PackTypeViewModel;
import model.bloodbagtype.BloodBagType;
import utils.PermissionConstants;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("packtypes")
public class PackTypeController {
	
	private static final Logger LOGGER = Logger.getLogger(PackTypeController.class);
	
	  @Autowired
	  BloodBagTypeRepository bloodBagTypeRepository;
	  
	  @Autowired
	  private UtilController utilController;
	  
	  public PackTypeController() {
	  }
	  
	  @InitBinder
	  protected void initBinder(WebDataBinder binder) {
	    binder.setValidator(new PackTypeBackingFormValidator(binder.getValidator(), utilController, bloodBagTypeRepository));
	  }

	  	@RequestMapping(method=RequestMethod.GET)
	  	@PreAuthorize("hasRole('" + PermissionConstants.MANAGE_BLOOD_BAG_TYPES + "')")
	  	public  Map<String, Object> getPackTypes() {
	  		Map<String, Object> map = new HashMap<String, Object>();
	  		addAllBloodBagTypesToModel(map);
	  		return map;
	  	}
	  
		@RequestMapping(value = "{id}", method = RequestMethod.GET)
		@PreAuthorize("hasRole('" + PermissionConstants.MANAGE_BLOOD_BAG_TYPES + "')")
		public ResponseEntity<BloodBagType> getPackTypeById(@PathVariable Integer id){
		    Map<String, Object> map = new HashMap<String, Object>();
		    BloodBagType packType = bloodBagTypeRepository.getBloodBagTypeById(id);
		    map.put("packtype", new PackTypeViewModel(packType));
		    return new ResponseEntity(map, HttpStatus.OK);
		}
		
		@RequestMapping(method = RequestMethod.POST)
		@PreAuthorize("hasRole('" + PermissionConstants.MANAGE_BLOOD_BAG_TYPES + "')")
		    public ResponseEntity savePackType(@Valid @RequestBody PackTypeBackingForm formData){
				BloodBagType packType = formData.getPackType();
		        packType = bloodBagTypeRepository.saveBloodBagType(packType);
		        return new ResponseEntity(new PackTypeViewModel(packType), HttpStatus.CREATED);
		    }
		  
		@RequestMapping(value = "{id}", method = RequestMethod.PUT)
		@PreAuthorize("hasRole('" + PermissionConstants.MANAGE_BLOOD_BAG_TYPES + "')")
		public ResponseEntity updateBloodBagType(@Valid @RequestBody PackTypeBackingForm formData , @PathVariable Integer id){
		    Map<String, Object> map = new HashMap<String, Object>();
		    BloodBagType packType = formData.getPackType();
		    packType.setId(id);
		    packType = bloodBagTypeRepository.updateBloodBagType(packType);
		    map.put("packtype", new PackTypeViewModel(packType));
		    return new ResponseEntity(map, HttpStatus.OK);
		}
	  
	  private void addAllBloodBagTypesToModel(Map<String, Object> m) {
	    m.put("allBloodBagTypes", getPackTypeViewModels(bloodBagTypeRepository.getAllBloodBagTypes()));
	  }
	  
	  private List<PackTypeViewModel> getPackTypeViewModels(List<BloodBagType> packTypes){
	      
	      List<PackTypeViewModel> viewModels = new ArrayList<PackTypeViewModel>();
	      for(BloodBagType packtType : packTypes){
	          viewModels.add(new PackTypeViewModel(packtType));
	      }
	      return viewModels;
	  }

}
