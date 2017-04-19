package org.jembi.bsis.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.validation.Valid;

import org.jembi.bsis.backingform.DiscardReasonBackingForm;
import org.jembi.bsis.backingform.validator.DiscardReasonBackingFormValidator;
import org.jembi.bsis.factory.ComponentStatusChangeReasonFactory;
import org.jembi.bsis.model.componentmovement.ComponentStatusChangeReason;
import org.jembi.bsis.repository.DiscardReasonRepository;
import org.jembi.bsis.utils.PermissionConstants;
import org.jembi.bsis.viewmodel.DiscardReasonViewModel;
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
@RequestMapping("discardreasons")
public class DiscardReasonController {

  @Autowired
  DiscardReasonRepository discardReasonRepository;

  @Autowired
  DiscardReasonBackingFormValidator discardReasonBackingFormValidator;
  
  @Autowired
  private ComponentStatusChangeReasonFactory componentStatusChangeReasonFactory;

  public DiscardReasonController() {
  }

  @InitBinder
  protected void initBinder(WebDataBinder binder) {
    binder.setValidator(discardReasonBackingFormValidator);
  }

  @RequestMapping(method = RequestMethod.GET)
  @PreAuthorize("hasRole('" + PermissionConstants.MANAGE_DISCARD_REASONS + "')")
  public Map<String, Object> getDiscardReasons() {
    Map<String, Object> map = new HashMap<String, Object>();
    List<DiscardReasonViewModel> discardReasons = componentStatusChangeReasonFactory
        .createDiscardReasonViewModels(discardReasonRepository.getAllDiscardReasons(true));
    map.put("allDiscardReasons", discardReasons);
    return map;
  }

  @RequestMapping(value = "{id}", method = RequestMethod.GET)
  @PreAuthorize("hasRole('" + PermissionConstants.MANAGE_DISCARD_REASONS + "')")
  public ResponseEntity<Map<String, Object>> getDiscardReasonById(@PathVariable UUID id) {
    Map<String, Object> map = new HashMap<String, Object>();
    ComponentStatusChangeReason discardReason = discardReasonRepository.getDiscardReasonById(id);
    map.put("reason", componentStatusChangeReasonFactory.createDiscardReasonViewModel(discardReason));
    return new ResponseEntity<>(map, HttpStatus.OK);
  }

  @RequestMapping(method = RequestMethod.POST)
  @PreAuthorize("hasRole('" + PermissionConstants.MANAGE_DISCARD_REASONS + "')")
  public ResponseEntity<DiscardReasonViewModel> saveDiscardReason(@Valid @RequestBody DiscardReasonBackingForm formData) {
    ComponentStatusChangeReason discardReason = componentStatusChangeReasonFactory.createDiscardReasonEntity(formData);
    discardReason = discardReasonRepository.saveDiscardReason(discardReason);
    DiscardReasonViewModel viewModel = componentStatusChangeReasonFactory.createDiscardReasonViewModel(discardReason);
    return new ResponseEntity<>(viewModel, HttpStatus.CREATED);
  }

  @RequestMapping(value = "{id}", method = RequestMethod.PUT)
  @PreAuthorize("hasRole('" + PermissionConstants.MANAGE_DISCARD_REASONS + "')")
  public ResponseEntity<Map<String, Object>> updateDiscardReason(@Valid @RequestBody DiscardReasonBackingForm formData, @PathVariable UUID id) {
    Map<String, Object> map = new HashMap<String, Object>();
    ComponentStatusChangeReason discardReason = componentStatusChangeReasonFactory.createDiscardReasonEntity(formData);
    discardReason.setId(id);
    discardReason = discardReasonRepository.updateDiscardReason(discardReason);
    map.put("reason", componentStatusChangeReasonFactory.createDiscardReasonViewModel(discardReason));
    return new ResponseEntity<>(map, HttpStatus.OK);
  }
}