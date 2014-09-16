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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import repository.CollectedSampleRepository;
import repository.LocationRepository;
import repository.ProductRepository;
import repository.RequestRepository;
import repository.bloodtesting.BloodTestingRepository;
import utils.CustomDateFormatter;
import utils.PermissionConstants;
import backingform.CollectionsReportBackingForm;
import backingform.DiscardedProductsReportBackingForm;
import backingform.IssuedProductsReportBackingForm;
import backingform.RequestsReportBackingForm;

@Controller
@RequestMapping
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
  
  @RequestMapping(value = "/inventoryReportFormGenerator", method = RequestMethod.GET)
  @PreAuthorize("hasRole('"+PermissionConstants.VIEW_REPORTING_INFORMATION+"')")
  public ModelAndView inventoryReportFormGenerator(Model model) {
    ModelAndView mv = new ModelAndView("reports/inventoryReportForm");
    utilController.addTipsToModel(model.asMap(), "report.inventory.generate");
    utilController.addTipsToModel(model.asMap(), "report.inventory.productinventorychart");
    Map<String, Object> m = model.asMap();
    m.put("centers", locationRepository.getAllCenters());
    mv.addObject("model", m);
    return mv;
  }

  @RequestMapping(value="/generateInventoryReport", method=RequestMethod.GET)
  @PreAuthorize("hasRole('"+PermissionConstants.VIEW_REPORTING_INFORMATION+"')")
  public @ResponseBody Map<String, Object> generateInventoryReport(
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
  
  @RequestMapping(value = "/collectionsReportFormGenerator", method = RequestMethod.GET)
  @PreAuthorize("hasRole('"+PermissionConstants.DONATIONS_REPORTING+"')")
  public ModelAndView collectionsReportFormGenerator(Model model) {
    ModelAndView mv = new ModelAndView("reports/collectionsReport");
    Map<String, Object> m = model.asMap();
    utilController.addTipsToModel(model.asMap(), "report.collections.collectionsreport");
    m.put("centers", locationRepository.getAllCenters());
    m.put("sites", locationRepository.getAllCollectionSites());
    mv.addObject("collectionsReportForm", new CollectionsReportBackingForm());
    mv.addObject("model", model);
    return mv;
  }

  @RequestMapping(value = "/requestsReportFormGenerator", method = RequestMethod.GET)
  @PreAuthorize("hasRole('"+PermissionConstants.REQUESTS_REPORTING+"')")
  public ModelAndView requestsReportFormGenerator(Model model) {
    ModelAndView mv = new ModelAndView("reports/requestsReport");
    Map<String, Object> m = model.asMap();
    utilController.addTipsToModel(model.asMap(), "report.requests.requestsreport");
    m.put("sites", locationRepository.getAllUsageSites());
    mv.addObject("requestsReportForm", new RequestsReportBackingForm());
    mv.addObject("model", model);
    return mv;
  }

  @RequestMapping(value = "/discardedProductsReportFormGenerator", method = RequestMethod.GET)
  @PreAuthorize("hasRole('"+PermissionConstants.COMPONENTS_DISCARDED_REPORTING+"')")
  public ModelAndView discardedProductsReportFormGenerator(Model model) {
    ModelAndView mv = new ModelAndView("reports/discardedProductsReport");
    Map<String, Object> m = model.asMap();
    utilController.addTipsToModel(m, "report.products.discardedproductsreport");
    m.put("centers", locationRepository.getAllCenters());
    m.put("sites", locationRepository.getAllCollectionSites());
    mv.addObject("discardedProductsReportForm", new DiscardedProductsReportBackingForm());
    mv.addObject("model", m);
    return mv;
  }

  @RequestMapping(value = "/issuedProductsReportFormGenerator", method = RequestMethod.GET)
  @PreAuthorize("hasRole('"+PermissionConstants.COMPONENTS_ISSUED_REPORTING+"')")
  public ModelAndView issuedProductsReportFormGenerator(Model model) {
    ModelAndView mv = new ModelAndView("reports/issuedProductsReport");
    Map<String, Object> m = model.asMap();
    utilController.addTipsToModel(m, "report.products.issuedproductsreport");
    m.put("centers", locationRepository.getAllCenters());
    m.put("sites", locationRepository.getAllCollectionSites());
    mv.addObject("issuedProductsReportForm", new IssuedProductsReportBackingForm());
    mv.addObject("model", m);
    return mv;
  }

  @RequestMapping("/getCollectionsReport")
  @PreAuthorize("hasRole('"+PermissionConstants.DONATIONS_REPORTING+"')")
  public @ResponseBody
  Map<String, Object> getCollectionsReport(
      @ModelAttribute("collectionsReportForm") CollectionsReportBackingForm form,
      BindingResult result, Model model) {

    String dateCollectedFrom = form.getDateCollectedFrom();
    String dateCollectedTo = form.getDateCollectedTo();

    Map<String, Object> m = new HashMap<String, Object>();

    try {

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
              form.getAggregationCriteria(), form.getCenters(), form.getSites(), form.getBloodGroups());
      // TODO: potential leap year bug here
      Long interval = (long) (24 * 3600 * 1000);
  
      if (form.getAggregationCriteria().equals("monthly"))
        interval = interval * 30;
      else if (form.getAggregationCriteria().equals("yearly"))
        interval = interval * 365;
  
      m.put("interval", interval);
      m.put("numCollections", numCollections);

      m.put("dateCollectedFromUTC", dateFrom.getTime());
      m.put("dateCollectedToUTC", dateTo.getTime());

    } catch (ParseException ex) {
      // TODO Auto-generated catch block
      ex.printStackTrace();
    }
    return m;
  }

  @RequestMapping("/getRequestsReport")
  @PreAuthorize("hasRole('"+PermissionConstants.REQUESTS_REPORTING+"')")
  public @ResponseBody
  Map<String, Object> getRequestsReport(
      @ModelAttribute("requestsReportForm") RequestsReportBackingForm form,
      BindingResult result, Model model) {

    String dateCollectedFrom = form.getDateRequestedFrom();
    String dateCollectedTo = form.getDateRequestedTo();

    Map<String, Object> m = new HashMap<String, Object>();

    try {

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
              form.getAggregationCriteria(), form.getSites(), form.getBloodGroups());
      // TODO: potential leap year bug here
      Long interval = (long) (24 * 3600 * 1000);
  
      if (form.getAggregationCriteria().equals("monthly"))
        interval = interval * 30;
      else if (form.getAggregationCriteria().equals("yearly"))
        interval = interval * 365;
  
      m.put("interval", interval);
      m.put("numRequests", numRequests);

      m.put("dateRequestedFromUTC", dateFrom.getTime());
      m.put("dateRequestedToUTC", dateTo.getTime());

    } catch (ParseException ex) {
      // TODO Auto-generated catch block
      ex.printStackTrace();
    }
    return m;
  }

  @RequestMapping("/getDiscardedProductsReport")
  @PreAuthorize("hasRole('"+PermissionConstants.COMPONENTS_DISCARDED_REPORTING+"')")
  public @ResponseBody
  Map<String, Object> getDiscardedProductsReport(
      @ModelAttribute("discardedProductsReportForm") DiscardedProductsReportBackingForm form,
      BindingResult result, Model model) {

    String dateCollectedFrom = form.getDateCollectedFrom();
    String dateCollectedTo = form.getDateCollectedTo();

    Map<String, Object> m = new HashMap<String, Object>();

    try {

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
              form.getAggregationCriteria(), form.getCenters(), form.getSites(), form.getBloodGroups());
      // TODO: potential leap year bug here
      Long interval = (long) (24 * 3600 * 1000);
  
      if (form.getAggregationCriteria().equals("monthly"))
        interval = interval * 30;
      else if (form.getAggregationCriteria().equals("yearly"))
        interval = interval * 365;
  
      m.put("interval", interval);
      m.put("numDiscardedProducts", numDiscardedProducts);

      m.put("dateCollectedFromUTC", dateFrom.getTime());
      m.put("dateCollectedToUTC", dateTo.getTime());

    } catch (ParseException ex) {
      // TODO Auto-generated catch block
      ex.printStackTrace();
    }
    return m;
  }

  @RequestMapping("/getIssuedProductsReport")
  @PreAuthorize("hasRole('"+PermissionConstants.COMPONENTS_ISSUED_REPORTING+"')")
  public @ResponseBody
  Map<String, Object> getIssuedProductsReport(
      @ModelAttribute("issuedProductsReportForm") IssuedProductsReportBackingForm form,
      BindingResult result, Model model) {

    String dateCollectedFrom = form.getDateIssuedFrom();
    String dateCollectedTo = form.getDateIssuedTo();

    Map<String, Object> m = new HashMap<String, Object>();

    try {

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
              form.getAggregationCriteria(), form.getCenters(), form.getSites(), form.getBloodGroups());
      // TODO: potential leap year bug here
      Long interval = (long) (24 * 3600 * 1000);
  
      if (form.getAggregationCriteria().equals("monthly"))
        interval = interval * 30;
      else if (form.getAggregationCriteria().equals("yearly"))
        interval = interval * 365;
  
      m.put("interval", interval);
      m.put("numIssuedProducts", numIssuedProducts);

      m.put("dateIssuedFromUTC", dateFrom.getTime());
      m.put("dateIssuedToUTC", dateTo.getTime());

    } catch (ParseException ex) {
      // TODO Auto-generated catch block
      ex.printStackTrace();
    }
    return m;
  }

  private Date dateSubtract(Date dateTo, int field, int amount) {
    Calendar gcal = new GregorianCalendar();
    gcal.setTime(dateTo);
    gcal.add(field, -amount);
    return gcal.getTime();
  }

  public static class TestResultsReportBackingForm {

    private List<String> ttiTests;
    private String dateTestedFrom;
    private String dateTestedTo;
    private String aggregationCriteria;
    private List<String> centers;
    private List<String> sites;
    
    public String getDateTestedFrom() {
      return dateTestedFrom;
    }
    public String getDateTestedTo() {
      return dateTestedTo;
    }
    public String getAggregationCriteria() {
      return aggregationCriteria;
    }
    public List<String> getCenters() {
      return centers;
    }
    public List<String> getSites() {
      return sites;
    }
    public void setDateTestedFrom(String dateTestedFrom) {
      this.dateTestedFrom = dateTestedFrom;
    }
    public void setDateTestedTo(String dateTestedTo) {
      this.dateTestedTo = dateTestedTo;
    }
    public void setAggregationCriteria(String aggregationCriteria) {
      this.aggregationCriteria = aggregationCriteria;
    }
    public void setCenters(List<String> centers) {
      this.centers = centers;
    }
    public void setSites(List<String> sites) {
      this.sites = sites;
    }
    public List<String> getTtiTests() {
      return ttiTests;
    }
    public void setTtiTests(List<String> ttiTests) {
      this.ttiTests = ttiTests;
    }

  }

  @RequestMapping(value = "/ttiReportFormGenerator", method = RequestMethod.GET)
  @PreAuthorize("hasRole('"+PermissionConstants.TTI_REPORTING+"')")
  public ModelAndView testResultsReportFormGenerator(Model model) {
    ModelAndView mv = new ModelAndView("reports/testResultsReport");
    Map<String, Object> m = model.asMap();
    m.put("ttiTests", bloodTestingRepository.getTTITests());
    m.put("centers", locationRepository.getAllCenters());
    m.put("sites", locationRepository.getAllCollectionSites());
    utilController.addTipsToModel(m, "report.collections.testresultsreport");
    mv.addObject("testResultsReportForm", new TestResultsReportBackingForm());
    mv.addObject("model", model);
    return mv;
  }

  @RequestMapping("/getTestResultsReport")
  @PreAuthorize("hasRole('"+PermissionConstants.TTI_REPORTING+"')")
  public @ResponseBody
  Map<String, Object> getTestResultsReport(
      @ModelAttribute("testResultsReportForm") TestResultsReportBackingForm form,
      BindingResult result, Model model) {

    List<String> ttiTests = form.getTtiTests();
    String dateTestedFrom = form.getDateTestedFrom();
    String dateTestedTo = form.getDateTestedTo();

    Map<String, Object> m = new HashMap<String, Object>();

    try {

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
              form.getAggregationCriteria(), form.getCenters(), form.getSites());
  
      // TODO: potential leap year bug here
      Long interval = (long) (24 * 3600 * 1000);
  
      if (form.getAggregationCriteria().equals("monthly"))
        interval = interval * 30;
      else if (form.getAggregationCriteria().equals("yearly"))
        interval = interval * 365;
  
      m.put("interval", interval);
      m.put("numTestResults", numTestResults);
      m.put("dateTestedFromUTC", dateFrom.getTime());
      m.put("dateTestedToUTC", dateTo.getTime());
    } catch (ParseException e) {
      e.printStackTrace();
    }

    return m;
  }

}
