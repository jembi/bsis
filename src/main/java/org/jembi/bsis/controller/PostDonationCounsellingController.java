package org.jembi.bsis.controller;

import java.util.*;

import javax.validation.Valid;

import org.jembi.bsis.backingform.PostDonationCounsellingBackingForm;
import org.jembi.bsis.factory.PostDonationCounsellingViewModelFactory;
import org.jembi.bsis.model.counselling.CounsellingStatus;
import org.jembi.bsis.model.counselling.PostDonationCounselling;
import org.jembi.bsis.repository.PostDonationCounsellingRepository;
import org.jembi.bsis.service.PostDonationCounsellingCRUDService;
import org.jembi.bsis.utils.PermissionConstants;
import org.jembi.bsis.viewmodel.CounsellingStatusViewModel;
import org.jembi.bsis.viewmodel.PostDonationCounsellingViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/postdonationcounsellings")
public class PostDonationCounsellingController {

  @Autowired
  private PostDonationCounsellingCRUDService postDonationCounsellingCRUDService;

  @Autowired
  private PostDonationCounsellingRepository postDonationCounsellingRepository;

  @Autowired
  private PostDonationCounsellingViewModelFactory postDonationCounsellingViewModelFactory;

  @RequestMapping(value = "{id}", method = RequestMethod.PUT)
  @PreAuthorize("hasRole('" + PermissionConstants.EDIT_POST_DONATION_COUNSELLING + "')")
  public PostDonationCounsellingViewModel updatePostDonationCounselling(
      @Valid @RequestBody PostDonationCounsellingBackingForm backingForm,
      @PathVariable Long id) {

    if (backingForm.getFlaggedForCounselling()) {
      //This is when you wish to clear the current status and re flag for counselling
      PostDonationCounselling postDonationCounselling = postDonationCounsellingCRUDService
          .flagForCounselling(backingForm.getId());

      return postDonationCounsellingViewModelFactory
          .createPostDonationCounsellingViewModel(postDonationCounselling);
    }

    PostDonationCounselling postDonationCounselling = postDonationCounsellingCRUDService.updatePostDonationCounselling(
        backingForm.getId(), backingForm.getCounsellingStatus(), backingForm.getCounsellingDate(),
        backingForm.getNotes());

    return postDonationCounsellingViewModelFactory.createPostDonationCounsellingViewModel(postDonationCounselling);
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

    return map;
  }

}
