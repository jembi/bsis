package controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import model.donordeferral.DeferralReason;

import org.apache.log4j.Logger;
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

import repository.DeferralReasonRepository;
import utils.PermissionConstants;
import viewmodel.DeferralReasonViewModel;
import backingform.DeferralReasonBackingForm;
import backingform.validator.DeferralReasonBackingFormValidator;

@RestController
@RequestMapping("deferralreasons")
public class DeferralReasonController {

    private static final Logger LOGGER = Logger.getLogger(DeferralReasonController.class);

    @Autowired
    DeferralReasonRepository deferralReasonRepository;
    
    @Autowired
    DeferralReasonBackingFormValidator deferralReasonBackingFormValidator;

    public DeferralReasonController() {
    }

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.setValidator(deferralReasonBackingFormValidator);
    }

    @RequestMapping(method=RequestMethod.GET)
    @PreAuthorize("hasRole('" + PermissionConstants.MANAGE_DEFERRAL_REASONS + "')")
    public  Map<String, Object> getDeferralReasons() {
        Map<String, Object> map = new HashMap<String, Object>();
        addAllDeferralReasonsToModel(map);
        return map;
    }

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    @PreAuthorize("hasRole('" + PermissionConstants.MANAGE_DEFERRAL_REASONS + "')")
    public ResponseEntity<DeferralReason> getDeferralReasonById(@PathVariable Long id){
        Map<String, Object> map = new HashMap<String, Object>();
        DeferralReason deferralReason = deferralReasonRepository.getDeferralReasonById(id);
        map.put("reason", new DeferralReasonViewModel(deferralReason));
        return new ResponseEntity(map, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST)
    @PreAuthorize("hasRole('" + PermissionConstants.MANAGE_DEFERRAL_REASONS + "')")
    public ResponseEntity saveDeferralReason(@Valid @RequestBody DeferralReasonBackingForm formData){
        DeferralReason deferralReason = formData.getDeferralReason();
        deferralReason = deferralReasonRepository.saveDeferralReason(deferralReason);
        return new ResponseEntity(new DeferralReasonViewModel(deferralReason), HttpStatus.CREATED);
    }

    @RequestMapping(value = "{id}", method = RequestMethod.PUT)
    @PreAuthorize("hasRole('" + PermissionConstants.MANAGE_DEFERRAL_REASONS + "')")
    public ResponseEntity updateDeferralReason(@Valid @RequestBody DeferralReasonBackingForm formData , @PathVariable Long id){
        Map<String, Object> map = new HashMap<String, Object>();
        DeferralReason deferralReason = formData.getDeferralReason();
        deferralReason.setId(id);
        deferralReason = deferralReasonRepository.updateDeferralReason(deferralReason);
        map.put("reason", new DeferralReasonViewModel(deferralReason));
        return new ResponseEntity(map, HttpStatus.OK);
    }

    private void addAllDeferralReasonsToModel(Map<String, Object> m) {
        m.put("allDeferralReasons", getDeferralReasonViewModels(deferralReasonRepository.getAllDeferralReasons()));
    }

    private List<DeferralReasonViewModel> getDeferralReasonViewModels(List<DeferralReason> deferralReasons){

        List<DeferralReasonViewModel> viewModels = new ArrayList<DeferralReasonViewModel>();
        for(DeferralReason reason : deferralReasons){
            viewModels.add(new DeferralReasonViewModel(reason));
        }
        return viewModels;
    }

}

