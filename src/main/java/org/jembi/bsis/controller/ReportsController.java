package org.jembi.bsis.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.jembi.bsis.controllerservice.ReportsControllerService;
import org.jembi.bsis.model.inventory.InventoryStatus;
import org.jembi.bsis.model.reporting.Report;
import org.jembi.bsis.service.report.BloodUnitsIssuedReportGenerator;
import org.jembi.bsis.service.report.CollectedDonationsReportGenerator;
import org.jembi.bsis.service.report.ComponentProductionReportGenerator;
import org.jembi.bsis.service.report.DiscardedComponentReportGenerator;
import org.jembi.bsis.service.report.DonorsAdverseEventsReportGenerator;
import org.jembi.bsis.service.report.DonorsDeferredSummaryReportGenerator;
import org.jembi.bsis.service.report.StockLevelsReportGenerator;
import org.jembi.bsis.service.report.TransfusionSummaryReportGenerator;
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
  private DonorsAdverseEventsReportGenerator donorsAdverseEventsReportGenerator;

  @Autowired
  private ReportsControllerService reportsControllerService;
  
  @Autowired
  private DiscardedComponentReportGenerator discardedComponentReportGenerator;

  @Autowired
  private ComponentProductionReportGenerator componentProductionReportGenerator;

  @Autowired
  private TransfusionSummaryReportGenerator transfusionSummaryReportGenerator;

  @RequestMapping(value = "/transfusionsummary/form", method = RequestMethod.GET)
  @PreAuthorize("hasRole('" + PermissionConstants.TRANSFUSIONS_REPORTING + "')")
  public Map<String, Object> transfusionSummaryFormFields() {
    Map<String, Object> map = new HashMap<>();
    map.put("usageSites", reportsControllerService.getUsageSites());
    map.put("transfusionReactionTypes", reportsControllerService.getTransfusionReactionTypes());
    return map;
  }

  @RequestMapping(value = "/transfusionsummary/generate", method = RequestMethod.GET)
  @PreAuthorize("hasRole('" + PermissionConstants.TRANSFUSIONS_REPORTING + "')")
  public Report generateTransfusionSummaryReport(
      @RequestParam(value = "transfusionSiteId", required = false) UUID transfusionSiteId,
      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date startDate,
      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date endDate) {
    return transfusionSummaryReportGenerator.generateTransfusionSummaryReport(transfusionSiteId, startDate, endDate);
  }
  
  @RequestMapping(value = "/discardedunits/form", method = RequestMethod.GET)
  @PreAuthorize("hasRole('" + PermissionConstants.COMPONENTS_REPORTING + "')")
  public Map<String, Object> discardedUnitsFormFields() {
    Map<String, Object> map = new HashMap<>();
    map.put("processingSites", reportsControllerService.getProcessingSites());
    map.put("componentTypes", reportsControllerService.getAllComponentTypes());
    map.put("discardReasons", reportsControllerService.getAllDiscardReasons(false));
    return map;
  }
  
  @RequestMapping(value = "/discardedunits/generate", method = RequestMethod.GET)
  @PreAuthorize("hasRole('" + PermissionConstants.COMPONENTS_REPORTING + "')") 
  public Report generateDiscardedUnits (
      @RequestParam(value = "processingSite", required = false) UUID processingSiteId,
      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date startDate,
      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date endDate) {
    return discardedComponentReportGenerator.generateDiscardedComponents(processingSiteId, startDate, endDate);
  }

  @RequestMapping(value = "/stockLevels/generate", method = RequestMethod.GET)
  @PreAuthorize("hasRole('" + PermissionConstants.VIEW_INVENTORY_INFORMATION + "')")
  public Report findStockLevels(@RequestParam(value = "location", required = false) UUID locationId,
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

  @RequestMapping(value = "/collecteddonations/form", method = RequestMethod.GET)
  @PreAuthorize("hasRole('" + PermissionConstants.DONATIONS_REPORTING + "')")
  public Map<String, Object> getCollectedDonationsReportFormFields() {
    Map<String, Object> map = new HashMap<String, Object>();
    map.put("donationTypes", reportsControllerService.getDonationTypes());
    return map;
  }
  
  @RequestMapping(value = "/ttiprevalence/form", method = RequestMethod.GET)
  @PreAuthorize("hasRole('" + PermissionConstants.TTI_REPORTING + "')")
  public Map<String, Object> getActiveTTIBloodTestsReportForm() {
    Map<String, Object> map = new HashMap<String, Object>();
    map.put("ttiBloodTests", reportsControllerService.getEnabledTTIBloodTests());
    return map;
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

  @RequestMapping(value = "/donorsadverseevents/form", method = RequestMethod.GET)
  @PreAuthorize("hasRole('" + PermissionConstants.DONATIONS_REPORTING + "')")
  public Map<String, Object> generateDonorsAdverseEventsForm() {
    Map<String, Object> map = new HashMap<String, Object>();
    map.put("venues", reportsControllerService.getVenues());
    map.put("adverseEventTypes", reportsControllerService.getAdverseEventTypes());
    return map;
  }

  @RequestMapping(value = "/donorsadverseevents/generate", method = RequestMethod.GET)
  @PreAuthorize("hasRole('" + PermissionConstants.DONATIONS_REPORTING + "')")
  public Report generateDonorsAdverseEventsReport(
      @RequestParam(value = "venue", required = false) UUID venueId,
      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date startDate,
      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date endDate) {
    return donorsAdverseEventsReportGenerator.generateDonorsAdverseEventsReport(venueId, startDate, endDate);
  }

  @RequestMapping(value = "/componentsprocessed/form", method = RequestMethod.GET)
  @PreAuthorize("hasRole('" + PermissionConstants.COMPONENTS_REPORTING + "')")
  public Map<String, Object> getProcessingSitesAndComponentTypes() {
    Map<String, Object> map = new HashMap<String, Object>();
    map.put("processingSites", reportsControllerService.getProcessingSites());
    map.put("componentTypes", reportsControllerService.getAllComponentTypesThatCanBeIssued());
    return map;
  }
  
  @RequestMapping(value = "/componentsprocessed/generate", method = RequestMethod.GET)
  @PreAuthorize("hasRole('" + PermissionConstants.COMPONENTS_REPORTING + "')")
  public Report generateComponentProductionReport(
      @RequestParam(value = "processingSite", required = false) UUID processingSiteId,
      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date startDate,
      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date endDate) {
    return componentProductionReportGenerator.generateComponentProductionReport(processingSiteId, startDate, endDate);
  }
}
