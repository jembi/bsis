package controller;

import java.util.List;

import javax.validation.Valid;

import model.adverseevent.AdverseEventType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import factory.AdverseEventTypeViewModelFactory;
import backingform.AdverseEventTypeBackingForm;
import backingform.validator.AdverseEventTypeBackingFormValidator;
import repository.AdverseEventTypeRepository;
import service.AdverseEventTypeCRUDService;
import utils.PermissionConstants;
import viewmodel.AdverseEventTypeViewModel;

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
    @PreAuthorize("hasRole('" + PermissionConstants.ADD_ADVERSE_EVENT_TYPES + "')")
    public AdverseEventTypeViewModel createAdverseEventType(@Valid @RequestBody AdverseEventTypeBackingForm backingForm) {
        AdverseEventType adverseEventType = adverseEventTypeCRUDService.createAdverseEventType(backingForm);
        return adverseEventTypeViewModelFactory.createAdverseEventTypeViewModel(adverseEventType);
    }
    
    @RequestMapping(value = "/types", method = RequestMethod.GET)
    @PreAuthorize("hasRole('" + PermissionConstants.VIEW_ADVERSE_EVENT_TYPES + "')")
    public List<AdverseEventTypeViewModel> findAdverseEventTypes() {
        return adverseEventTypeRepository.findAdverseEventTypeViewModels();
    }
    
    @RequestMapping(value = "/types/{id}", method = RequestMethod.GET)
    @PreAuthorize("hasRole('" + PermissionConstants.VIEW_ADVERSE_EVENT_TYPES + "')")
    public AdverseEventTypeViewModel findAdverseEventTypeById(@PathVariable("id") Long id) {
        AdverseEventType adverseEventType = adverseEventTypeRepository.findById(id);
        return adverseEventTypeViewModelFactory.createAdverseEventTypeViewModel(adverseEventType);
    }

    @RequestMapping(value = "/types/{id}", method = RequestMethod.PUT)
    @PreAuthorize("hasRole('" + PermissionConstants.EDIT_ADVERSE_EVENT_TYPES + "')")
    public AdverseEventTypeViewModel updateAdverseEventType(
            @PathVariable("id") Long id,
            @Valid @RequestBody AdverseEventTypeBackingForm backingForm) {
        AdverseEventType adverseEventType = adverseEventTypeCRUDService.updateAdverseEventType(id, backingForm);
        return adverseEventTypeViewModelFactory.createAdverseEventTypeViewModel(adverseEventType);
    }

}
