/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package controller;

import backingform.GeneralConfigItemBackingForm;
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

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

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
        binder.setValidator(new GeneralConfigBackingFormValidator(binder.getValidator(), configRepository));
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    @PreAuthorize("hasRole('" + PermissionConstants.MANAGE_GENERAL_CONFIGS + "')")
    public ResponseEntity<Map<String, Object>> generalConfigGenerator(HttpServletRequest request) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("configs", configRepository.getAll());
        return new ResponseEntity<Map<String, Object>>(map, HttpStatus.OK);
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    @PreAuthorize("hasRole('" + PermissionConstants.MANAGE_GENERAL_CONFIGS + "')")
    public ResponseEntity addGeneralConfig(@RequestBody @Valid GeneralConfigItemBackingForm form) {
        configRepository.save(form.getGeneralConfig());
        return new ResponseEntity<GeneralConfig>(form.getGeneralConfig(), HttpStatus.CREATED);
    }

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    @PreAuthorize("hasRole('"+PermissionConstants.MANAGE_GENERAL_CONFIGS+"')")

    public ResponseEntity<Map<String, Object>> getGeneralConfig(@PathVariable Integer id) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("config", configRepository.getGeneralConfigById(id));
        return new ResponseEntity<Map<String, Object>>(map, HttpStatus.OK);
    }

    @RequestMapping(value = "{id}", method = RequestMethod.PUT)
    @PreAuthorize("hasRole('"+PermissionConstants.MANAGE_GENERAL_CONFIGS+"')")
    public ResponseEntity<Map<String, Object>> updateGeneralConfig(@RequestBody  @Valid GeneralConfigItemBackingForm form, @PathVariable Integer id) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("config", configRepository.getGeneralConfigById(id));
        return new ResponseEntity<Map<String, Object>>(map, HttpStatus.OK);
    }
}