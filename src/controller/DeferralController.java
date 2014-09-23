/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import static controller.DonorController.getUrl;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.donordeferral.DonorDeferral;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("deferral")
public class DeferralController {

    @Autowired
    private DonorRepository donorRepository;

    @Autowired
    private UtilController utilController;

    @RequestMapping(method = RequestMethod.GET)
    @PreAuthorize("hasRole('" + PermissionConstants.ADD_DEFERRAL + "')")
    public 
    Map<String, Object> deferDonorFormGenerator(HttpServletRequest request,
            @RequestParam("donorId") String donorId) {

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("donorId", donorId);
        map.put("deferralReasons", donorRepository.getDeferralReasons());
        return map;
    }

    @RequestMapping(value = "{id}/edit", method = RequestMethod.GET)
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
        try {
            donorDeferrals = donorRepository.getDonorDeferrals(id);
            donorDeferralViewModels = getDonorDeferralViewModels(donorDeferrals);
        } catch (Exception ex) {
            ex.printStackTrace();
            donorDeferralViewModels = Arrays.asList(new DonorDeferralViewModel[0]);
        }

        map.put("isDonorCurrentlyDeferred", donorRepository.isCurrentlyDeferred(donorDeferrals));
        map.put("allDonorDeferrals", donorDeferralViewModels);
        map.put("refreshUrl", getUrl(request));
        // to ensure custom field names are displayed in the form
        map.put("donorDeferralFields", utilController.getFormFieldsForForm("donorDeferral"));
        return map;
    }

    @RequestMapping(method = RequestMethod.POST)
    @PreAuthorize("hasRole('" + PermissionConstants.ADD_DEFERRAL + "')")
    public 
    Map<String, Object> deferDonor(
            HttpServletResponse response,
            @RequestParam(value = "donorId" , required = true) String donorId,
            @RequestParam(value = "deferUntil", required = true) String deferUntil,
            @RequestParam(value = "deferralReasonId", required = true) String deferralReasonId,
            @RequestParam(value = "deferralReasonText", required = false) String deferralReasonText) {

        Map<String, Object> donorDeferralResult = new HashMap<String, Object>();

        try {
            donorRepository.deferDonor(donorId, deferUntil, deferralReasonId, deferralReasonText);
        } catch (Exception ex) {
            ex.printStackTrace();
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }

        return donorDeferralResult;
    }

    @RequestMapping(method = RequestMethod.PUT)
    @PreAuthorize("hasRole('" + PermissionConstants.EDIT_DEFERRAL + "')")
    public 
    Map<String, Object> updateDeferDonor( HttpServletResponse response,
            @RequestParam(value = "donorDeferralId",  required = true) String donorDeferralId,
            @RequestParam("donorId") String donorId,
            @RequestParam("deferUntil") String deferUntil,
            @RequestParam("deferralReasonId") String deferralReasonId,
            @RequestParam("deferralReasonText") String deferralReasonText) {

        Map<String, Object> donorDeferralResult = new HashMap<String, Object>();

        try {
            donorRepository.updatedeferDonor(donorDeferralId, donorId, deferUntil, deferralReasonId, deferralReasonText);
        } catch (Exception ex) {
            ex.printStackTrace();
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }

        return donorDeferralResult;
    }

    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    @PreAuthorize("hasRole('" + PermissionConstants.VOID_DEFERRAL + "')")
    public 
    Map<String, Object> cancelDeferDonor(HttpServletResponse response,
            @PathVariable Long  id) {

        Map<String, Object> donorDeferralResult = new HashMap<String, Object>();

        try {
            donorRepository.cancelDeferDonor(id);
        } catch (Exception ex) {
            ex.printStackTrace();
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }

        return donorDeferralResult;
    }

    private List<DonorDeferralViewModel> getDonorDeferralViewModels(List<DonorDeferral> donorDeferrals) {
        List<DonorDeferralViewModel> donorDeferralViewModels = new ArrayList<DonorDeferralViewModel>();
        for (DonorDeferral donorDeferral : donorDeferrals) {
            donorDeferralViewModels.add(new DonorDeferralViewModel(donorDeferral));
        }
        return donorDeferralViewModels;
    }

}
