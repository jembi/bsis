package org.jembi.bsis.controller;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.jembi.bsis.backingform.PostDonationCounsellingBackingForm;
import org.jembi.bsis.backingform.validator.PostDonationCounsellingBackingFormValidator;
import org.jembi.bsis.controllerservice.PostDonationCounsellingControllerService;
import org.jembi.bsis.factory.DonationSummaryViewModelFactory;
import org.jembi.bsis.model.donation.Donation;
import org.jembi.bsis.repository.PostDonationCounsellingRepository;
import org.jembi.bsis.utils.PermissionConstants;
import org.jembi.bsis.utils.PermissionUtils;
import org.jembi.bsis.viewmodel.DonationSummaryViewModel;
import org.jembi.bsis.viewmodel.PostDonationCounsellingViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.AccessDeniedException;
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
  @Autowired
  private PostDonationCounsellingRepository postDonationCounsellingRepository;
  @Autowired
  private DonationSummaryViewModelFactory donationSummaryViewModelFactory;
  
  @InitBinder
  public void initBinder(WebDataBinder binder) {
    binder.addValidators(postDonationCounsellingBackingFormValidator);
  }

  @RequestMapping(value = "{id}", method = RequestMethod.PUT)
  @PreAuthorize("hasRole('" + PermissionConstants.EDIT_POST_DONATION_COUNSELLING + "')")
  public PostDonationCounsellingViewModel updatePostDonationCounselling(
      @Valid @RequestBody PostDonationCounsellingBackingForm backingForm,
      @PathVariable Long id) {

    return postDonationCounsellingControllerService.update(backingForm);
  }

  @RequestMapping(value = "/form", method = RequestMethod.GET)
  @PreAuthorize("hasAnyRole('" + PermissionConstants.ADD_POST_DONATION_COUNSELLING + "', '" +
      PermissionConstants.EDIT_POST_DONATION_COUNSELLING + "')")
  public Map<String, Object> getPostDonationCounsellingForm() {
    Map<String, Object> map = new HashMap<>();
    map.put("counsellingStatuses", postDonationCounsellingControllerService.getCounsellingStatuses());
    map.put("referralSites", postDonationCounsellingControllerService.getReferralSites());

    return map;
  }

  @RequestMapping(value = "/searchForm", method = RequestMethod.GET)
  @PreAuthorize("hasAnyRole('" + PermissionConstants.VIEW_POST_DONATION_COUNSELLING + "')")
  public Map<String, Object> getPostDonationCounsellingSearchForm() {
    Map<String, Object> map = new HashMap<>();
    map.put("counsellingStatuses", postDonationCounsellingControllerService.getCounsellingStatuses());
    map.put("venues", postDonationCounsellingControllerService.getVenues());
    return map;
  }

  @RequestMapping(method = RequestMethod.GET)
  @PreAuthorize("hasRole('" + PermissionConstants.VIEW_DONOR + "')")
  public List<DonationSummaryViewModel> getDonationSummaries(
      @RequestParam(value = "flaggedForCounselling", required = true) boolean flaggedForCounselling,
      @RequestParam(value = "startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date startDate,
      @RequestParam(value = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date endDate,
      @RequestParam(value = "venue", required = false) List<Long> venues) {

    if (flaggedForCounselling) {

      if (!PermissionUtils.loggedOnUserHasPermission(PermissionConstants.VIEW_POST_DONATION_COUNSELLING_DONORS)) {
        throw new AccessDeniedException("You do not have permission to view post donation counselling donors.");
      }

      List<Donation> donations = postDonationCounsellingRepository.findDonationsFlaggedForCounselling(
          startDate, endDate, venues == null ? null : new HashSet<>(venues));
      return donationSummaryViewModelFactory.createFullDonationSummaryViewModels(donations);
    }

    // Just return an empty list for now. This could return the full list of donations if needed.
    return Collections.emptyList();
  }
}
