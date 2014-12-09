package controller;

import backingform.DonorCodeBackingForm;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityExistsException;
import javax.validation.Valid;
import model.donor.Donor;
import model.donorcodes.DonorCode;
import model.donorcodes.DonorCodeGroup;
import model.donorcodes.DonorDonorCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import repository.DonorRepository;
import utils.PermissionConstants;
import viewmodel.DonorCodeViewModel;
import viewmodel.DonorViewModel;

@RestController
@RequestMapping("donorcodes")
public class DonorCodeController {

    @Autowired
    private DonorRepository donorRepository;

    @RequestMapping(value = "/form", method = RequestMethod.GET)
    @PreAuthorize("hasRole('" + PermissionConstants.ADD_DONOR_CODE + "')")
    public 
    List<DonorCodeGroup> addDonorCodeFormGenerator() {
        return donorRepository.getAllDonorCodeGroups();
    }

    @RequestMapping(method = RequestMethod.POST)
    @PreAuthorize("hasRole('" + PermissionConstants.ADD_DONOR_CODE + "')")
    public 
    ResponseEntity<Map <String, Object>> addDonorCode(
            @Valid @RequestBody DonorCodeBackingForm formObject) {
        Map<String, Object> map = new HashMap<String, Object>();
        DonorDonorCode donorDonorCode = new DonorDonorCode();
        Long donorId = formObject.getDonorId();
        donorDonorCode.setDonor(donorRepository.findDonorById(donorId));
        DonorCode donorCode = donorRepository.findDonorCodeById(formObject.getDonorCodeId());
        donorDonorCode.setDonorCode(donorCode);
        if( donorRepository.findDonorById(donorId).getDonorCodes().contains(donorCode))
           throw new EntityExistsException("Donor Code is already assigned to donor");
        donorRepository.saveDonorDonorCode(donorDonorCode);
        List<DonorDonorCode>  donorDonorCodes = donorRepository.findDonorDonorCodesOfDonorByDonorId(donorId);
        map.put("donorCodeViewModels", getDonorCodeViewModels(donorDonorCodes));
        return new ResponseEntity<Map<String , Object>>(map , HttpStatus.OK);

    }

    @RequestMapping(value ="{donorId}" , method = RequestMethod.GET)
    @PreAuthorize("hasRole('" + PermissionConstants.VIEW_DONOR_CODE + "')")
    public 
    ResponseEntity<Map <String, Object>> donorCodesTable(@PathVariable Long donorId) {

        HashMap<String, Object> map = new HashMap<String, Object>();
        List<DonorDonorCode>  donorDonorCodes = donorRepository.findDonorDonorCodesOfDonorByDonorId(donorId);
        map.put("donorCodeViewModels", getDonorCodeViewModels(donorDonorCodes));
        return new ResponseEntity<Map<String , Object>>(map , HttpStatus.OK);

    }

    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    @PreAuthorize("hasRole('" + PermissionConstants.VOID_DONOR_CODE + "')")
    public 
   ResponseEntity<Map <String, Object>> deleteDomorCode(@PathVariable Long id) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        Donor donor = donorRepository.deleteDonorCode(id);
        List<DonorDonorCode> donorDonorCodes = donorRepository.findDonorDonorCodesOfDonorByDonorId(donor.getId());
        map.put("donorCodeViewModels", getDonorCodeViewModels(donorDonorCodes));
        return new ResponseEntity<Map<String, Object>>(map, HttpStatus.OK);
    }
   
    private  List<DonorCodeViewModel> getDonorCodeViewModels(
            List<DonorDonorCode> donorDonorCodes) {
        if (donorDonorCodes == null) {
            return Arrays.asList(new DonorCodeViewModel[0]);
        }
        List<DonorCodeViewModel> DonorCodeViewModel = new ArrayList<DonorCodeViewModel>();
        for (DonorDonorCode donorDonorCode : donorDonorCodes) {
            DonorCodeViewModel.add(new DonorCodeViewModel(donorDonorCode));
        }
        return DonorCodeViewModel;
    }

}
