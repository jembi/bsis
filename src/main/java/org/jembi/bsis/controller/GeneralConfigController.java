package org.jembi.bsis.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.validation.Valid;

import org.jembi.bsis.backingform.GeneralConfigBackingForm;
import org.jembi.bsis.backingform.validator.GeneralConfigBackingFormValidator;
import org.jembi.bsis.factory.GeneralConfigFactory;
import org.jembi.bsis.model.admin.GeneralConfig;
import org.jembi.bsis.repository.GeneralConfigRepository;
import org.jembi.bsis.utils.LoggerUtil;
import org.jembi.bsis.utils.PermissionConstants;
import org.jembi.bsis.viewmodel.GeneralConfigViewModel;
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

@RestController
@RequestMapping("/configurations")
public class GeneralConfigController {

  @Autowired
  private GeneralConfigRepository configRepository;

  @Autowired
  private GeneralConfigBackingFormValidator generalConfigBackingFormValidator;

  @Autowired
  private GeneralConfigFactory generalConfigFactory;
  
  @InitBinder
  protected void initBinder(WebDataBinder binder) {
    binder.setValidator(generalConfigBackingFormValidator);
  }

  @RequestMapping(method = RequestMethod.GET)
  // no @PreAuthorize() - allow anonymous access to GET list of general configs
  public ResponseEntity<Map<String, Object>> generalConfigGenerator() {

    Map<String, Object> map = new HashMap<String, Object>();
    map.put("configurations", generalConfigFactory.createViewModels(configRepository.getAll()));
    return new ResponseEntity<Map<String, Object>>(map, HttpStatus.OK);
  }

  @RequestMapping(method = RequestMethod.POST)
  @PreAuthorize("hasRole('" + PermissionConstants.MANAGE_GENERAL_CONFIGS + "')")
  public ResponseEntity<GeneralConfigViewModel> addGeneralConfig(@RequestBody @Valid GeneralConfigBackingForm form) {
    configRepository.save(form.getGeneralConfig());
    return new ResponseEntity<GeneralConfigViewModel>(generalConfigFactory.createViewModel(
        configRepository.getGeneralConfigByName(form.getName())), HttpStatus.CREATED);
  }

  @RequestMapping(value = "{id}", method = RequestMethod.GET)
  @PreAuthorize("hasRole('" + PermissionConstants.MANAGE_GENERAL_CONFIGS + "')")
  public ResponseEntity<GeneralConfigViewModel> getGeneralConfig(@PathVariable UUID id) {
    return new ResponseEntity<GeneralConfigViewModel>(generalConfigFactory.createViewModel(
        configRepository.getGeneralConfigById(id)), HttpStatus.OK);
  }

  @RequestMapping(value = "{id}", method = RequestMethod.PUT)
  @PreAuthorize("hasRole('" + PermissionConstants.MANAGE_GENERAL_CONFIGS + "')")
  public ResponseEntity<GeneralConfigViewModel> updateGeneralConfig(@RequestBody @Valid GeneralConfigBackingForm form, @PathVariable UUID id) {

    GeneralConfig updatedConfig = null;
    form.setId(id);
    updatedConfig = configRepository.update(form.getGeneralConfig());

    //Update log level if changed
    if (form.getName().equalsIgnoreCase("log.level"))
      LoggerUtil.setLogLevel(configRepository.getGeneralConfigByName("log.level").getValue());

    return new ResponseEntity<GeneralConfigViewModel>(generalConfigFactory.createViewModel(updatedConfig), HttpStatus.OK);
  }
}