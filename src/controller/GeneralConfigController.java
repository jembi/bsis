/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package controller;

import backingform.GeneralConfigBackingForm;
import backingform.GeneralConfigItemBackingForm;
import backingform.validator.GeneralConfigFormValidator;
import model.admin.GeneralConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import repository.GeneralConfigRepository;
import utils.PermissionConstants;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
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
        binder.setValidator(new GeneralConfigFormValidator(binder.getValidator(), configRepository));
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    @PreAuthorize("hasRole('" + PermissionConstants.MANAGE_GENERAL_CONFIGS + "')")
    public ResponseEntity<Map<String, Object>> generalConfigGenerator(HttpServletRequest request) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("generalConfigs", configRepository.getAll());
        return new ResponseEntity<Map<String, Object>>(map, HttpStatus.OK);
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    @PreAuthorize("hasRole('" + PermissionConstants.MANAGE_GENERAL_CONFIGS + "')")
    public ResponseEntity addGeneralConfig(@Valid @RequestBody GeneralConfigItemBackingForm form
                                                                ) throws IOException {

        configRepository.update(form.getGeneralConfig());
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
    public ResponseEntity<Map<String, Object>> updateGeneralConfig(@RequestBody  @Valid GeneralConfigItemBackingForm form,
                                                                @PathVariable Integer id) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("config", configRepository.getGeneralConfigById(id));
        return new ResponseEntity<Map<String, Object>>(map, HttpStatus.OK);
    }
}