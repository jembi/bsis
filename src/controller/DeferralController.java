/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import model.donordeferral.DonorDeferral;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
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
    @PreAuthorize("hasRole('" + PermissionConstants.ADD_DEFERRAL + "')")
    public 
    Map<String, Object> deferDonorFormGenerator() {

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("deferralReasons", donorRepository.getDeferralReasons());
        return map;
    }

    /**
     * 
     * Not Used anywhere - #209 
     *
    @RequestMapping(value = "edit/form", method = RequestMethod.GET)
    @PreAuthorize("hasRole('" + PermissionConstants.EDIT_DEFERRAL + "')")
    public 
    Map<String, Object> editDeferDonorFormGenerator(
            @PathVariable String donorId) {

        Map<String, Object> map = new HashMap<String, Object>();
        DonorDeferral donorDeferral = donorRepository.getDonorDeferralsId(Long.parseLong(donorId));
        if (donorDeferral != null) {
            map.put("deferralUntilDate", CustomDateFormatter.getDateString(donorDeferral.getDeferredUntil()));
            map.put("deferReasonText", donorDeferral.getDeferralReasonText());
            map.put("deferReasonId", donorDeferral.getDeferralReason().getId());
            map.put("donorId", donorDeferral.getDeferredDonor().getId());
            map.put("donorDeferralId", donorDeferral.getId());
        }
        map.put("deferralReasons", donorRepository.getDeferralReasons());
        return map;
    }
   */
 
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
    ResponseEntity deferDonor(
            @RequestParam("donorId") String donorId,
            @RequestParam(value = "deferUntil", required = true) String deferUntil,
            @RequestParam(value = "deferralReasonId", required = true) String deferralReasonId,
            @RequestParam(value = "deferralReasonText", required = false) String deferralReasonText) {

        try {
            donorRepository.deferDonor(donorId, deferUntil, deferralReasonId, deferralReasonText);
        } catch (ParseException ex) {
          ex.printStackTrace();
          return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
       return new ResponseEntity(HttpStatus.CREATED);
    }

    @RequestMapping(value="{id}", method = RequestMethod.PUT)
    @PreAuthorize("hasRole('" + PermissionConstants.EDIT_DEFERRAL + "')")
    public ResponseEntity updateDeferDonor(
            @PathVariable String id,
            @RequestParam(value = "donorId") String donorId,
            @RequestParam("deferUntil") String deferUntil,
            @RequestParam("deferralReasonId") String deferralReasonId,
            @RequestParam("deferralReasonText") String deferralReasonText) {

        try {
            donorRepository.updatedeferDonor(id, donorId, deferUntil, deferralReasonId, deferralReasonText);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }

         return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    @PreAuthorize("hasRole('" + PermissionConstants.VOID_DEFERRAL + "')")
    public 
    ResponseEntity cancelDeferDonor(HttpServletResponse response,
            @PathVariable Long  id) {

            donorRepository.cancelDeferDonor(id);
            return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    private List<DonorDeferralViewModel> getDonorDeferralViewModels(List<DonorDeferral> donorDeferrals) {
        List<DonorDeferralViewModel> donorDeferralViewModels = new ArrayList<DonorDeferralViewModel>();
        for (DonorDeferral donorDeferral : donorDeferrals) {
            donorDeferralViewModels.add(new DonorDeferralViewModel(donorDeferral));
        }
        return donorDeferralViewModels;
    }

}
