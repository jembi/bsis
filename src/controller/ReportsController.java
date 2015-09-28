package controller;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
import repository.DonationRepository;
import repository.LocationRepository;
import repository.ComponentRepository;
import repository.RequestRepository;
import repository.bloodtesting.BloodTestingRepository;
import service.ReportGeneratorService;
import utils.CustomDateFormatter;
import utils.PermissionConstants;

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
  private UtilController utilController;

  @Autowired
  private BloodTestingRepository bloodTestingRepository;
  
  @Autowired
  private ReportGeneratorService reportGeneratorService;
  
  @RequestMapping(value = "/inventory/form", method = RequestMethod.GET)
  @PreAuthorize("hasRole('"+PermissionConstants.VIEW_REPORTING_INFORMATION+"')")
  public Map<String, Object> inventoryReportFormGenerator() {
    Map<String, Object> map = new HashMap<String, Object>();
    utilController.addTipsToModel(map, "report.inventory.generate");
    utilController.addTipsToModel(map, "report.inventory.componentinventorychart");
    map.put("panels", locationRepository.getAllDonorPanels());
    map.put("model", map);
    return map;
  }

  @RequestMapping(value="/inventory/generate", method=RequestMethod.GET)
  @PreAuthorize("hasRole('"+PermissionConstants.VIEW_REPORTING_INFORMATION+"')")
  public  Map<String, Object> generateInventoryReport(
                  HttpServletRequest request, HttpServletResponse response,
                  @RequestParam(value="status") String status,
                  @RequestParam(value="panels") String panels
                  ) {

    List<String> componentStatuses = Arrays.asList(status.split("\\|"));
    List<String> centerIds = Arrays.asList(panels.split("\\|"));

    List<Long> centerIdsLong = new ArrayList<Long>();
    centerIdsLong.add((long)-1);
    for (String centerId : centerIds) {
      if (centerId.trim().equals(""))
        continue;
      centerIdsLong.add(Long.parseLong(centerId));
    }

    Map<String, Object> data = null;

    try {
      data = componentRepository.generateInventorySummaryFast(componentStatuses, centerIdsLong);
    } catch (Exception ex) {
      ex.printStackTrace();
      response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }
    return data;
  }
  
  @RequestMapping(value = "/donations/form", method = RequestMethod.GET)
  @PreAuthorize("hasRole('"+PermissionConstants.DONATIONS_REPORTING+"')")
  public Map<String, Object> donationsReportFormGenerator() {
    Map<String, Object> map = new HashMap<String, Object>();
    utilController.addTipsToModel(map, "report.donations.donationsreport");
    map.put("panels", locationRepository.getAllDonorPanels());
    return map;
  }

  @RequestMapping(value = "/requests/form", method = RequestMethod.GET)
  @PreAuthorize("hasRole('"+PermissionConstants.REQUESTS_REPORTING+"')")
  public Map<String, Object> requestsReportFormGenerator() {
    Map<String, Object> map = new HashMap<String, Object>();
    utilController.addTipsToModel(map, "report.requests.requestsreport");
    map.put("sites", locationRepository.getAllUsageSites());
    return map;
  }

  @RequestMapping(value = "/components/discard/form", method = RequestMethod.GET)
  @PreAuthorize("hasRole('"+PermissionConstants.COMPONENTS_DISCARDED_REPORTING+"')")
  public Map<String, Object> discardedComponentsReportFormGenerator() {
    Map<String, Object> map = new HashMap<String, Object>();
    utilController.addTipsToModel(map, "report.components.discardedcomponentsreport");
    map.put("panels", locationRepository.getAllDonorPanels());
    map.put("model", map);
    return map;
  }

  @RequestMapping(value = "/components/issued/form", method = RequestMethod.GET)
  @PreAuthorize("hasRole('"+PermissionConstants.COMPONENTS_ISSUED_REPORTING+"')")
  public Map<String, Object> issuedComponentsReportFormGenerator() {
    Map<String, Object> map = new HashMap<String, Object>();
    utilController.addTipsToModel(map, "report.components.issuedcomponentsreport");
    map.put("panels", locationRepository.getAllDonorPanels());
    return map;
  }

  @RequestMapping(value = "/donations/generate", method = RequestMethod.GET)
  @PreAuthorize("hasRole('"+PermissionConstants.DONATIONS_REPORTING+"')")
  public 
  ResponseEntity<Map<String, Object>> getDonationsReport(
          @RequestParam(value = "donationDateFrom", required = false) String donationDateFrom,
          @RequestParam(value = "donationDateTo", required = false) String donationDateTo,
          @RequestParam(value = "aggregationCriteria", required = false) String aggregationCriteria,
          @RequestParam(value = "panels", required = false) List<String> panels,
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
              aggregationCriteria, panels, bloodGroups);
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
  @PreAuthorize("hasRole('"+PermissionConstants.REQUESTS_REPORTING+"')")
  public 
  ResponseEntity<Map<String, Object>> getRequestsReport(
          @RequestParam(value = "donationDateFrom", required = false) String donationDateFrom,
          @RequestParam(value = "donationDateTo", required = false) String donationDateTo,
          @RequestParam(value = "aggregationCriteria", required = false) String aggregationCriteria,
          @RequestParam(value = "panels", required = false) List<String> panels,
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
              aggregationCriteria, panels, bloodGroups);
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
  @PreAuthorize("hasRole('"+PermissionConstants.COMPONENTS_DISCARDED_REPORTING+"')")
  public 
  ResponseEntity<Map<String, Object>> getDiscardedComponentsReport(
          @RequestParam(value = "donationDateFrom", required = false) String donationDateFrom,
          @RequestParam(value = "donationDateTo", required = false) String donationDateTo,
          @RequestParam(value = "aggregationCriteria", required = false) String aggregationCriteria,
          @RequestParam(value = "panels", required = false) List<String> panels,
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
              aggregationCriteria, panels, bloodGroups);
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
  @PreAuthorize("hasRole('"+PermissionConstants.COMPONENTS_ISSUED_REPORTING+"')")
  public 
  ResponseEntity<Map<String, Object>> getIssuedComponentsReport(
          @RequestParam(value = "dateIssuedFrom", required = false) String donationDateFrom,
          @RequestParam(value = "dateIssuedTo", required = false) String donationDateTo,
          @RequestParam(value = "aggregationCriteria", required = false) String aggregationCriteria,
          @RequestParam(value = "panels", required = false) List<String> panels,
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
              aggregationCriteria, panels, bloodGroups);
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
  @PreAuthorize("hasRole('"+PermissionConstants.TTI_REPORTING+"')")
  public Map<String, Object> testResultsReportFormGenerator() {
    Map<String, Object> map = new HashMap<String, Object>();
    map.put("ttiTests", bloodTestingRepository.getTTITests());
    map.put("panels", locationRepository.getAllDonorPanels());
    utilController.addTipsToModel(map, "report.donations.testresultsreport");
    return map;
  }

  @RequestMapping(value = "/testresult/generate", method = RequestMethod.GET)
  @PreAuthorize("hasRole('"+PermissionConstants.TTI_REPORTING+"')")
  public 
   ResponseEntity<Map<String, Object>> getTestResultsReport(
          @RequestParam(value = "dateTestedFrom", required = false) String dateTestedFrom,
          @RequestParam(value = "dateTestedTo", required = false) String dateTestedTo,
          @RequestParam(value = "aggregationCriteria", required = false) String aggregationCriteria,
          @RequestParam(value = "panels", required = false) List<String> panels,
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
              aggregationCriteria, panels);
  
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

}
