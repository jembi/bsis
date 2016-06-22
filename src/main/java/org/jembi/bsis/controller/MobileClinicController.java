package org.jembi.bsis.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.jembi.bsis.dto.MobileClinicDonorDTO;
import org.jembi.bsis.factory.MobileClinicDonorViewModelFactory;
import org.jembi.bsis.model.location.LocationType;
import org.jembi.bsis.repository.DonorRepository;
import org.jembi.bsis.repository.LocationRepository;
import org.jembi.bsis.utils.PermissionConstants;
import org.jembi.bsis.viewmodel.MobileClinicLookUpDonorViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("mobileclinic")
public class MobileClinicController {

  /**
   * The Constant LOGGER.
   */
  @SuppressWarnings("unused")
  private static final Logger LOGGER = Logger.getLogger(MobileClinicController.class);

  @Autowired
  private DonorRepository donorRepository;

  @Autowired
  private LocationRepository locationRepository;

  @Autowired
  private MobileClinicDonorViewModelFactory mobileClinicDonorViewModelFactory;

  public MobileClinicController() {
  }

  @InitBinder
  protected void initBinder(WebDataBinder binder) {
  }

  @RequestMapping(value = "/form", method = RequestMethod.GET)
  @PreAuthorize("hasRole('" + PermissionConstants.VIEW_DONOR_INFORMATION + "')")
  public
  @ResponseBody
  Map<String, Object> mobileClinicLookUpFormGenerator() {

    Map<String, Object> map = new HashMap<String, Object>();
    map.put("venues", locationRepository.getLocationsByType(LocationType.VENUE));
    return map;
  }


  @RequestMapping(value = "/lookup", method = RequestMethod.GET)
  @PreAuthorize("hasRole('" + PermissionConstants.VIEW_DONOR_INFORMATION + "')")
  public
  @ResponseBody
  ResponseEntity<Map<String, Object>> mobileClinicLookUp(
      @RequestParam(value = "venueId", required = true) Long venueId,
      @RequestParam(value = "clinicDate", required = true)
      @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date clinicDate) {

    Map<String, Object> map = new HashMap<String, Object>();

    List<MobileClinicDonorDTO> mobileClinicDonorDTOs = donorRepository.findMobileClinicDonorsByVenue(venueId);
    List<MobileClinicLookUpDonorViewModel> donors = mobileClinicDonorViewModelFactory.createMobileClinicDonorViewModels(mobileClinicDonorDTOs, clinicDate);

    map.put("donors", donors);

    return new ResponseEntity<Map<String, Object>>(map, HttpStatus.OK);
  }
}
