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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import repository.CollectedSampleRepository;
import repository.LocationRepository;
import repository.ProductRepository;
import repository.RequestRepository;
import repository.bloodtesting.BloodTestingRepository;
import utils.CustomDateFormatter;
import utils.PermissionConstants;

@RestController
@RequestMapping("reports")
public class ReportsController {

  @Autowired
  private CollectedSampleRepository collectionRepository;

  @Autowired
  private ProductRepository productRepository;

  @Autowired
  private LocationRepository locationRepository;

  @Autowired
  private RequestRepository requestRepository;

  @Autowired
  private UtilController utilController;

  @Autowired
  private BloodTestingRepository bloodTestingRepository;
  
  @RequestMapping(value = "/inventory/form", method = RequestMethod.GET)
  @PreAuthorize("hasRole('"+PermissionConstants.VIEW_REPORTING_INFORMATION+"')")
  public Map<String, Object> inventoryReportFormGenerator() {
    Map<String, Object> map = new HashMap<String, Object>();
    utilController.addTipsToModel(map, "report.inventory.generate");
    utilController.addTipsToModel(map, "report.inventory.productinventorychart");
    map.put("centers", locationRepository.getAllCenters());
    map.put("model", map);
    return map;
  }

  @RequestMapping(value="/inventory/generate", method=RequestMethod.GET)
  @PreAuthorize("hasRole('"+PermissionConstants.VIEW_REPORTING_INFORMATION+"')")
  public  Map<String, Object> generateInventoryReport(
                  HttpServletRequest request, HttpServletResponse response,
                  @RequestParam(value="status") String status,
                  @RequestParam(value="centers") String centers
                  ) {

    List<String> productStatuses = Arrays.asList(status.split("\\|"));
    List<String> centerIds = Arrays.asList(centers.split("\\|"));

    List<Long> centerIdsLong = new ArrayList<Long>();
    centerIdsLong.add((long)-1);
    for (String centerId : centerIds) {
      if (centerId.trim().equals(""))
        continue;
      centerIdsLong.add(Long.parseLong(centerId));
    }

    Map<String, Object> data = null;

    try {
      data = productRepository.generateInventorySummaryFast(productStatuses, centerIdsLong);
    } catch (Exception ex) {
      ex.printStackTrace();
      response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }
    return data;
  }
  
