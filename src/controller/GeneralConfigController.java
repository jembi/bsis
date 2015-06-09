/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package controller;

import backingform.GeneralConfigBackingForm;
import backingform.validator.GeneralConfigBackingFormValidator;
import model.admin.GeneralConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import repository.GeneralConfigRepository;
import utils.PermissionConstants;

import javax.validation.Valid;
import java.util.List;

/**
 * @author Duma Mtungwa
 */
@RestController
@RequestMapping(value = "/configure")
public class GeneralConfigController {

    @Autowired
    private GeneralConfigRepository configRepository;

    @Autowired
    private UtilController utilController;

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.setValidator(new GeneralConfigBackingFormValidator(binder.getValidator(), configRepository, utilController));
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    @PreAuthorize("hasRole('" + PermissionConstants.MANAGE_GENERAL_CONFIGS + "')")
    public ResponseEntity<List<GeneralConfig>> generalConfigGenerator() {
        return new ResponseEntity<List<GeneralConfig>>(configRepository.getAll(), HttpStatus.OK);
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    @PreAuthorize("hasRole('" + PermissionConstants.MANAGE_GENERAL_CONFIGS + "')")
    public ResponseEntity addGeneralConfig(@RequestBody @Valid GeneralConfigBackingForm form) {
        configRepository.save(form.getGeneralConfig());
        return new ResponseEntity<GeneralConfig>(form.getGeneralConfig(), HttpStatus.CREATED);
    }

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    @PreAuthorize("hasRole('"+PermissionConstants.MANAGE_GENERAL_CONFIGS+"')")
    public ResponseEntity<GeneralConfig> getGeneralConfig(@PathVariable Integer id) {
        return new ResponseEntity<GeneralConfig>(configRepository.getGeneralConfigById(id), HttpStatus.OK);
    }

    @RequestMapping(value = "{id}", method = RequestMethod.PUT)
    @PreAuthorize("hasRole('"+PermissionConstants.MANAGE_GENERAL_CONFIGS+"')")
    public ResponseEntity<GeneralConfig> updateGeneralConfig(@RequestBody  @Valid GeneralConfigBackingForm form, @PathVariable Integer id) {
        GeneralConfig updatedConfig = form.getGeneralConfig();
        updatedConfig.setId(id);
        configRepository.update(updatedConfig);
        return new ResponseEntity<GeneralConfig>(updatedConfig, HttpStatus.CREATED);
    }
}