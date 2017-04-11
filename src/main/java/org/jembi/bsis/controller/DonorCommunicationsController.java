package org.jembi.bsis.controller;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.jembi.bsis.factory.DonorViewModelFactory;
import org.jembi.bsis.factory.LocationFactory;
import org.jembi.bsis.model.donor.Donor;
import org.jembi.bsis.model.location.Location;
import org.jembi.bsis.model.util.BloodGroup;
import org.jembi.bsis.repository.DonorCommunicationsRepository;
import org.jembi.bsis.repository.LocationRepository;
import org.jembi.bsis.service.FormFieldAccessorService;
import org.jembi.bsis.utils.PermissionConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("donorcommunications")
public class DonorCommunicationsController {

  /**
   * The Constant LOGGER.
   */
  private static final Logger LOGGER = Logger.getLogger(DonorCommunicationsController.class);

  @Autowired
  private DonorCommunicationsRepository donorCommunicationsRepository;

  @Autowired
  private FormFieldAccessorService formFieldAccessorService;

  @Autowired
  private LocationRepository locationRepository;

  @Autowired
  private LocationFactory locationFactory;
  
  @Autowired
  private DonorViewModelFactory donorViewModelFactory;

  public DonorCommunicationsController() {
  }

  @InitBinder
  protected void initBinder(WebDataBinder binder) {
    binder.registerCustomEditor(BloodGroup.class, null, null);
  }

  @RequestMapping(value = "/form", method = RequestMethod.GET)
  @PreAuthorize("hasRole('" + PermissionConstants.VIEW_DONOR_INFORMATION + "')")
  public
  @ResponseBody
  Map<String, Object> donorCommunicationsFormGenerator(HttpServletRequest request) {
    Map<String, Object> map = new HashMap<String, Object>();
    map.put("donorFields", formFieldAccessorService.getFormFieldsForForm("donor"));
    map.put("venues", locationFactory.createViewModels(locationRepository.getVenues()));
    map.put("bloodGroups", BloodGroup.getBloodgroups());
    return map;
  }

  @RequestMapping(value = "/search", method = RequestMethod.GET)
  @PreAuthorize("hasRole('" + PermissionConstants.VIEW_DONOR_INFORMATION + "')")
  public
  @ResponseBody
  Map<String, Object> findDonorCommunicationsPagination(
      @RequestParam(value = "bloodGroups", required = false) List<String> bloodGroups,
      @RequestParam(value = "venues", required = true) List<String> venues,
      @RequestParam(value = "clinicDate", required = false) String clinicDate,
      @RequestParam(value = "lastDonationFromDate", required = false) String lastDonationFromDate,
      @RequestParam(value = "lastDonationToDate", required = false) String lastDonationToDate,
      @RequestParam(value = "anyBloodGroup", required = false) boolean anyBloodGroup,
      @RequestParam(value = "noBloodGroup", required = false) boolean noBloodGroup) throws ParseException {

    LOGGER.debug("Start DonorCommunicationsController:findDonorCommunicationsPagination");

    Map<String, Object> pagingParams = new HashMap<String, Object>();
    pagingParams.put("sortColumn", "id");
    pagingParams.put("sortDirection", "asc");

    List<Donor> results = new ArrayList<Donor>();
    results = donorCommunicationsRepository.findDonors(setLocations(venues), clinicDate, lastDonationFromDate,
        lastDonationToDate, setBloodGroups(bloodGroups), anyBloodGroup, noBloodGroup, pagingParams, clinicDate);

    Map<String, Object> map = new HashMap<String, Object>();
    map.put("donors", donorViewModelFactory.createDonorViewModels(results));

    return map;
  }

  private List<Location> setLocations(List<String> locations) {

    List<Location> venues = new ArrayList<Location>();

    for (String venueId : locations) {
      Location l = new Location();
      l.setId(UUID.fromString(venueId));
      venues.add(l);
    }

    return venues;
  }

  private List<BloodGroup> setBloodGroups(List<String> bloodGroups) {
    if (bloodGroups == null) {
      return Collections.emptyList();
    }

    List<BloodGroup> bloodGroupsList = new ArrayList<BloodGroup>();

    for (String bloodGroup : bloodGroups) {
      BloodGroup bg = new BloodGroup(bloodGroup);
      bloodGroupsList.add(bg);
    }

    return bloodGroupsList;
  }

}
