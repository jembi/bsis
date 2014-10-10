/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import static controller.DonorController.getUrl;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.donordeferral.DonorDeferral;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import repository.DonorRepository;
import utils.CustomDateFormatter;
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

    @Autowired
    private UtilController utilController;

    @RequestMapping(value = "form/{donorId}", method = RequestMethod.GET)
    @PreAuthorize("hasRole('" + PermissionConstants.ADD_DEFERRAL + "')")
    public 
    Map<String, Object> deferDonorFormGenerator(HttpServletRequest request,
            @PathVariable String donorId) {

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("donorId", donorId);
        map.put("deferralReasons", donorRepository.getDeferralReasons());
        return map;
    }

    @RequestMapping(value = "{id}/edit/form", method = RequestMethod.GET)
    @PreAuthorize("hasRole('" + PermissionConstants.EDIT_DEFERRAL + "')")
    public 
    Map<String, Object> editDeferDonorFormGenerator(HttpServletRequest request,
            @PathVariable String id) {

        Map<String, Object> map = new HashMap<String, Object>();
        DonorDeferral donorDeferral = donorRepository.getDonorDeferralsId(Long.parseLong(id));
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

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    @PreAuthorize("hasRole('" + PermissionConstants.VIEW_DEFERRAL + "')")
    public 
    Map<String, Object> viewDonorDeferrals(HttpServletRequest request, @PathVariable Long id) {

        Map<String, Object> map = new HashMap<String, Object>();
        List<DonorDeferral> donorDeferrals = null;
        List<DonorDeferralViewModel> donorDeferralViewModels;
        donorDeferrals = donorRepository.getDonorDeferrals(id);
        donorDeferralViewModels = getDonorDeferralViewModels(donorDeferrals);
        map.put("isDonorCurrentlyDeferred", donorRepository.isCurrentlyDeferred(donorDeferrals));
        map.put("allDonorDeferrals", donorDeferralViewModels);
        return map;
    }

    @RequestMapping(method = RequestMethod.POST)
    @PreAuthorize("hasRole('" + PermissionConstants.ADD_DEFERRAL + "')")
    public 
    HttpStatus deferDonor(
            HttpServletResponse response,
            @RequestParam(value = "donorId" , required = true) String donorId,
            @RequestParam(value = "deferUntil", required = true) String deferUntil,
            @RequestParam(value = "deferralReasonId", required = true) String deferralReasonId,
            @RequestParam(value = "deferralReasonText", required = false) String deferralReasonText) {

        try {
            donorRepository.deferDonor(donorId, deferUntil, deferralReasonId, deferralReasonText);
        } catch (ParseException ex) {
          ex.printStackTrace();
          response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
        return HttpStatus.CREATED;
    }

    @RequestMapping(value = "{id}", method = RequestMethod.PUT)
    @PreAuthorize("hasRole('" + PermissionConstants.EDIT_DEFERRAL + "')")
    public HttpStatus updateDeferDonor(HttpServletResponse response,
            @PathVariable String id,
            @RequestParam("donorId") String donorId,
            @RequestParam("deferUntil") String deferUntil,
            @RequestParam("deferralReasonId") String deferralReasonId,
            @RequestParam("deferralReasonText") String deferralReasonText) {

        try {
            donorRepository.updatedeferDonor(id, donorId, deferUntil, deferralReasonId, deferralReasonText);
        } catch (Exception ex) {
            ex.printStackTrace();
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }

        return HttpStatus.OK;
    }

    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    @PreAuthorize("hasRole('" + PermissionConstants.VOID_DEFERRAL + "')")
    public 
    HttpStatus cancelDeferDonor(HttpServletResponse response,
            @PathVariable Long  id) {

        try {
            donorRepository.cancelDeferDonor(id);
        } catch (Exception ex) {
            ex.printStackTrace();
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }

        return HttpStatus.OK;
    }

    private List<DonorDeferralViewModel> getDonorDeferralViewModels(List<DonorDeferral> donorDeferrals) {
        List<DonorDeferralViewModel> donorDeferralViewModels = new ArrayList<DonorDeferralViewModel>();
        for (DonorDeferral donorDeferral : donorDeferrals) {
            donorDeferralViewModels.add(new DonorDeferralViewModel(donorDeferral));
        }
        return donorDeferralViewModels;
    }

}
