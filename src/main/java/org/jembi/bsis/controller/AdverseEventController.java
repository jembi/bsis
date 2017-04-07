package org.jembi.bsis.controller;

import java.util.List;
import java.util.UUID;

import javax.validation.Valid;

import org.jembi.bsis.backingform.AdverseEventTypeBackingForm;
import org.jembi.bsis.backingform.validator.AdverseEventTypeBackingFormValidator;
import org.jembi.bsis.factory.AdverseEventTypeViewModelFactory;
import org.jembi.bsis.model.adverseevent.AdverseEventType;
import org.jembi.bsis.repository.AdverseEventTypeRepository;
import org.jembi.bsis.service.AdverseEventTypeCRUDService;
import org.jembi.bsis.utils.PermissionConstants;
import org.jembi.bsis.viewmodel.AdverseEventTypeViewModel;
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
@RequestMapping("adverseevents")
public class AdverseEventController {

  @Autowired
  private AdverseEventTypeRepository adverseEventTypeRepository;
  @Autowired
  private AdverseEventTypeCRUDService adverseEventTypeCRUDService;
  @Autowired
  private AdverseEventTypeViewModelFactory adverseEventTypeViewModelFactory;
  @Autowired
  private AdverseEventTypeBackingFormValidator adverseEventTypeBackingFormValidator;

  @InitBinder
  public void initBinder(WebDataBinder binder) {
    binder.addValidators(adverseEventTypeBackingFormValidator);
  }

  @RequestMapping(value = "/types", method = RequestMethod.POST)
  @PreAuthorize("hasRole('" + PermissionConstants.MANAGE_ADVERSE_EVENTS + "')")
  public AdverseEventTypeViewModel createAdverseEventType(@Valid @RequestBody AdverseEventTypeBackingForm backingForm) {
    AdverseEventType adverseEventType = adverseEventTypeCRUDService.createAdverseEventType(backingForm);
    return adverseEventTypeViewModelFactory.createAdverseEventTypeViewModel(adverseEventType);
  }

  @RequestMapping(value = "/types", method = RequestMethod.GET)
  @PreAuthorize("hasRole('" + PermissionConstants.MANAGE_ADVERSE_EVENTS + "')")
  public List<AdverseEventTypeViewModel> findAdverseEventTypes() {
    return adverseEventTypeRepository.findAdverseEventTypeViewModels();
  }

  @RequestMapping(value = "/types/{id}", method = RequestMethod.GET)
  @PreAuthorize("hasRole('" + PermissionConstants.MANAGE_ADVERSE_EVENTS + "')")
  public AdverseEventTypeViewModel findAdverseEventTypeById(@PathVariable("id") UUID id) {
    AdverseEventType adverseEventType = adverseEventTypeRepository.findById(id);
    return adverseEventTypeViewModelFactory.createAdverseEventTypeViewModel(adverseEventType);
  }

  @RequestMapping(value = "/types/{id}", method = RequestMethod.PUT)
  @PreAuthorize("hasRole('" + PermissionConstants.MANAGE_ADVERSE_EVENTS + "')")
  public AdverseEventTypeViewModel updateAdverseEventType(
      @PathVariable("id") UUID id,
      @Valid @RequestBody AdverseEventTypeBackingForm backingForm) {
    AdverseEventType adverseEventType = adverseEventTypeCRUDService.updateAdverseEventType(id, backingForm);
    return adverseEventTypeViewModelFactory.createAdverseEventTypeViewModel(adverseEventType);
  }

}
