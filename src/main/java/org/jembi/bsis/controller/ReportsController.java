package org.jembi.bsis.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.jembi.bsis.controllerservice.ReportsControllerService;
import org.jembi.bsis.model.inventory.InventoryStatus;
import org.jembi.bsis.model.reporting.Report;
import org.jembi.bsis.service.report.BloodUnitsIssuedReportGenerator;
import org.jembi.bsis.service.report.CollectedDonationsReportGenerator;
import org.jembi.bsis.service.report.DiscardedComponentReportGenerator;
import org.jembi.bsis.service.report.DonorsDeferredSummaryReportGenerator;
import org.jembi.bsis.service.report.StockLevelsReportGenerator;
import org.jembi.bsis.service.report.TtiPrevalenceReportGenerator;
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
  private StockLevelsReportGenerator stockLevelsReportGenerator;
  
  @Autowired
  private TtiPrevalenceReportGenerator ttiPrevalenceReportGenerator;

  @Autowired
  private BloodUnitsIssuedReportGenerator bloodUnitsIssuedReportGenerator;
  
  @Autowired
  private DonorsDeferredSummaryReportGenerator donorsDeferredSummaryReportGenerator;
  
  @Autowired
  private CollectedDonationsReportGenerator collectedDonationsReportGenerator;

  @Autowired
  private ReportsControllerService reportsControllerService;
  
  @Autowired
  private DiscardedComponentReportGenerator discardedComponentReportGenerator;
  
  @RequestMapping(value = "/discardedunits/form", method = RequestMethod.GET)
  @PreAuthorize("hasRole('" + PermissionConstants.COMPONENTS_REPORTING + "')")
  public Map<String, Object> discardedUnitsFormFields() {
    Map<String, Object> map = new HashMap<>();
    map.put("processingSites", reportsControllerService.getProcessingSites());
    map.put("componentTypes", reportsControllerService.getAllComponentTypes());
    map.put("discardReasons", reportsControllerService.getAllDiscardReasons());
    return map;
  }
  
  @RequestMapping(value = "/discardedunits/generate", method = RequestMethod.GET)
  @PreAuthorize("hasRole('" + PermissionConstants.COMPONENTS_REPORTING + "')") 
  public Report generateDiscardedUnits (
      @RequestParam(value = "processingSite", required = false) Long processingSiteId,
      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date startDate,
      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date endDate) {
    return discardedComponentReportGenerator.generateDiscardedComponents(processingSiteId, startDate, endDate);
  }

  @RequestMapping(value = "/stockLevels/generate", method = RequestMethod.GET)
  @PreAuthorize("hasRole('" + PermissionConstants.VIEW_INVENTORY_INFORMATION + "')")
  public Report findStockLevels(@RequestParam(value = "location", required = false) Long locationId,
      @RequestParam(value = "inventoryStatus", required = true) InventoryStatus inventoryStatus) {
    return stockLevelsReportGenerator.generateStockLevelsForLocationReport(locationId, inventoryStatus);
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
    return collectedDonationsReportGenerator.generateCollectedDonationsReport(startDate, endDate);
  }
  
  @RequestMapping(value = "/ttiprevalence/generate", method = RequestMethod.GET)
  @PreAuthorize("hasRole('" + PermissionConstants.TTI_REPORTING + "')")
  public Report getTTIPrevalenceReport(
      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date startDate,
      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date endDate) {
    return ttiPrevalenceReportGenerator.generateTTIPrevalenceReport(startDate, endDate);
  }

  @RequestMapping(value = "/unitsissued/form", method = RequestMethod.GET)
  @PreAuthorize("hasRole('" + PermissionConstants.COMPONENTS_REPORTING + "')")
  public Map<String, Object> getUnitsIssuedReportFormFields() {
    Map<String, Object> map = new HashMap<String, Object>();
    map.put("componentTypes", reportsControllerService.getAllComponentTypesThatCanBeIssued());
    return map;
  }

  @RequestMapping(value = "/unitsissued/generate", method = RequestMethod.GET)
  @PreAuthorize("hasRole('" + PermissionConstants.COMPONENTS_REPORTING + "')")
  public Report generateUnitsIssuedReport(
      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date startDate,
      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date endDate) {
    return bloodUnitsIssuedReportGenerator.generateUnitsIssuedReport(startDate, endDate);
  }
  
  @RequestMapping(value = "/donorsdeferred/form", method = RequestMethod.GET)
  @PreAuthorize("hasRole('" + PermissionConstants.DONORS_REPORTING + "')")
  public Map<String, Object> generateDonorsDeferredFormFields() {
    Map<String, Object> map = new HashMap<String, Object>();
    map.put("deferralReasons", reportsControllerService.getDeferralReasons());
    return map;  
  }

  @RequestMapping(value = "/donorsdeferred/generate", method = RequestMethod.GET)
  @PreAuthorize("hasRole('" + PermissionConstants.DONORS_REPORTING + "')")
  public Report generateDonorsDeferredReport(
      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date startDate,
      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date endDate) {
    return donorsDeferredSummaryReportGenerator.generateDonorDeferralSummaryReport(startDate, endDate);
  }
}
