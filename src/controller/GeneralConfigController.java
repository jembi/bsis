package controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import model.admin.GeneralConfig;

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

import repository.DataTypeRepository;
import repository.GeneralConfigRepository;
import utils.LoggerUtil;
import utils.PermissionConstants;
import viewmodel.GeneralConfigViewModel;
import backingform.GeneralConfigBackingForm;
import backingform.validator.GeneralConfigBackingFormValidator;


@RestController
@RequestMapping("/configurations")
public class GeneralConfigController {

    @Autowired
    private GeneralConfigRepository configRepository;

    @Autowired
    private DataTypeRepository dataTypeRepository;

    @Autowired
    private UtilController utilController;
    
    @Autowired
    private GeneralConfigBackingFormValidator generalConfigBackingFormValidator;

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.setValidator(generalConfigBackingFormValidator);
    }

    @RequestMapping(method = RequestMethod.GET)
    // no @PreAuthorize() - allow anonymous access to GET list of general configs
    public ResponseEntity<Map<String, Object>> generalConfigGenerator() {
    	
    	Map<String, Object> map = new HashMap<String, Object>();
        List<GeneralConfigViewModel> configs = new ArrayList<GeneralConfigViewModel>();
        for (GeneralConfig config : configRepository.getAll()) {
            configs.add(new GeneralConfigViewModel(config));
        }
        map.put("configurations", configs);
        return new ResponseEntity<Map<String, Object>>(map, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST)
    @PreAuthorize("hasRole('" + PermissionConstants.MANAGE_GENERAL_CONFIGS + "')")
    public ResponseEntity<GeneralConfigViewModel> addGeneralConfig(@RequestBody @Valid GeneralConfigBackingForm form) {
        configRepository.save(form.getGeneralConfig());
        return new ResponseEntity<GeneralConfigViewModel>(new GeneralConfigViewModel(configRepository.getGeneralConfigByName(form.getName())), HttpStatus.CREATED);
    }

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    @PreAuthorize("hasRole('"+PermissionConstants.MANAGE_GENERAL_CONFIGS+"')")
    public ResponseEntity<GeneralConfigViewModel> getGeneralConfig(@PathVariable Long id) {
        return new ResponseEntity<GeneralConfigViewModel>(new GeneralConfigViewModel(configRepository.getGeneralConfigById(id)), HttpStatus.OK);
    }

    @RequestMapping(value = "{id}", method = RequestMethod.PUT)
    @PreAuthorize("hasRole('"+PermissionConstants.MANAGE_GENERAL_CONFIGS+"')")
    public ResponseEntity<GeneralConfigViewModel> updateGeneralConfig(@RequestBody  @Valid GeneralConfigBackingForm form, @PathVariable Long id) {

    	GeneralConfig updatedConfig = null;
        form.setId(id);
        updatedConfig = configRepository.update(form.getGeneralConfig());

        //Update log level if changed
        if (form.getName().equalsIgnoreCase("log.level"))
            LoggerUtil.setLogLevel(configRepository.getGeneralConfigByName("log.level").getValue());

        return new ResponseEntity<GeneralConfigViewModel>(new GeneralConfigViewModel(updatedConfig), HttpStatus.OK);
    }
}