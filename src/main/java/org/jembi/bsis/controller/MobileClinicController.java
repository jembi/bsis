package org.jembi.bsis.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.jembi.bsis.controllerservice.MobileClinicControllerService;
import org.jembi.bsis.utils.PermissionConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("mobileclinic")
public class MobileClinicController {

  @Autowired
  private MobileClinicControllerService mobileClinicControllerService;

  @RequestMapping(value = "/form", method = RequestMethod.GET)
  @PreAuthorize("hasRole('" + PermissionConstants.VIEW_DONOR_INFORMATION + "')")
  public @ResponseBody Map<String, Object> getMobileClinicFormFields() {
    Map<String, Object> map = new HashMap<String, Object>();
    map.put("venues", mobileClinicControllerService.getMobileVenues());
    return map;
  }

  @RequestMapping(value = "/search", method = RequestMethod.GET)
  @PreAuthorize("hasRole('" + PermissionConstants.VIEW_DONOR_INFORMATION + "')")
  public @ResponseBody ResponseEntity<Map<String, Object>> getMobileClinicDonors(
      @RequestParam(value = "venueId", required = true) UUID venueId,
      @RequestParam(value = "clinicDate", required = true) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date clinicDate) {
    Map<String, Object> map = new HashMap<String, Object>();
    map.put("donors", mobileClinicControllerService.getMobileClinicDonorsByVenue(venueId, clinicDate));
    return new ResponseEntity<Map<String, Object>>(map, HttpStatus.OK);
  }
  
  @RequestMapping(value = "/export", method = RequestMethod.GET)
  @PreAuthorize("hasRole('" + PermissionConstants.VIEW_MOBILE_CLINIC_EXPORT + "')")
  public @ResponseBody ResponseEntity<Map<String, Object>> getMobileClinicDonorsByVenues(
      @RequestParam(value = "venueIds", required = false) Set<UUID> venueIds,
      @RequestParam(value = "clinicDate", required = true) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date clinicDate) {
    Map<String, Object> map = new HashMap<String, Object>();
    map.put("donors", mobileClinicControllerService.getMobileClinicDonorsByVenues(venueIds, clinicDate));
    return new ResponseEntity<Map<String, Object>>(map, HttpStatus.OK);
  }

  @RequestMapping(value = "/donoroutcomes/form", method = RequestMethod.GET)
  @PreAuthorize("hasRole('" + PermissionConstants.VIEW_DONOR_INFORMATION + "')")
  public @ResponseBody ResponseEntity<Map<String, Object>> getDonorOutcomesForm() {
    Map<String, Object> map = new HashMap<>();
    map.put("venues", mobileClinicControllerService.getMobileVenues());
    map.put("bloodTestNames", mobileClinicControllerService.getBloodTestNames());
    return new ResponseEntity<>(map, HttpStatus.OK);
  }

  @RequestMapping(value = "/donoroutcomes", method = RequestMethod.GET)
  @PreAuthorize("hasRole('" + PermissionConstants.VIEW_DONOR_INFORMATION + "')")
  public @ResponseBody ResponseEntity<Map<String, Object>> getDonorOutcomes(
      @RequestParam(value = "venueId", required = true) UUID venueId,
      @RequestParam(value = "startDate", required = true) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date startDate,
      @RequestParam(value = "endDate", required = true) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date endDate) {
    Map<String, Object> map = new HashMap<>();
    map.put("donorOutcomes", mobileClinicControllerService.getDonorOutcomes(venueId, startDate, endDate));
    return new ResponseEntity<>(map, HttpStatus.OK);
  }
}
