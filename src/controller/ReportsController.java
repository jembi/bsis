package controller;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.inventory.InventoryStatus;
import model.location.Location;
import model.location.LocationType;
import model.reporting.Report;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import repository.ComponentRepository;
import repository.DonationRepository;
import repository.LocationRepository;
import repository.RequestRepository;
import repository.TipsRepository;
import repository.bloodtesting.BloodTestingRepository;
import service.ReportGeneratorService;
import utils.CustomDateFormatter;
import utils.PermissionConstants;
import factory.LocationViewModelFactory;

@RestController
@RequestMapping("reports")
public class ReportsController {

  @Autowired
  private DonationRepository donationRepository;

  @Autowired
  private ComponentRepository componentRepository;

  @Autowired
  private LocationRepository locationRepository;

  @Autowired
  private RequestRepository requestRepository;

  @Autowired
  private BloodTestingRepository bloodTestingRepository;

  @Autowired
  private ReportGeneratorService reportGeneratorService;

  @Autowired
  private TipsRepository tipsRepository;
  
  @Autowired
  private LocationViewModelFactory locationViewModelFactory;
  
  @RequestMapping(value = "/stockLevels/generate", method = RequestMethod.GET)
  @PreAuthorize("hasRole('" + PermissionConstants.VIEW_INVENTORY_INFORMATION + "')")
  public Report findStockLevels(@RequestParam(value = "location", required = false) Long locationId,
      @RequestParam(value = "inventoryStatus", required = true) InventoryStatus inventoryStatus) {

    return reportGeneratorService.generateStockLevelsForLocationReport(locationId, inventoryStatus);
  }
  
  @RequestMapping(value = "/stockLevels/form", method = RequestMethod.GET)
  @PreAuthorize("hasRole('" + PermissionConstants.VIEW_INVENTORY_INFORMATION + "')")
  public Map<String, Object> stockLevelsFormGenerator() {
    List<Location> distributionSites = locationRepository.getDistributionSites();

    Map<String, Object> map = new HashMap<>();
    map.put("distributionSites", locationViewModelFactory.createLocationViewModels(distributionSites));
    return map;
  }

  @RequestMapping(value = "/donations/form", method = RequestMethod.GET)
  @PreAuthorize("hasRole('" + PermissionConstants.DONATIONS_REPORTING + "')")
  public Map<String, Object> donationsReportFormGenerator() {
    Map<String, Object> map = new HashMap<String, Object>();
    map.put("report.donations.donationsreport", tipsRepository.getTipsContent("report.donations.donationsreport"));
    map.put("venues", locationRepository.getLocationsByType(LocationType.VENUE));
    return map;
  }

  @RequestMapping(value = "/requests/form", method = RequestMethod.GET)
  @PreAuthorize("hasRole('" + PermissionConstants.REQUESTS_REPORTING + "')")
  public Map<String, Object> requestsReportFormGenerator() {
    Map<String, Object> map = new HashMap<String, Object>();
    map.put("report.requests.requestsreport", tipsRepository.getTipsContent("report.requests.requestsreport"));
    map.put("sites", locationRepository.getLocationsByType(LocationType.USAGE_SITE));
    return map;
  }

  @RequestMapping(value = "/components/discard/form", method = RequestMethod.GET)
  @PreAuthorize("hasRole('" + PermissionConstants.COMPONENTS_DISCARDED_REPORTING + "')")
  public Map<String, Object> discardedComponentsReportFormGenerator() {
    Map<String, Object> map = new HashMap<String, Object>();
    map.put("report.components.discardedcomponentsreport", tipsRepository.getTipsContent("report.components.discardedcomponentsreport"));
    map.put("venues", locationRepository.getLocationsByType(LocationType.VENUE));
    map.put("model", map);
    return map;
  }

  @RequestMapping(value = "/components/issued/form", method = RequestMethod.GET)
  @PreAuthorize("hasRole('" + PermissionConstants.COMPONENTS_ISSUED_REPORTING + "')")
  public Map<String, Object> issuedComponentsReportFormGenerator() {
    Map<String, Object> map = new HashMap<String, Object>();
    map.put("report.components.issuedcomponentsreport", tipsRepository.getTipsContent("report.components.issuedcomponentsreport"));
    map.put("venues", locationRepository.getLocationsByType(LocationType.VENUE));
    return map;
  }

