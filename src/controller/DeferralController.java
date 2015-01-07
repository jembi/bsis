/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import backingform.DeferralBackingForm;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import model.donordeferral.DonorDeferral;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import repository.DonorRepository;
import utils.PermissionConstants;
import viewmodel.DonorDeferralViewModel;

/**
 *
 * @author srikanth
 */
@RestController
@RequestMapping("deferrals")
public class DeferralController {

    @Autowired
    private DonorRepository donorRepository;

    @RequestMapping(value = "/form", method = RequestMethod.GET)
    @PreAuthorize("hasRole('" + PermissionConstants.VIEW_DONOR_INFORMATION + "')")
    public 
    Map<String, Object> deferDonorFormGenerator() {

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("deferralReasons", donorRepository.getDeferralReasons());
        return map;
    }

    @RequestMapping(value = "{donorId}", method = RequestMethod.GET)
    @PreAuthorize("hasRole('" + PermissionConstants.VIEW_DEFERRAL + "')")
    public 
    Map<String, Object> viewDonorDeferrals(@PathVariable Long donorId) {

        Map<String, Object> map = new HashMap<String, Object>();
        List<DonorDeferral> donorDeferrals = null;
        List<DonorDeferralViewModel> donorDeferralViewModels;
        donorDeferrals = donorRepository.getDonorDeferrals(donorId);
        donorDeferralViewModels = getDonorDeferralViewModels(donorDeferrals);
        map.put("isDonorCurrentlyDeferred", donorRepository.isCurrentlyDeferred(donorDeferrals));
        map.put("allDonorDeferrals", donorDeferralViewModels);
        return map;
    }

    @RequestMapping(method = RequestMethod.POST)
    @PreAuthorize("hasRole('" + PermissionConstants.ADD_DEFERRAL + "')")
    public 
    ResponseEntity deferDonor(@Valid @RequestBody DeferralBackingForm form) {
    	
    	HttpStatus httpStatus = HttpStatus.CREATED;
        Map<String, Object> map = new HashMap<String, Object>();
    	DonorDeferral savedDeferral = null;

    	DonorDeferral deferral = form.getDonorDeferral();
    	deferral.setIsVoided(false);
    	savedDeferral = donorRepository.deferDonor(deferral);
        map.put("hasErrors", false);

        map.put("deferralId", savedDeferral.getId());
        map.put("deferral", getDonorDeferralViewModel(donorRepository.findDeferralById(savedDeferral.getId())));

        return new ResponseEntity<Map<String, Object>>(map, httpStatus);

    }

    @RequestMapping(value="{id}", method = RequestMethod.PUT)
    @PreAuthorize("hasRole('" + PermissionConstants.EDIT_DEFERRAL + "')")
    public ResponseEntity updateDeferral(@Valid @RequestBody DeferralBackingForm form, @PathVariable Long id) {

		 HttpStatus httpStatus = HttpStatus.OK;
	     Map<String, Object> map = new HashMap<String, Object>();
	     DonorDeferral updatedDeferral = null;
	     
	     DonorDeferral deferral = form.getDonorDeferral();
	     deferral.setIsVoided(false);
	     deferral.setId(id);
	     
	     updatedDeferral = donorRepository.updateDeferral(deferral);
	
	     map.put("deferral", getDonorDeferralViewModel(donorRepository.findDeferralById(updatedDeferral.getId())));
	     
	     return new ResponseEntity<Map<String, Object>>(map, httpStatus);
    }

    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    @PreAuthorize("hasRole('" + PermissionConstants.VOID_DEFERRAL + "')")
    public 
    ResponseEntity cancelDeferDonor(HttpServletResponse response,
            @PathVariable Long  id) {

            donorRepository.cancelDeferDonor(id);
            return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
    
    private DonorDeferralViewModel getDonorDeferralViewModel(DonorDeferral donorDeferral) {
    	DonorDeferralViewModel donorDeferralViewModel = new DonorDeferralViewModel(donorDeferral);
        return donorDeferralViewModel;
    }

    private List<DonorDeferralViewModel> getDonorDeferralViewModels(List<DonorDeferral> donorDeferrals) {
        List<DonorDeferralViewModel> donorDeferralViewModels = new ArrayList<DonorDeferralViewModel>();
        for (DonorDeferral donorDeferral : donorDeferrals) {
            donorDeferralViewModels.add(new DonorDeferralViewModel(donorDeferral));
        }
        return donorDeferralViewModels;
    }

}
