package org.jembi.bsis.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.jembi.bsis.backingform.PostDonationCounsellingBackingForm;
import org.jembi.bsis.backingform.validator.PostDonationCounsellingBackingFormValidator;
import org.jembi.bsis.controllerservice.LocationControllerService;
import org.jembi.bsis.controllerservice.PostDonationCounsellingControllerService;
import org.jembi.bsis.model.counselling.CounsellingStatus;
import org.jembi.bsis.utils.PermissionConstants;
import org.jembi.bsis.viewmodel.CounsellingStatusViewModel;
import org.jembi.bsis.viewmodel.PostDonationCounsellingViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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
      @PathVariable Long id) {

    return postDonationCounsellingControllerService.update(backingForm);
  }

  @RequestMapping(value = "/form", method = RequestMethod.GET)
  @PreAuthorize("hasAnyRole('" + PermissionConstants.ADD_POST_DONATION_COUNSELLING + "', '" +
      PermissionConstants.EDIT_POST_DONATION_COUNSELLING + "')")
  public Map<String, Object> getPostDonationCounsellingForm() {
    Map<String, Object> map = new HashMap<>();

    List<CounsellingStatusViewModel> counsellingStatuses = new ArrayList<>();
    for (CounsellingStatus counsellingStatus : CounsellingStatus.values()) {
      counsellingStatuses.add(new CounsellingStatusViewModel(counsellingStatus));
    }
    map.put("counsellingStatuses", counsellingStatuses);
    map.put("referralSites", postDonationCounsellingControllerService.getReferralSites());

    return map;
  }

  @RequestMapping(value = "/searchForm", method = RequestMethod.GET)
  @PreAuthorize("hasAnyRole('" + PermissionConstants.VIEW_POST_DONATION_COUNSELLING + "')")
  public Map<String, Object> getPostDonationCounsellingSearchForm() {
    Map<String, Object> map = new HashMap<>();
    map.put("venues", postDonationCounsellingControllerService.getVenues());
    return map;
  }

}
