package controller;

import backingform.DeferralReasonBackingForm;
import backingform.validator.DeferralReasonBackingFormValidator;
import model.donordeferral.DeferralReason;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import repository.DeferralReasonRepository;
import utils.PermissionConstants;
import viewmodel.DeferralReasonViewModel;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("deferralreasons")
public class DeferralReasonController {

    private static final Logger LOGGER = Logger.getLogger(DeferralReasonController.class);

    @Autowired
    DeferralReasonRepository deferralReasonRepository;

    @Autowired
    private UtilController utilController;

    public DeferralReasonController() {
    }

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.setValidator(new DeferralReasonBackingFormValidator(utilController));
    }

    @RequestMapping(method=RequestMethod.GET)
    @PreAuthorize("hasRole('" + PermissionConstants.MANAGE_DEFERRAL_REASONS + "')")
    public  Map<String, Object> getDeferralReasons() {
        Map<String, Object> map = new HashMap<>();
        addAllDeferralReasonsToModel(map);
        return map;
    }

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    @PreAuthorize("hasRole('" + PermissionConstants.MANAGE_DEFERRAL_REASONS + "')")
    public ResponseEntity<DeferralReason> getDeferralReasonById(@PathVariable Integer id){
        Map<String, Object> map = new HashMap<>();
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
    public ResponseEntity updateDeferralReason(@Valid @RequestBody DeferralReasonBackingForm formData , @PathVariable Integer id){
        Map<String, Object> map = new HashMap<>();
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

        List<DeferralReasonViewModel> viewModels = new ArrayList<>();
        for(DeferralReason reason : deferralReasons){
            viewModels.add(new DeferralReasonViewModel(reason));
        }
        return viewModels;
    }

}

