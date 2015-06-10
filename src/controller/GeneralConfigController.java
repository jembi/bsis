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
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import repository.DataTypeRepository;
import repository.GeneralConfigRepository;
import utils.PermissionConstants;
import viewmodel.GeneralConfigViewModel;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping(value = "/configure")
public class GeneralConfigController {

    @Autowired
    private GeneralConfigRepository configRepository;

    @Autowired
    private DataTypeRepository dataTypeRepository;

    @Autowired
    private UtilController utilController;

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.setValidator(new GeneralConfigBackingFormValidator(binder.getValidator(), configRepository, utilController, dataTypeRepository));
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    @PreAuthorize("hasRole('" + PermissionConstants.MANAGE_GENERAL_CONFIGS + "')")
    public ResponseEntity<List<GeneralConfigViewModel>> generalConfigGenerator() {
        List<GeneralConfigViewModel> configs = new ArrayList<GeneralConfigViewModel>();
        for (GeneralConfig config : configRepository.getAll()) {
            configs.add(new GeneralConfigViewModel(config));
        }
        return new ResponseEntity<List<GeneralConfigViewModel>>(configs, HttpStatus.OK);
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    @PreAuthorize("hasRole('" + PermissionConstants.MANAGE_GENERAL_CONFIGS + "')")
    public ResponseEntity<GeneralConfigViewModel> addGeneralConfig(@RequestBody @Valid GeneralConfigBackingForm form) {
        configRepository.save(form.getGeneralConfig());
        return new ResponseEntity<GeneralConfigViewModel>(new GeneralConfigViewModel(configRepository.getGeneralConfigByName(form.getName())), HttpStatus.CREATED);
    }

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    @PreAuthorize("hasRole('"+PermissionConstants.MANAGE_GENERAL_CONFIGS+"')")
    public ResponseEntity<GeneralConfigViewModel> getGeneralConfig(@PathVariable Integer id) {
        return new ResponseEntity<GeneralConfigViewModel>(new GeneralConfigViewModel(configRepository.getGeneralConfigById(id)), HttpStatus.OK);
    }

    @RequestMapping(value = "{id}", method = RequestMethod.PUT)
    @PreAuthorize("hasRole('"+PermissionConstants.MANAGE_GENERAL_CONFIGS+"')")
    public ResponseEntity<GeneralConfigViewModel> updateGeneralConfig(@RequestBody  @Valid GeneralConfigBackingForm form, @PathVariable Integer id) {

    	GeneralConfig updatedConfig = null;
        form.setId(id);
        updatedConfig = configRepository.update(form.getGeneralConfig());

        return new ResponseEntity<GeneralConfigViewModel>(new GeneralConfigViewModel(updatedConfig), HttpStatus.CREATED);
    }
}