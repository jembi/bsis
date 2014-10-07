package controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityExistsException;
import javax.servlet.http.HttpServletRequest;
import model.donor.Donor;
import model.donorcodes.DonorCode;
import model.donorcodes.DonorCodeGroup;
import model.donorcodes.DonorDonorCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import repository.DonorRepository;
import utils.PermissionConstants;
import viewmodel.DonorViewModel;

@RestController
@RequestMapping("donorcodes")
public class DonorCodeController {

    @Autowired
    private DonorRepository donorRepository;


    @RequestMapping(value = "/{id}/update/form", method = RequestMethod.GET)
    @PreAuthorize("hasRole('" + PermissionConstants.EDIT_DONOR_CODE + "')")
    public 
    Map<String, Object> updateDonorCodesForm(@PathVariable Long id) {

        Map<String, Object> map = new HashMap<String, Object>();
        Donor donor = donorRepository.findDonorById(id);
        map.put("donor", new DonorViewModel(donor));
        return map;

    }

    @RequestMapping(value = "/add/form", method = RequestMethod.GET)
    @PreAuthorize("hasRole('" + PermissionConstants.ADD_DONOR_CODE + "')")
    public 
    List<DonorCodeGroup> addDonorCodeFormGenerator() {
        return donorRepository.getAllDonorCodeGroups();
    }

    @RequestMapping(value = "/{id}/assign/{donorId}" ,method = RequestMethod.POST)
    @PreAuthorize("hasRole('" + PermissionConstants.ADD_DONOR_CODE + "')")
    public 
    Map<String, Object> addDonorCode(@PathVariable Long id,
        @PathVariable Long donorId) {
        Map<String, Object> map = new HashMap<String, Object>();
        DonorDonorCode donorDonorCode = new DonorDonorCode();
        donorDonorCode.setDonorId(donorRepository.findDonorById(donorId));
        DonorCode donorCode = donorRepository.findDonorCodeById(id);
        donorDonorCode.setDonorCodeId(donorCode);
        if( donorRepository.findDonorById(id).getDonorCodes().contains(donorCode))
           throw new EntityExistsException("Donor Code is already assigned to donor");
        donorRepository.saveDonorDonorCode(donorDonorCode);
        map.put("donorDonorCodes", donorRepository.findDonorDonorCodesOfDonorByDonorId(id));
        return map;

    }

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    @PreAuthorize("hasRole('" + PermissionConstants.VIEW_DONOR_CODE + "')")
    public 
    List<DonorDonorCode> donorCodesTable(@PathVariable Long id) {

        return donorRepository.findDonorDonorCodesOfDonorByDonorId(id);

    }

    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    @PreAuthorize("hasRole('" + PermissionConstants.VOID_DONOR_CODE + "')")
    public 
    List<DonorDonorCode> deleteDomorCode(@RequestParam(value = "id") Long id) {
        Donor donor = donorRepository.deleteDonorCode(id);
        return donorRepository.findDonorDonorCodesOfDonorByDonorId(donor.getId());
    }

}
