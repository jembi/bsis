package controller;

import model.donationtype.DonationType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import repository.DonationTypeRepository;
import utils.PermissionConstants;

import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

@RestController
public class DonationTypesController {

    private static final Logger LOGGER = Logger.getLogger(DonationTypesController.class);

    @Autowired
    DonationTypeRepository donationTypesRepository;

    @RequestMapping(value="/donationtypes", method=RequestMethod.GET)
    @PreAuthorize("hasRole('"+PermissionConstants.MANAGE_DONATION_TYPES+"')")
    public Map<String, Object> configureDonationTypesFormGenerator() {
        Map<String, Object> map = new HashMap<String, Object>();
        addAllDonationTypesToModel(map);
        return map;
    }

    private void addAllDonationTypesToModel(Map<String, Object> m) {
        m.put("allDonationTypes", donationTypesRepository.getAllDonationTypes());
    }


    @RequestMapping(value = "/donationtypes/{id}", method = RequestMethod.GET)
    @PreAuthorize("hasRole('"+PermissionConstants.MANAGE_DONATION_TYPES+"')")
    public  ResponseEntity getDonationType(@PathVariable Integer id) {
        Map<String, Object> map = new HashMap<String, Object>();
        DonationType donationType = donationTypesRepository.getDonationTypeById(id);
        map.put("donationType", donationType);
        return new ResponseEntity(map, HttpStatus.OK);

    }

    @RequestMapping(value = "/donationtypes", method = RequestMethod.POST)
    @PreAuthorize("hasRole('"+PermissionConstants.MANAGE_DONATION_TYPES+"')")
    public  ResponseEntity saveDonationType(@RequestBody DonationType donationType) {
        donationTypesRepository.saveDonationType(donationType);
        return new ResponseEntity(donationType, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/donationtypes/{id}", method = RequestMethod.PUT)
    @PreAuthorize("hasRole('"+PermissionConstants.MANAGE_DONATION_TYPES+"')")
    public  ResponseEntity updateDonationType(@PathVariable Integer id,
                                              @RequestBody DonationType donationType) {
        Map<String, Object> map = new HashMap<String, Object>();
        donationType.setId(id);
        donationType = donationTypesRepository.updateDonationType(donationType);
        map.put("donationType", donationType);
        return new ResponseEntity(map , HttpStatus.OK);
    }
}
