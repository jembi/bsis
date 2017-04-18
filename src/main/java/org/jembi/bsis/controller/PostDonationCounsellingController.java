package org.jembi.bsis.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.validation.Valid;

import org.jembi.bsis.backingform.PostDonationCounsellingBackingForm;
import org.jembi.bsis.backingform.validator.PostDonationCounsellingBackingFormValidator;
import org.jembi.bsis.controllerservice.PostDonationCounsellingControllerService;
import org.jembi.bsis.model.counselling.CounsellingStatus;
import org.jembi.bsis.utils.PermissionConstants;
import org.jembi.bsis.viewmodel.PostDonationCounsellingViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/postdonationcounsellings")
public class PostDonationCounsellingController {
  
  @Autowired
  private PostDonationCounsellingControllerService postDonationCounsellingControllerService;
  @Autowired
  private PostDonationCounsellingBackingFormValidator postDonationCounsellingBackingFormValidator;
  
  @InitBinder
  public void initBinder(WebDataBinder binder) {
    binder.addValidators(postDonationCounsellingBackingFormValidator);
  }

  @RequestMapping(value = "{id}", method = RequestMethod.PUT)
  @PreAuthorize("hasRole('" + PermissionConstants.EDIT_POST_DONATION_COUNSELLING + "')")
  public PostDonationCounsellingViewModel updatePostDonationCounselling(
      @Valid @RequestBody PostDonationCounsellingBackingForm backingForm,
      @PathVariable UUID id) {

    return postDonationCounsellingControllerService.update(backingForm);
  }

  @RequestMapping(value = "/form", method = RequestMethod.GET)
  @PreAuthorize("hasAnyRole('" + PermissionConstants.ADD_POST_DONATION_COUNSELLING + "', '" +
      PermissionConstants.EDIT_POST_DONATION_COUNSELLING + "')")
  public Map<String, Object> getPostDonationCounsellingForm() {
    Map<String, Object> map = new HashMap<>();
    map.put("counsellingStatuses", CounsellingStatus.values());
    map.put("referralSites", postDonationCounsellingControllerService.getReferralSites());

    return map;
  }

  @RequestMapping(value = "/searchForm", method = RequestMethod.GET)
  @PreAuthorize("hasAnyRole('" + PermissionConstants.VIEW_POST_DONATION_COUNSELLING + "')")
  public Map<String, Object> getPostDonationCounsellingSearchForm() {
    Map<String, Object> map = new HashMap<>();
    map.put("counsellingStatuses", CounsellingStatus.values());
    map.put("venues", postDonationCounsellingControllerService.getVenues());
    return map;
  }

  @RequestMapping(method = RequestMethod.GET)
  @PreAuthorize("hasRole('" + PermissionConstants.VIEW_POST_DONATION_COUNSELLING_DONORS + "')")
  public Map<String, Object> searchPostDonationCounselling(
      @RequestParam(value = "flaggedForCounselling", required = true) boolean flaggedForCounselling,
      @RequestParam(value = "startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date startDate,
      @RequestParam(value = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date endDate,
      @RequestParam(value = "venue", required = false) Set<UUID> venues,
      @RequestParam(value = "counsellingStatus", required = false) CounsellingStatus counsellingStatus,
      @RequestParam(value = "referred", required = false) Boolean referred,
      @RequestParam(value = "notReferred", required = false) Boolean notReferred) {
    Map<String, Object> map = new HashMap<>();
    map.put("counsellings", postDonationCounsellingControllerService.getCounsellingSummaries(startDate, endDate,
        venues, counsellingStatus, referred, notReferred, flaggedForCounselling));
    return map;
  }
}
