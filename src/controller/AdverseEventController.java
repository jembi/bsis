package controller;

import java.util.List;

import javax.validation.Valid;

import model.adverseevent.AdverseEventType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import factory.AdverseEventTypeViewModelFactory;
import backingform.AdverseEventTypeBackingForm;
import repository.AdverseEventTypeRepository;
import service.AdverseEventTypeCRUDService;
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
    
    @RequestMapping(value = "/types", method = RequestMethod.POST)
    // TODO: Permissions
    public AdverseEventTypeViewModel createAdverseEventType(@Valid @RequestBody AdverseEventTypeBackingForm backingForm) {
        AdverseEventType adverseEventType = adverseEventTypeCRUDService.createAdverseEventType(backingForm);
        return adverseEventTypeViewModelFactory.createAdverseEventTypeViewModel(adverseEventType);
    }
    
    @RequestMapping(value = "/types", method = RequestMethod.GET)
    // TODO: Permissions
    public List<AdverseEventTypeViewModel> findAdverseEventTypes() {
        return adverseEventTypeRepository.findAdverseEventTypeViewModels();
    }

}
