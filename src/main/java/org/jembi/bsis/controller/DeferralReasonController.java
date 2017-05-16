package org.jembi.bsis.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.validation.Valid;

import org.jembi.bsis.backingform.DeferralReasonBackingForm;
import org.jembi.bsis.backingform.validator.DeferralReasonBackingFormValidator;
import org.jembi.bsis.factory.DeferralReasonFactory;
import org.jembi.bsis.model.donordeferral.DeferralReason;
import org.jembi.bsis.repository.DeferralReasonRepository;
import org.jembi.bsis.utils.PermissionConstants;
import org.jembi.bsis.viewmodel.DeferralReasonViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("deferralreasons")
public class DeferralReasonController {

  @Autowired
  DeferralReasonRepository deferralReasonRepository;
  
  @Autowired
  DeferralReasonFactory deferralReasonFactory;

  @Autowired
  DeferralReasonBackingFormValidator deferralReasonBackingFormValidator;

  public DeferralReasonController() {
  }

  @InitBinder
  protected void initBinder(WebDataBinder binder) {
    binder.setValidator(deferralReasonBackingFormValidator);
  }

  @RequestMapping(method = RequestMethod.GET)
  @PreAuthorize("hasRole('" + PermissionConstants.MANAGE_DEFERRAL_REASONS + "')")
  public Map<String, Object> getDeferralReasons() {
    Map<String, Object> map = new HashMap<String, Object>();
    map.put("allDeferralReasons", deferralReasonFactory.createViewModels(deferralReasonRepository.getAllDeferralReasonsIncludDeleted()));
    return map;
  }

  @RequestMapping(value = "{id}", method = RequestMethod.GET)
  @PreAuthorize("hasRole('" + PermissionConstants.MANAGE_DEFERRAL_REASONS + "')")
  public ResponseEntity<Map<String, Object>> getDeferralReasonById(@PathVariable UUID id) {
    Map<String, Object> map = new HashMap<String, Object>();
    DeferralReason deferralReason = deferralReasonRepository.getDeferralReasonById(id);
    map.put("reason", deferralReasonFactory.createViewModel(deferralReason));
    return new ResponseEntity<>(map, HttpStatus.OK);
  }

  @RequestMapping(method = RequestMethod.POST)
  @PreAuthorize("hasRole('" + PermissionConstants.MANAGE_DEFERRAL_REASONS + "')")
  public ResponseEntity<DeferralReasonViewModel> saveDeferralReason(@Valid @RequestBody DeferralReasonBackingForm formData) {
    DeferralReason deferralReason = formData.getDeferralReason();
    deferralReason = deferralReasonRepository.saveDeferralReason(deferralReason);
    return new ResponseEntity<>(deferralReasonFactory.createViewModel(deferralReason), HttpStatus.CREATED);
  }

  @RequestMapping(value = "{id}", method = RequestMethod.PUT)
  @PreAuthorize("hasRole('" + PermissionConstants.MANAGE_DEFERRAL_REASONS + "')")
  public ResponseEntity<Map<String, Object>> updateDeferralReason(@Valid @RequestBody DeferralReasonBackingForm formData, @PathVariable UUID id) {
    Map<String, Object> map = new HashMap<String, Object>();
    DeferralReason deferralReason = formData.getDeferralReason();
    deferralReason.setId(id);
    deferralReason = deferralReasonRepository.updateDeferralReason(deferralReason);
    map.put("reason", deferralReasonFactory.createViewModel(deferralReason));
    return new ResponseEntity<>(map, HttpStatus.OK);
  }
}

