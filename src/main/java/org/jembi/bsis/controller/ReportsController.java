package org.jembi.bsis.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.jembi.bsis.controllerservice.ReportsControllerService;
import org.jembi.bsis.model.inventory.InventoryStatus;
import org.jembi.bsis.model.reporting.Report;
import org.jembi.bsis.service.BloodUnitsIssuedReportGeneratorService;
import org.jembi.bsis.service.ReportGeneratorService;
import org.jembi.bsis.service.TtiPrevalenceReportGeneratorService;
import org.jembi.bsis.utils.PermissionConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("reports")
public class ReportsController {

  @Autowired
  private ReportGeneratorService reportGeneratorService;
  
  @Autowired
  private TtiPrevalenceReportGeneratorService ttiPrevalenceReportGeneratorService;

  @Autowired
  private BloodUnitsIssuedReportGeneratorService bloodUnitsIssuedReportGeneratorService;

  @Autowired
  private ReportsControllerService reportsControllerService;

  @RequestMapping(value = "/stockLevels/generate", method = RequestMethod.GET)
  @PreAuthorize("hasRole('" + PermissionConstants.VIEW_INVENTORY_INFORMATION + "')")
  public Report findStockLevels(@RequestParam(value = "location", required = false) Long locationId,
      @RequestParam(value = "inventoryStatus", required = true) InventoryStatus inventoryStatus) {
    return reportGeneratorService.generateStockLevelsForLocationReport(locationId, inventoryStatus);
  }
  
  @RequestMapping(value = "/stockLevels/form", method = RequestMethod.GET)
  @PreAuthorize("hasRole('" + PermissionConstants.VIEW_INVENTORY_INFORMATION + "')")
  public Map<String, Object> stockLevelsFormGenerator() {
    Map<String, Object> map = new HashMap<>();
    map.put("distributionSites", reportsControllerService.getDistributionSites());
    return map;
  }

  @RequestMapping(value = "/collecteddonations/generate", method = RequestMethod.GET)
  @PreAuthorize("hasRole('" + PermissionConstants.DONATIONS_REPORTING + "')")
  public Report getCollectedDonationsReport(
      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date startDate,
      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date endDate) {
    return reportGeneratorService.generateCollectedDonationsReport(startDate, endDate);
  }
  
  @RequestMapping(value = "/ttiprevalence/generate", method = RequestMethod.GET)
  @PreAuthorize("hasRole('" + PermissionConstants.TTI_REPORTING + "')")
  public Report getTTIPrevalenceReport(
      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date startDate,
      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date endDate) {
    return ttiPrevalenceReportGeneratorService.generateTTIPrevalenceReport(startDate, endDate);
  }

  @RequestMapping(value = "/unitsissued/form", method = RequestMethod.GET)
  @PreAuthorize("hasRole('" + PermissionConstants.VIEW_INVENTORY_INFORMATION + "')")
  public Map<String, Object> getUnitsIssuedReportFormFields() {
    Map<String, Object> map = new HashMap<String, Object>();
    map.put("componentTypes", reportsControllerService.getAllComponentTypesThatCanBeIssued());
    return map;
  }

  @RequestMapping(value = "/unitsissued/generate", method = RequestMethod.GET)
  @PreAuthorize("hasRole('" + PermissionConstants.VIEW_INVENTORY_INFORMATION + "')")
  public Report generateUnitsIssuedReport(
      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date startDate,
      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date endDate) {
    return bloodUnitsIssuedReportGeneratorService.generateUnitsIssuedReport(startDate, endDate);
  }

}