  @RequestMapping(value = "/donations/generate", method = RequestMethod.GET)
  @PreAuthorize("hasRole('" + PermissionConstants.DONATIONS_REPORTING + "')")
  public ResponseEntity<Map<String, Object>> getDonationsReport(
      @RequestParam(value = "donationDateFrom", required = false) String donationDateFrom,
      @RequestParam(value = "donationDateTo", required = false) String donationDateTo,
      @RequestParam(value = "aggregationCriteria", required = false) String aggregationCriteria,
      @RequestParam(value = "venues", required = false) List<String> venues,
      @RequestParam(value = "bloodGroups", required = false) List<String> bloodGroups) throws ParseException {


    HttpStatus httpStatus = HttpStatus.OK;
    Map<String, Object> map = new HashMap<String, Object>();


    Date dateTo;
    if (donationDateTo == null || donationDateTo.equals(""))
      dateTo = new Date();
    else
      dateTo = CustomDateFormatter.getDateFromString(donationDateTo);

    Calendar gcal = new GregorianCalendar();
    gcal.setTime(dateTo);
    gcal.add(Calendar.DATE, 1);
    dateTo = CustomDateFormatter.getDateFromString(CustomDateFormatter.getDateString(gcal.getTime()));

    Date dateFrom;
    if (donationDateFrom == null || donationDateFrom.equals(""))
      dateFrom = dateSubtract(dateTo, Calendar.MONTH, 1);
    else
      dateFrom = CustomDateFormatter.getDateFromString(donationDateFrom);

    Map<String, Map<Long, Long>> numDonations = donationRepository
        .findNumberOfDonations(dateFrom, dateTo,
            aggregationCriteria, venues, bloodGroups);
    // TODO: potential leap year bug here
    Long interval = (long) (24 * 3600 * 1000);

    if (aggregationCriteria.equals("monthly"))
      interval = interval * 30;
    else if (aggregationCriteria.equals("yearly"))
      interval = interval * 365;

    map.put("interval", interval);
    map.put("numDonations", numDonations);

    map.put("donationDateFromUTC", dateFrom.getTime());
    map.put("donationDateToUTC", dateTo.getTime());

    return new ResponseEntity<Map<String, Object>>(map, httpStatus);
  }

  @RequestMapping(value = "/requests/generate", method = RequestMethod.GET)
  @PreAuthorize("hasRole('" + PermissionConstants.REQUESTS_REPORTING + "')")
  public ResponseEntity<Map<String, Object>> getRequestsReport(
      @RequestParam(value = "donationDateFrom", required = false) String donationDateFrom,
      @RequestParam(value = "donationDateTo", required = false) String donationDateTo,
      @RequestParam(value = "aggregationCriteria", required = false) String aggregationCriteria,
      @RequestParam(value = "venues", required = false) List<String> venues,
      @RequestParam(value = "bloodGroups", required = false) List<String> bloodGroups) throws ParseException {

    HttpStatus httpStatus = HttpStatus.OK;
    Map<String, Object> map = new HashMap<String, Object>();

    Date dateTo;
    if (donationDateTo == null || donationDateTo.equals(""))
      dateTo = new Date();
    else
      dateTo = CustomDateFormatter.getDateFromString(donationDateTo);

    Calendar gcal = new GregorianCalendar();
    gcal.setTime(dateTo);
    gcal.add(Calendar.DATE, 1);
    dateTo = CustomDateFormatter.getDateFromString(CustomDateFormatter.getDateString(gcal.getTime()));

    Date dateFrom;
    if (donationDateFrom == null || donationDateFrom.equals(""))
      dateFrom = dateSubtract(dateTo, Calendar.MONTH, 1);
    else
      dateFrom = CustomDateFormatter.getDateFromString(donationDateFrom);

    Map<String, Map<Long, Long>> numRequests = requestRepository
        .findNumberOfRequests(dateFrom, dateTo,
            aggregationCriteria, venues, bloodGroups);
    // TODO: potential leap year bug here
    Long interval = (long) (24 * 3600 * 1000);

    if (aggregationCriteria.equals("monthly"))
      interval = interval * 30;
    else if (aggregationCriteria.equals("yearly"))
      interval = interval * 365;

    map.put("interval", interval);
    map.put("numRequests", numRequests);

    map.put("dateRequestedFromUTC", dateFrom.getTime());
    map.put("dateRequestedToUTC", dateTo.getTime());

    return new ResponseEntity<Map<String, Object>>(map, httpStatus);
  }

