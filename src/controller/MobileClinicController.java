package controller;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import model.location.Location;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import repository.MobileClinicRepository;
import repository.LocationRepository;
import service.DonorConstraintChecker;
import service.FormFieldAccessorService;
import utils.CustomDateFormatter;
import utils.PermissionConstants;
import viewmodel.MobileClinicLookUpDonorViewModel;

@RestController
@RequestMapping("mobileclinic")
public class MobileClinicController {

    /**
     * The Constant LOGGER.
     */
    private static final Logger LOGGER = Logger.getLogger(MobileClinicController.class);
    @Autowired
    private MobileClinicRepository mobileClinicRepository;

    @Autowired
    private FormFieldAccessorService formFieldAccessorService;

    @Autowired
    private LocationRepository locationRepository;
    
    @Autowired
    private DonorConstraintChecker donorConstraintChecker;

    public MobileClinicController() {
    }

    @InitBinder
    protected void initBinder(WebDataBinder binder) {}
    
    @RequestMapping(value = "/form", method = RequestMethod.GET)
    @PreAuthorize("hasRole('" + PermissionConstants.VIEW_DONOR_INFORMATION + "')")
    public @ResponseBody
    Map<String, Object> mobileClinicLookUpFormGenerator(
            HttpServletRequest request) {

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("venues", locationRepository.getAllVenues());
        return map;
    }


    @RequestMapping(value = "/lookup", method = RequestMethod.GET)
    @PreAuthorize("hasRole('" + PermissionConstants.VIEW_DONOR_INFORMATION + "')")
    public @ResponseBody ResponseEntity<Map<String, Object>> mobileClinicLookUp(
            @RequestParam(value="venue",required=true) String venue,
            @RequestParam(value="clinicDate",required=true ) String clinicDate) throws ParseException{

       Map<String, Object> map = new HashMap<String, Object>();
        
        List<MobileClinicLookUpDonorViewModel> donors = new ArrayList<MobileClinicLookUpDonorViewModel>();
        donors = mobileClinicRepository.lookUp(setLocation(venue));
                
        if (donors != null){
    	    for(MobileClinicLookUpDonorViewModel donor : donors){
    	    	donor.setEligibility(donorConstraintChecker.isDonorEligibleToDonateOnDate(donor.getId(), CustomDateFormatter.parse(clinicDate)));
    	    }
        }

        map.put("donors", donors);
        return new ResponseEntity<Map<String, Object>>(map, HttpStatus.OK);        
       
    }
    
    public Location setLocation(String location) {

        Location l = new Location();
        l.setId(Long.parseLong(location));
        return l;
    }

}
