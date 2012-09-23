package controller;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import repository.CollectionRepository;
import repository.TestResultRepository;

@Controller
public class ReportsController {

  @Autowired
  private CollectionRepository collectionRepository;

  @Autowired
  private TestResultRepository testResultsRepository;

  public static class CollectionsReportBackingForm {

    String dateCollectedFrom;
    String dateCollectedTo;
    String aggregationCriteria;

    public CollectionsReportBackingForm() {
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
  }

  @RequestMapping(value = "/collectionsReportFormGenerator", method = RequestMethod.GET)
  public ModelAndView collectionsReportFormGenerator(Model model) {
    ModelAndView mv = new ModelAndView("collectionsReport");
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
        .findNumberOfCollections(dateCollectedFrom, dateCollectedTo,
            form.getAggregationCriteria());
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
    } catch (ParseException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return m;
  }

  public static class TestResultsReportBackingForm {

    String dateTestedFrom;
    String dateTestedTo;
    String aggregationCriteria;
    String hiv;
    String hbv;
    String hcv;
    String syphilis;
    String none;
    List<String> tests;

    public TestResultsReportBackingForm() {
    }

    public String getDateTestedFrom() {
      return dateTestedFrom;
    }

    public void setDateTestedFrom(String dateCollectedFrom) {
      this.dateTestedFrom = dateCollectedFrom;
    }

    public String getDateTestedTo() {
      return dateTestedTo;
    }

    public void setDateTestedTo(String dateTestedTo) {
      this.dateTestedTo = dateTestedTo;
    }

    public String getAggregationCriteria() {
      return aggregationCriteria;
    }

    public void setAggregationCriteria(String aggregationCriteria) {
      this.aggregationCriteria = aggregationCriteria;
    }

    public String getHiv() {
      return hiv;
    }

    public void setHiv(String hiv) {
      this.hiv = hiv;
    }

    public String getHbv() {
      return hbv;
    }

    public void setHbv(String hbv) {
      this.hbv = hbv;
    }

    public String getHcv() {
      return hcv;
    }

    public void setHcv(String hcv) {
      this.hcv = hcv;
    }

    public String getSyphilis() {
      return syphilis;
    }

    public void setSyphilis(String syphilis) {
      this.syphilis = syphilis;
    }

    public String getNone() {
      return none;
    }

    public void setNone(String none) {
      this.none = none;
    }

    public List<String> getTests() {
      return tests;
    }

    public void setTests(List<String> tests) {
      this.tests = tests;
    }
  }

  @RequestMapping(value = "/testResultsReportFormGenerator", method = RequestMethod.GET)
  public ModelAndView testResultsReportFormGenerator(Model model) {
    ModelAndView mv = new ModelAndView("testResultsReport");
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
    String hiv = "";
    String hbv = "";
    String hcv = "";
    String syphilis = "";
    String none = "";
    if (form.getTests() != null) {
      hiv = form.getTests().contains("hiv") ? "reactive" : "";
      hbv = form.getTests().contains("hbv") ? "reactive" : "";
      hcv = form.getTests().contains("hcv") ? "reactive" : "";
      syphilis = form.getTests().contains("syphilis") ? "reactive" : "";
      none = form.getTests().contains("none") ? "none" : "";
    }

    Map<Long, Long> numTestResults = testResultsRepository
        .findNumberOfPositiveTests(dateTestedFrom, dateTestedTo,
            form.getAggregationCriteria(), hiv, hbv, hcv, syphilis, none);
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
