package controller;

import backingform.DiscardReasonBackingForm;
import backingform.validator.DiscardReasonBackingFormValidator;
import model.componentmovement.ComponentStatusChangeReason;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import repository.DiscardReasonRepository;
import utils.PermissionConstants;
import viewmodel.DiscardReasonViewModel;

import javax.validation.Valid;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("discardreasons")
public class DiscardReasonController {

    private static final Logger LOGGER = Logger.getLogger(DiscardReasonController.class);

    @Autowired
    DiscardReasonRepository discardReasonRepository;
    
    @Autowired
    DiscardReasonBackingFormValidator discardReasonBackingFormValidator;

    public DiscardReasonController() {
    }

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.setValidator(discardReasonBackingFormValidator);
    }

    @RequestMapping(method=RequestMethod.GET)
    @PreAuthorize("hasRole('" + PermissionConstants.MANAGE_DISCARD_REASONS + "')")
    public  Map<String, Object> getDiscardReasons() {
        Map<String, Object> map = new HashMap<String, Object>();
        addAllDiscardReasonsToModel(map);
        return map;
    }

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    @PreAuthorize("hasRole('" + PermissionConstants.MANAGE_DISCARD_REASONS + "')")
    public ResponseEntity<ComponentStatusChangeReason> getDiscardReasonById(@PathVariable Long id){
        Map<String, Object> map = new HashMap<String, Object>();
        ComponentStatusChangeReason discardReason = discardReasonRepository.getDiscardReasonById(id);
        map.put("reason", new DiscardReasonViewModel(discardReason));
        return new ResponseEntity(map, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST)
    @PreAuthorize("hasRole('" + PermissionConstants.MANAGE_DISCARD_REASONS + "')")
    public ResponseEntity saveDiscardReason(@Valid @RequestBody DiscardReasonBackingForm formData){
        ComponentStatusChangeReason discardReason = formData.getDiscardReason();
        discardReason = discardReasonRepository.saveDiscardReason(discardReason);
        return new ResponseEntity(new DiscardReasonViewModel(discardReason), HttpStatus.CREATED);
    }

    @RequestMapping(value = "{id}", method = RequestMethod.PUT)
    @PreAuthorize("hasRole('" + PermissionConstants.MANAGE_DISCARD_REASONS + "')")
    public ResponseEntity updateDiscardReason(@Valid @RequestBody DiscardReasonBackingForm formData , @PathVariable Long id){
        Map<String, Object> map = new HashMap<String, Object>();
        ComponentStatusChangeReason discardReason = formData.getDiscardReason();
        discardReason.setId(id);
        discardReason = discardReasonRepository.updateDiscardReason(discardReason);
        map.put("reason", new DiscardReasonViewModel(discardReason));
        return new ResponseEntity(map, HttpStatus.OK);
    }

    private void addAllDiscardReasonsToModel(Map<String, Object> m) {
        m.put("allDiscardReasons", getDiscardReasonViewModels(discardReasonRepository.getAllDiscardReasons()));
    }

    private List<DiscardReasonViewModel> getDiscardReasonViewModels(List<ComponentStatusChangeReason> discardReasons){

        List<DiscardReasonViewModel> viewModels = new ArrayList<DiscardReasonViewModel>();
        for(ComponentStatusChangeReason reason : discardReasons){
            viewModels.add(new DiscardReasonViewModel(reason));
        }
        return viewModels;
    }

}