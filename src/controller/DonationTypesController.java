package controller;

import backingform.DonationTypeBackingForm;
import backingform.validator.DonationTypeBackingFormValidator;
import model.donationtype.DonationType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import repository.DonationTypeRepository;
import utils.PermissionConstants;
import javax.validation.Valid;

import org.apache.log4j.Logger;
import viewmodel.DonationTypeViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("donationtypes")
public class DonationTypesController {

    private static final Logger LOGGER = Logger.getLogger(DonationTypesController.class);

    @Autowired
    DonationTypeRepository donationTypesRepository;

    @Autowired
    private UtilController utilController;

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.setValidator(new DonationTypeBackingFormValidator(binder.getValidator(), utilController, donationTypesRepository));
    }

    @RequestMapping(method=RequestMethod.GET)
    @PreAuthorize("hasRole('"+PermissionConstants.MANAGE_DONATION_TYPES+"')")
    public Map<String, Object> configureDonationTypesFormGenerator() {
        Map<String, Object> map = new HashMap<>();
        addAllDonationTypesToModel(map);
        return map;
    }

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    @PreAuthorize("hasRole('"+PermissionConstants.MANAGE_DONATION_TYPES+"')")
    public  ResponseEntity getDonationType(@PathVariable Long id) {
        Map<String, Object> map = new HashMap<>();
        DonationType donationType = donationTypesRepository.getDonationTypeById(id);
        map.put("donationType", new DonationTypeViewModel(donationType));
        return new ResponseEntity(map, HttpStatus.OK);

    }

    @RequestMapping(method = RequestMethod.POST)
    @PreAuthorize("hasRole('"+PermissionConstants.MANAGE_DONATION_TYPES+"')")
    public  ResponseEntity saveDonationType(@Valid @RequestBody DonationTypeBackingForm form) {
        DonationType donationType = donationTypesRepository.saveDonationType(form.getDonationType());
        return new ResponseEntity(new DonationTypeViewModel(donationType), HttpStatus.CREATED);
    }

    @RequestMapping(value = "{id}", method = RequestMethod.PUT)
    @PreAuthorize("hasRole('"+PermissionConstants.MANAGE_DONATION_TYPES+"')")
    public  ResponseEntity updateDonationType(@Valid @RequestBody DonationTypeBackingForm form,  @PathVariable Long id) {
        Map<String, Object> map = new HashMap<>();
        DonationType donationType = form.getDonationType();
        donationType.setId(id);
        donationType = donationTypesRepository.updateDonationType(donationType);
        map.put("donationType", new DonationTypeViewModel(donationType));
        return new ResponseEntity(map, HttpStatus.OK);
    }

    private void addAllDonationTypesToModel(Map<String, Object> m) {
        m.put("allDonationTypes", getDonationTypeViewModels(donationTypesRepository.getAllDonationTypes(true)));
    }

    private List<DonationTypeViewModel> getDonationTypeViewModels(List<DonationType> donationTypes){
        List<DonationTypeViewModel> viewModels = new ArrayList<>();
        for(DonationType donationType : donationTypes){
            viewModels.add(new DonationTypeViewModel(donationType));
        }
        return viewModels;
    }
}
