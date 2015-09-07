package controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import repository.AdverseEventTypeRepository;
import viewmodel.AdverseEventTypeViewModel;

@RestController
@RequestMapping("adverseevents")
public class AdverseEventController {
    
    @Autowired
    private AdverseEventTypeRepository adverseEventTypeRepository;
    
    @RequestMapping(value = "/types", method = RequestMethod.GET)
    public List<AdverseEventTypeViewModel> findAdverseEventTypes() {
        return adverseEventTypeRepository.findAdverseEventTypeViewModels();
    }

}
