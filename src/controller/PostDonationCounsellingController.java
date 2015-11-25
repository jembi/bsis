package controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import model.counselling.CounsellingStatus;
import model.counselling.PostDonationCounselling;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import service.PostDonationCounsellingCRUDService;
import utils.PermissionConstants;
import viewmodel.CounsellingStatusViewModel;
import viewmodel.PostDonationCounsellingViewModel;
import backingform.PostDonationCounsellingBackingForm;

@RestController
@RequestMapping("/postdonationcounsellings")
public class PostDonationCounsellingController {
    
    @Autowired
    private PostDonationCounsellingCRUDService postDonationCounsellingCRUDService;
    
    @RequestMapping(value = "{id}", method = RequestMethod.PUT)
    @PreAuthorize("hasRole('" + PermissionConstants.EDIT_POST_DONATION_COUNSELLING + "')")
    public PostDonationCounsellingViewModel updatePostDonationCounselling(
            @Valid @RequestBody PostDonationCounsellingBackingForm backingForm,
            @PathVariable Long id) {

        if (backingForm.getFlaggedForCounselling()) {
            return new PostDonationCounsellingViewModel(postDonationCounsellingCRUDService.flagForCounselling(backingForm.getId()));
        }

        PostDonationCounselling postDonationCounselling = postDonationCounsellingCRUDService.updatePostDonationCounselling(
                backingForm.getId(), backingForm.getCounsellingStatus(), backingForm.getCounsellingDate(),
                backingForm.getNotes());
        
        return new PostDonationCounsellingViewModel(postDonationCounselling);
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