  @RequestMapping(value = "/donations/form", method = RequestMethod.GET)
  @PreAuthorize("hasRole('"+PermissionConstants.DONATIONS_REPORTING+"')")
  public Map<String, Object> collectionsReportFormGenerator() {
    Map<String, Object> map = new HashMap<String, Object>();
    utilController.addTipsToModel(map, "report.collections.collectionsreport");
    map.put("centers", locationRepository.getAllCenters());
    map.put("sites", locationRepository.getAllCollectionSites());
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
  public Map<String, Object> discardedProductsReportFormGenerator() {
    Map<String, Object> map = new HashMap<String, Object>();
    utilController.addTipsToModel(map, "report.products.discardedproductsreport");
    map.put("centers", locationRepository.getAllCenters());
    map.put("sites", locationRepository.getAllCollectionSites());
    map.put("model", map);
    return map;
  }

  @RequestMapping(value = "/components/issued/form", method = RequestMethod.GET)
  @PreAuthorize("hasRole('"+PermissionConstants.COMPONENTS_ISSUED_REPORTING+"')")
  public Map<String, Object> issuedProductsReportFormGenerator() {
    Map<String, Object> map = new HashMap<String, Object>();
    utilController.addTipsToModel(map, "report.products.issuedproductsreport");
    map.put("centers", locationRepository.getAllCenters());
    map.put("sites", locationRepository.getAllCollectionSites());
    return map;
  }

  @RequestMapping(value = "/donations/generate", method = RequestMethod.GET)
  @PreAuthorize("hasRole('"+PermissionConstants.DONATIONS_REPORTING+"')")
  public 
  ResponseEntity<Map<String, Object>> getCollectionsReport(
          @RequestParam(value = "dateCollectedFrom", required = false) String dateCollectedFrom,
          @RequestParam(value = "dateCollectedTo", required = false) String dateCollectedTo,
          @RequestParam(value = "aggregationCriteria", required = false) String aggregationCriteria,
          @RequestParam(value = "centers", required = false) List<String> centers,
          @RequestParam(value = "sites", required = false) List<String> sites,
          @RequestParam(value = "bloodGroups", required = false) List<String> bloodGroups) throws ParseException {


      HttpStatus httpStatus = HttpStatus.OK;
      Map<String, Object> map = new HashMap<String, Object>();


      Date dateTo;
      if (dateCollectedTo == null || dateCollectedTo.equals(""))
        dateTo = new Date();
      else
        dateTo = CustomDateFormatter.getDateFromString(dateCollectedTo);

      Calendar gcal = new GregorianCalendar();
      gcal.setTime(dateTo);
      gcal.add(Calendar.DATE, 1);
      dateTo = CustomDateFormatter.getDateFromString(CustomDateFormatter.getDateString(gcal.getTime()));

      Date dateFrom;
      if (dateCollectedFrom == null || dateCollectedFrom.equals(""))
        dateFrom = dateSubtract(dateTo, Calendar.MONTH, 1);
      else
        dateFrom = CustomDateFormatter.getDateFromString(dateCollectedFrom);

      Map<String, Map<Long, Long>> numCollections = collectionRepository
          .findNumberOfCollectedSamples(dateFrom, dateTo,
              aggregationCriteria, centers, sites, bloodGroups);
      // TODO: potential leap year bug here
      Long interval = (long) (24 * 3600 * 1000);
  
      if (aggregationCriteria.equals("monthly"))
        interval = interval * 30;
      else if (aggregationCriteria.equals("yearly"))
        interval = interval * 365;
  
      map.put("interval", interval);
      map.put("numCollections", numCollections);

      map.put("dateCollectedFromUTC", dateFrom.getTime());
      map.put("dateCollectedToUTC", dateTo.getTime());

    return new ResponseEntity<Map<String, Object>>(map, httpStatus);
  }

  @RequestMapping(value = "/requests/generate", method = RequestMethod.GET)
  @PreAuthorize("hasRole('"+PermissionConstants.REQUESTS_REPORTING+"')")
  public 
  ResponseEntity<Map<String, Object>> getRequestsReport(
          @RequestParam(value = "dateCollectedFrom", required = false) String dateCollectedFrom,
          @RequestParam(value = "dateCollectedTo", required = false) String dateCollectedTo,
          @RequestParam(value = "aggregationCriteria", required = false) String aggregationCriteria,
          @RequestParam(value = "centers", required = false) List<String> centers,
          @RequestParam(value = "sites", required = false) List<String> sites,
          @RequestParam(value = "bloodGroups", required = false) List<String> bloodGroups) throws ParseException {

    HttpStatus httpStatus = HttpStatus.OK;
    Map<String, Object> map = new HashMap<String, Object>();

      Date dateTo;
      if (dateCollectedTo == null || dateCollectedTo.equals(""))
        dateTo = new Date();
      else
        dateTo = CustomDateFormatter.getDateFromString(dateCollectedTo);

      Calendar gcal = new GregorianCalendar();
      gcal.setTime(dateTo);
      gcal.add(Calendar.DATE, 1);
      dateTo = CustomDateFormatter.getDateFromString(CustomDateFormatter.getDateString(gcal.getTime()));

      Date dateFrom;
      if (dateCollectedFrom == null || dateCollectedFrom.equals(""))
        dateFrom = dateSubtract(dateTo, Calendar.MONTH, 1);
      else
        dateFrom = CustomDateFormatter.getDateFromString(dateCollectedFrom);

      Map<String, Map<Long, Long>> numRequests = requestRepository
          .findNumberOfRequests(dateFrom, dateTo,
              aggregationCriteria, sites, bloodGroups);
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
  ResponseEntity<Map<String, Object>> getDiscardedProductsReport(
          @RequestParam(value = "dateCollectedFrom", required = false) String dateCollectedFrom,
          @RequestParam(value = "dateCollectedTo", required = false) String dateCollectedTo,
          @RequestParam(value = "aggregationCriteria", required = false) String aggregationCriteria,
          @RequestParam(value = "centers", required = false) List<String> centers,
          @RequestParam(value = "sites", required = false) List<String> sites,
          @RequestParam(value = "bloodGroups", required = false) List<String> bloodGroups) throws ParseException {

    HttpStatus httpStatus = HttpStatus.OK;
    Map<String, Object> map = new HashMap<String, Object>();


      Date dateTo;
      if (dateCollectedTo == null || dateCollectedTo.equals(""))
        dateTo = new Date();
      else
        dateTo = CustomDateFormatter.getDateFromString(dateCollectedTo);

      Calendar gcal = new GregorianCalendar();
      gcal.setTime(dateTo);
      gcal.add(Calendar.DATE, 1);
      dateTo = CustomDateFormatter.getDateFromString(CustomDateFormatter.getDateString(gcal.getTime()));

      Date dateFrom;
      if (dateCollectedFrom == null || dateCollectedFrom.equals(""))
        dateFrom = dateSubtract(dateTo, Calendar.MONTH, 1);
      else
        dateFrom = CustomDateFormatter.getDateFromString(dateCollectedFrom);

      Map<String, Map<Long, Long>> numDiscardedProducts = productRepository
          .findNumberOfDiscardedProducts(dateFrom, dateTo,
              aggregationCriteria, centers, sites, bloodGroups);
      // TODO: potential leap year bug here
      Long interval = (long) (24 * 3600 * 1000);
  
      if (aggregationCriteria.equals("monthly"))
        interval = interval * 30;
      else if (aggregationCriteria.equals("yearly"))
        interval = interval * 365;
  
      map.put("interval", interval);
      map.put("numDiscardedProducts", numDiscardedProducts);

      map.put("dateCollectedFromUTC", dateFrom.getTime());
      map.put("dateCollectedToUTC", dateTo.getTime());

   return new ResponseEntity<Map<String, Object>>(map, httpStatus);
  }

  @RequestMapping(value = "/components/issued/generate", method = RequestMethod.GET)
  @PreAuthorize("hasRole('"+PermissionConstants.COMPONENTS_ISSUED_REPORTING+"')")
  public 
  ResponseEntity<Map<String, Object>> getIssuedProductsReport(
          @RequestParam(value = "dateIssuedFrom", required = false) String dateCollectedFrom,
          @RequestParam(value = "dateIssuedTo", required = false) String dateCollectedTo,
          @RequestParam(value = "aggregationCriteria", required = false) String aggregationCriteria,
          @RequestParam(value = "centers", required = false) List<String> centers,
          @RequestParam(value = "sites", required = false) List<String> sites,
          @RequestParam(value = "bloodGroups", required = false) List<String> bloodGroups) throws ParseException {

    HttpStatus httpStatus = HttpStatus.OK;
    Map<String, Object> map = new HashMap<String, Object>();

      Date dateTo;
      if (dateCollectedTo == null || dateCollectedTo.equals(""))
        dateTo = new Date();
      else
        dateTo = CustomDateFormatter.getDateFromString(dateCollectedTo);

      Calendar gcal = new GregorianCalendar();
      gcal.setTime(dateTo);
      gcal.add(Calendar.DATE, 1);
      dateTo = CustomDateFormatter.getDateFromString(CustomDateFormatter.getDateString(gcal.getTime()));

      Date dateFrom;
      if (dateCollectedFrom == null || dateCollectedFrom.equals(""))
        dateFrom = dateSubtract(dateTo, Calendar.MONTH, 1);
      else
        dateFrom = CustomDateFormatter.getDateFromString(dateCollectedFrom);

      Map<String, Map<Long, Long>> numIssuedProducts = productRepository
          .findNumberOfIssuedProducts(dateFrom, dateTo,
              aggregationCriteria, centers, sites, bloodGroups);
      // TODO: potential leap year bug here
      Long interval = (long) (24 * 3600 * 1000);
  
      if (aggregationCriteria.equals("monthly"))
        interval = interval * 30;
      else if (aggregationCriteria.equals("yearly"))
        interval = interval * 365;
  
      map.put("interval", interval);
      map.put("numIssuedProducts", numIssuedProducts);

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
    map.put("centers", locationRepository.getAllCenters());
    map.put("sites", locationRepository.getAllCollectionSites());
    utilController.addTipsToModel(map, "report.collections.testresultsreport");
    return map;
  }

  @RequestMapping(value = "/testresult/generate", method = RequestMethod.GET)
  @PreAuthorize("hasRole('"+PermissionConstants.TTI_REPORTING+"')")
  public 
   ResponseEntity<Map<String, Object>> getTestResultsReport(
          @RequestParam(value = "dateTestedFrom", required = false) String dateTestedFrom,
          @RequestParam(value = "dateTestedTo", required = false) String dateTestedTo,
          @RequestParam(value = "aggregationCriteria", required = false) String aggregationCriteria,
          @RequestParam(value = "centers", required = false) List<String> centers,
          @RequestParam(value = "sites", required = false) List<String> sites,
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
              aggregationCriteria, centers, sites);
  
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

}
