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
import repository.DisplayNamesRepository;
import repository.LocationRepository;
import repository.RecordFieldsConfigRepository;
import repository.RequestRepository;

@Controller
public class ReportsController {

  @Autowired
  private RequestRepository requestRepository;

  @Autowired
  private LocationRepository locationRepository;

  @Autowired
  private DisplayNamesRepository displayNamesRepository;

  @Autowired
  private RecordFieldsConfigRepository recordFieldsConfigRepository;

  @Autowired
  private CollectionRepository collectionRepository;

  public static class CollectionsReportBackingForm {

    String dateCollectedFrom;
    String dateCollectedTo;

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
  }

  @RequestMapping(value = "/collectionsReportFormGenerator", method = RequestMethod.GET)
  public ModelAndView collectionReportFormGenerator(Model model) {
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
    System.out.println(dateCollectedFrom);
    System.out.println(dateCollectedTo);
    Map<Long, Long> numCollections = collectionRepository.findNumberOfCollections(
        dateCollectedFrom, dateCollectedTo);
    System.out.println(numCollections);
    
    Map<String, Object> m = new HashMap<String, Object>();
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

}
