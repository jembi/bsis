package controller;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

    public CollectionsReportBackingForm() {
      centers = Arrays.asList(new String[0]);
      sites = Arrays.asList(new String[0]);
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
  }

  @RequestMapping(value = "/inventoryReportFormGenerator", method = RequestMethod.GET)
  public ModelAndView inventoryReportFormGenerator(Model model) {
    ModelAndView mv = new ModelAndView("inventoryReportForm");
    utilController.addTipsToModel(model.asMap(), "report.inventory.generate");
    utilController.addTipsToModel(model.asMap(), "report.inventory.productinventorychart");
    mv.addObject("model", model);
    return mv;
  }

  @RequestMapping(value="/generateInventoryReport", method=RequestMethod.GET)
  public @ResponseBody Map<String, Object> generateInventoryReport(
                  HttpServletRequest request, HttpServletResponse response,
                  @RequestParam(value="status") String status
                  ) {
    List<String> productStatuses = Arrays.asList(status.split("\\|"));
    Map<String, Object> data = null;
    try {
      data = productRepository.generateInventorySummaryFast(productStatuses);
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
    Map<Long, Long> numCollections = collectionRepository
        .findNumberOfCollectedSamples(dateCollectedFrom, dateCollectedTo,
            form.getAggregationCriteria(), form.getCenters(), form.getSites());
    Map<String, Object> m = new HashMap<String, Object>();
    // TODO: potential leap year bug here
    Long interval = (long) (24 * 3600 * 1000);
    if (form.getAggregationCriteria().equals("monthly"))
      interval = interval * 30;
    else if (form.getAggregationCriteria().equals("yearly"))
      interval = interval * 365;
    m.put("interval", interval);
    m.put("numCollections", numCollections);
    DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
    Date date;
    try {
      date = dateFormat.parse(dateCollectedFrom);
      m.put("dateCollectedFromUTC", date.getTime());
      date = dateFormat.parse(dateCollectedTo);
      m.put("dateCollectedToUTC", date.getTime());
    } catch (ParseException ex) {
      // TODO Auto-generated catch block
      ex.printStackTrace();
    }
    return m;
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

    Map<String, Map<Long, Long>> numTestResults = testResultsRepository
        .findNumberOfPositiveTests(dateTestedFrom, dateTestedTo,
            form.getAggregationCriteria(), form.getCenters(), form.getSites());

    Map<String, Object> m = new HashMap<String, Object>();
    // TODO: potential leap year bug here
    Long interval = (long) (24 * 3600 * 1000);
    if (form.getAggregationCriteria().equals("monthly"))
      interval = interval * 30;
    else if (form.getAggregationCriteria().equals("yearly"))
      interval = interval * 365;
    m.put("interval", interval);
    m.put("numTestResults", numTestResults);
    DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
    Date date;
    try {
      date = (dateTestedFrom == null || dateTestedFrom.equals("")) ? dateFormat
          .parse("12/31/2011") : dateFormat.parse(dateTestedFrom);
      m.put("dateTestedFromUTC", date.getTime());
      date = (dateTestedTo == null || dateTestedTo.equals("")) ? new Date()
          : dateFormat.parse(dateTestedTo);
      m.put("dateTestedToUTC", date.getTime());
    } catch (ParseException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    return m;
  }

}
