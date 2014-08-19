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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import repository.DonorRepository;
import utils.CustomDateFormatter;
import utils.PermissionConstants;
import viewmodel.DonorDeferralViewModel;

/**
 *
 * @author srikanth
 */
@Controller
public class DeferralController {

    @Autowired
    private DonorRepository donorRepository;

    @Autowired
    private UtilController utilController;

    @RequestMapping(value = "/deferDonorFormGenerator", method = RequestMethod.GET)
    @PreAuthorize("hasRole('" + PermissionConstants.ADD_DEFERRAL + "')")
    public @ResponseBody
    Map<String, Object> deferDonorFormGenerator(HttpServletRequest request,
            @RequestParam("donorId") String donorId) {

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("donorId", donorId);
        map.put("deferralReasons", donorRepository.getDeferralReasons());
        return map;
    }

    @RequestMapping(value = "/editDeferDonorFormGenerator", method = RequestMethod.GET)
    @PreAuthorize("hasRole('" + PermissionConstants.EDIT_DEFERRAL + "')")
    public @ResponseBody
    Map<String, Object> editDeferDonorFormGenerator(HttpServletRequest request,
            @RequestParam("donorDeferralId") String donorDeferralId) {

        Map<String, Object> map = new HashMap<String, Object>();
        DonorDeferral donorDeferral = donorRepository.getDonorDeferralsId(Long.parseLong(donorDeferralId));
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

    @RequestMapping(value = "/viewDonorDeferrals", method = RequestMethod.GET)
    @PreAuthorize("hasRole('" + PermissionConstants.VIEW_DEFERRAL + "')")
    public @ResponseBody
    Map<String, Object> viewDonorDeferrals(HttpServletRequest request, Model model,
            @RequestParam(value = "donorId", required = false) Long donorId) {

        Map<String, Object> map = new HashMap<String, Object>();
        List<DonorDeferral> donorDeferrals = null;
        List<DonorDeferralViewModel> donorDeferralViewModels;
        try {
            donorDeferrals = donorRepository.getDonorDeferrals(donorId);
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

    @RequestMapping(value = "/deferDonor", method = RequestMethod.POST)
    @PreAuthorize("hasRole('" + PermissionConstants.ADD_DEFERRAL + "')")
    public @ResponseBody
    Map<String, Object> deferDonor(HttpServletRequest request,
            HttpServletResponse response,
            @RequestParam("donorId") String donorId,
            @RequestParam("deferUntil") String deferUntil,
            @RequestParam("deferralReasonId") String deferralReasonId,
            @RequestParam("deferralReasonText") String deferralReasonText) {

        Map<String, Object> donorDeferralResult = new HashMap<String, Object>();

        try {
            donorRepository.deferDonor(donorId, deferUntil, deferralReasonId, deferralReasonText);
        } catch (Exception ex) {
            ex.printStackTrace();
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }

        return donorDeferralResult;
    }

    @RequestMapping(value = "/updateDeferDonor", method = RequestMethod.POST)
    @PreAuthorize("hasRole('" + PermissionConstants.EDIT_DEFERRAL + "')")
    public @ResponseBody
    Map<String, Object> updateDeferDonor(HttpServletRequest request,
            HttpServletResponse response,
            @RequestParam("donorDeferralId") String donorDeferralId,
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

    @RequestMapping(value = "/cancelDeferDonor", method = RequestMethod.POST)
    @PreAuthorize("hasRole('" + PermissionConstants.VOID_DEFERRAL + "')")
    public @ResponseBody
    Map<String, Object> cancelDeferDonor(HttpServletRequest request,
            HttpServletResponse response,
            @RequestParam("donorDeferralId") String donorDeferralId) {

        Map<String, Object> donorDeferralResult = new HashMap<String, Object>();

        try {
            donorRepository.cancelDeferDonor(donorDeferralId);
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
