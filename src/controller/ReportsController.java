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

import model.CustomDateFormatter;

import org.springframework.beans.factory.annotation.Autowired;
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
import repository.TestResultRepository;

@Controller
public class ReportsController {

  @Autowired
  private CollectedSampleRepository collectionRepository;

  @Autowired
  private TestResultRepository testResultsRepository;

  @Autowired
  private ProductRepository productRepository;

  @Autowired
  private LocationRepository locationRepository;

  @Autowired
  private UtilController utilController;
  
  public static class CollectionsReportBackingForm {

    private String dateCollectedFrom;
    private String dateCollectedTo;
    private String aggregationCriteria;
    private List<String> centers;
    private List<String> sites;
    private List<String> bloodGroups;

    public CollectionsReportBackingForm() {
      centers = Arrays.asList(new String[0]);
      sites = Arrays.asList(new String[0]);
      setBloodGroups(Arrays.asList(new String[0]));
    }

    public String getDateCollectedFrom() {
      return dateCollectedFrom;
    }

    public void setDateCollectedFrom(String dateCollectedFrom) {
      this.dateCollectedFrom = dateCollectedFrom;
    }

    public String getDateCollectedTo() {
      return dateCollectedTo;
    }

    public void setDateCollectedTo(String dateCollectedTo) {
      this.dateCollectedTo = dateCollectedTo;
    }

    public String getAggregationCriteria() {
      return aggregationCriteria;
    }

    public void setAggregationCriteria(String aggregationCriteria) {
      this.aggregationCriteria = aggregationCriteria;
    }

    public List<String> getCenters() {
      return centers;
    }

    public void setCenters(List<String> centers) {
      this.centers = centers;
    }

    public List<String> getSites() {
      return sites;
    }

    public void setSites(List<String> sites) {
      this.sites = sites;
    }

    public List<String> getBloodGroups() {
      return bloodGroups;
    }

    public void setBloodGroups(List<String> bloodGroups) {
      this.bloodGroups = bloodGroups;
    }
  }

  @RequestMapping(value = "/inventoryReportFormGenerator", method = RequestMethod.GET)
  public ModelAndView inventoryReportFormGenerator(Model model) {
    ModelAndView mv = new ModelAndView("inventoryReportForm");
    utilController.addTipsToModel(model.asMap(), "report.inventory.generate");
    utilController.addTipsToModel(model.asMap(), "report.inventory.productinventorychart");
    Map<String, Object> m = model.asMap();
    m.put("centers", locationRepository.getAllCenters());
    mv.addObject("model", m);
    return mv;
  }

  @RequestMapping(value="/generateInventoryReport", method=RequestMethod.GET)
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
  public ModelAndView collectionsReportFormGenerator(Model model) {
    ModelAndView mv = new ModelAndView("collectionsReport");
    Map<String, Object> m = model.asMap();
    utilController.addTipsToModel(model.asMap(), "report.collections.collectionsreport");
    m.put("centers", locationRepository.getAllCenters());
    m.put("sites", locationRepository.getAllCollectionSites());
    mv.addObject("collectionsReportForm", new CollectionsReportBackingForm());
    mv.addObject("model", model);
    return mv;
  }

  @RequestMapping("/getCollectionsReport")
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

  private Date dateSubtract(Date dateTo, int field, int amount) {
    Calendar gcal = new GregorianCalendar();
    gcal.setTime(dateTo);
    gcal.add(field, -amount);
    return gcal.getTime();
  }

  public static class TestResultsReportBackingForm {

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

  }

  @RequestMapping(value = "/testResultsReportFormGenerator", method = RequestMethod.GET)
  public ModelAndView testResultsReportFormGenerator(Model model) {
    ModelAndView mv = new ModelAndView("testResultsReport");
    Map<String, Object> m = model.asMap();
    m.put("centers", locationRepository.getAllCenters());
    m.put("sites", locationRepository.getAllCollectionSites());
    utilController.addTipsToModel(m, "report.collections.testresultsreport");
    mv.addObject("testResultsReportForm", new TestResultsReportBackingForm());
    mv.addObject("model", model);
    return mv;
  }

  @RequestMapping("/getTestResultsReport")
  public @ResponseBody
  Map<String, Object> getTestResultsReport(
      @ModelAttribute("testResultsReportForm") TestResultsReportBackingForm form,
      BindingResult result, Model model) {

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
  
      Map<String, Map<Long, Long>> numTestResults = testResultsRepository
          .findNumberOfPositiveTests(dateFrom, dateTo,
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
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    return m;
  }

}