  @RequestMapping(value = "/components/discard/generate", method = RequestMethod.GET)
  @PreAuthorize("hasRole('" + PermissionConstants.COMPONENTS_DISCARDED_REPORTING + "')")
  public ResponseEntity<Map<String, Object>> getDiscardedComponentsReport(
      @RequestParam(value = "donationDateFrom", required = false) String donationDateFrom,
      @RequestParam(value = "donationDateTo", required = false) String donationDateTo,
      @RequestParam(value = "aggregationCriteria", required = false) String aggregationCriteria,
      @RequestParam(value = "venues", required = false) List<String> venues,
      @RequestParam(value = "bloodGroups", required = false) List<String> bloodGroups) throws ParseException {

    HttpStatus httpStatus = HttpStatus.OK;
    Map<String, Object> map = new HashMap<String, Object>();


    Date dateTo;
    if (donationDateTo == null || donationDateTo.equals(""))
      dateTo = new Date();
    else
      dateTo = CustomDateFormatter.getDateFromString(donationDateTo);

    Calendar gcal = new GregorianCalendar();
    gcal.setTime(dateTo);
    gcal.add(Calendar.DATE, 1);
    dateTo = CustomDateFormatter.getDateFromString(CustomDateFormatter.getDateString(gcal.getTime()));

    Date dateFrom;
    if (donationDateFrom == null || donationDateFrom.equals(""))
      dateFrom = dateSubtract(dateTo, Calendar.MONTH, 1);
    else
      dateFrom = CustomDateFormatter.getDateFromString(donationDateFrom);

    Map<String, Map<Long, Long>> numDiscardedComponents = componentRepository
        .findNumberOfDiscardedComponents(dateFrom, dateTo,
            aggregationCriteria, venues, bloodGroups);
    // TODO: potential leap year bug here
    Long interval = (long) (24 * 3600 * 1000);

    if (aggregationCriteria.equals("monthly"))
      interval = interval * 30;
    else if (aggregationCriteria.equals("yearly"))
      interval = interval * 365;

    map.put("interval", interval);
    map.put("numDiscardedComponents", numDiscardedComponents);

    map.put("donationDateFromUTC", dateFrom.getTime());
    map.put("donationDateToUTC", dateTo.getTime());

    return new ResponseEntity<Map<String, Object>>(map, httpStatus);
  }

  @RequestMapping(value = "/components/issued/generate", method = RequestMethod.GET)
  @PreAuthorize("hasRole('" + PermissionConstants.COMPONENTS_ISSUED_REPORTING + "')")
  public ResponseEntity<Map<String, Object>> getIssuedComponentsReport(
      @RequestParam(value = "dateIssuedFrom", required = false) String donationDateFrom,
      @RequestParam(value = "dateIssuedTo", required = false) String donationDateTo,
      @RequestParam(value = "aggregationCriteria", required = false) String aggregationCriteria,
      @RequestParam(value = "venues", required = false) List<String> venues,
      @RequestParam(value = "bloodGroups", required = false) List<String> bloodGroups) throws ParseException {

    HttpStatus httpStatus = HttpStatus.OK;
    Map<String, Object> map = new HashMap<String, Object>();

    Date dateTo;
    if (donationDateTo == null || donationDateTo.equals(""))
      dateTo = new Date();
    else
      dateTo = CustomDateFormatter.getDateFromString(donationDateTo);

    Calendar gcal = new GregorianCalendar();
    gcal.setTime(dateTo);
    gcal.add(Calendar.DATE, 1);
    dateTo = CustomDateFormatter.getDateFromString(CustomDateFormatter.getDateString(gcal.getTime()));

    Date dateFrom;
    if (donationDateFrom == null || donationDateFrom.equals(""))
      dateFrom = dateSubtract(dateTo, Calendar.MONTH, 1);
    else
      dateFrom = CustomDateFormatter.getDateFromString(donationDateFrom);

    Map<String, Map<Long, Long>> numIssuedComponents = componentRepository
        .findNumberOfIssuedComponents(dateFrom, dateTo,
            aggregationCriteria, venues, bloodGroups);
    // TODO: potential leap year bug here
    Long interval = (long) (24 * 3600 * 1000);

    if (aggregationCriteria.equals("monthly"))
      interval = interval * 30;
    else if (aggregationCriteria.equals("yearly"))
      interval = interval * 365;

    map.put("interval", interval);
    map.put("numIssuedComponents", numIssuedComponents);

    map.put("dateIssuedFromUTC", dateFrom.getTime());
    map.put("dateIssuedToUTC", dateTo.getTime());

    return new ResponseEntity<Map<String, Object>>(map, httpStatus);
  }

  private Date dateSubtract(Date dateTo, int field, int amount) {
    Calendar gcal = new GregorianCalendar();
    gcal.setTime(dateTo);
    gcal.add(field, -amount);
    return gcal.getTime();
  }


  @RequestMapping(value = "/tti/form", method = RequestMethod.GET)
  @PreAuthorize("hasRole('" + PermissionConstants.TTI_REPORTING + "')")
  public Map<String, Object> testResultsReportFormGenerator() {
    Map<String, Object> map = new HashMap<String, Object>();
    map.put("ttiTests", bloodTestingRepository.getTTITests());
    map.put("venues", locationRepository.getLocationsByType(LocationType.VENUE));
    map.put("report.donations.testresultsreport", tipsRepository.getTipsContent("report.donations.testresultsreport"));
    return map;
  }

  @RequestMapping(value = "/testresult/generate", method = RequestMethod.GET)
  @PreAuthorize("hasRole('" + PermissionConstants.TTI_REPORTING + "')")
  public ResponseEntity<Map<String, Object>> getTestResultsReport(
      @RequestParam(value = "dateTestedFrom", required = false) String dateTestedFrom,
      @RequestParam(value = "dateTestedTo", required = false) String dateTestedTo,
      @RequestParam(value = "aggregationCriteria", required = false) String aggregationCriteria,
      @RequestParam(value = "venues", required = false) List<String> venues,
      @RequestParam(value = "ttiTests", required = false) List<String> ttiTests) throws ParseException {


    HttpStatus httpStatus = HttpStatus.OK;
    Map<String, Object> map = new HashMap<String, Object>();

    Date dateTo;
    if (dateTestedTo == null || dateTestedTo.equals(""))
      dateTo = new Date();
    else
      dateTo = CustomDateFormatter.getDateFromString(dateTestedTo);
    Calendar gcal = new GregorianCalendar();
    gcal.setTime(dateTo);
    gcal.add(Calendar.DATE, 1);
    dateTo = CustomDateFormatter.getDateFromString(CustomDateFormatter.getDateString(gcal.getTime()));

    Date dateFrom;
    if (dateTestedFrom == null || dateTestedFrom.equals(""))
      dateFrom = dateSubtract(dateTo, Calendar.MONTH, 1);
    else
      dateFrom = CustomDateFormatter.getDateFromString(dateTestedFrom);

    Map<String, Map<Long, Long>> numTestResults = bloodTestingRepository
        .findNumberOfPositiveTests(ttiTests, dateFrom, dateTo,
            aggregationCriteria, venues);

    // TODO: potential leap year bug here
    Long interval = (long) (24 * 3600 * 1000);

    if (aggregationCriteria.equals("monthly"))
      interval = interval * 30;
    else if (aggregationCriteria.equals("yearly"))
      interval = interval * 365;

    map.put("interval", interval);
    map.put("numTestResults", numTestResults);
    map.put("dateTestedFromUTC", dateFrom.getTime());
    map.put("dateTestedToUTC", dateTo.getTime());

    return new ResponseEntity<Map<String, Object>>(map, httpStatus);
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
    return reportGeneratorService.generateTTIPrevalenceReport(startDate, endDate);
  }

}
