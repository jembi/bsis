package controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import model.admin.FormField;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import repository.FormFieldRepository;

@Controller
public class AdminController {

  @Autowired
  FormFieldRepository formFieldRepository;

  @Autowired
  CreateDataController createDataController;
  
  public static String getUrl(HttpServletRequest req) {
    String reqUrl = req.getRequestURL().toString();
    String queryString = req.getQueryString();   // d=789
    if (queryString != null) {
        reqUrl += "?"+queryString;
    }
    return reqUrl;
  }

  @RequestMapping("/getFormToConfigure")
  public ModelAndView getFormToConfigure(HttpServletRequest request,
          @RequestParam(value="formToConfigure", required=false) String formToConfigure, 
                              Model model) {
    ModelAndView mv = new ModelAndView("admin/configureForms");

    Map<String, Object> m = model.asMap();
    m.put("requestUrl", getUrl(request));
    m.put("formName", formToConfigure);
    m.put("formFields", formFieldRepository.getFormFields(formToConfigure));
    mv.addObject("model", m);
    return mv;
  }

  @RequestMapping(value="/configureFormFieldChange", method=RequestMethod.POST)
  public @ResponseBody Map<String, ? extends Object>
    configureFormFieldChange(@RequestParam Map<String, String> params) {

    boolean success = true;
    String errMsg = "";

    try {
      System.out.println(params);

      FormField ff = new FormField();
      String id = params.get("id");
      ff.setId(Long.parseLong(id));

      Boolean hidden = params.get("hidden").equals("true") ? true : false;
      ff.setHidden(hidden);

      String defaultValue = params.get("defaultValue").trim();
      ff.setDefaultValue(defaultValue);

      String sourceField = params.get("sourceField").trim();
      if (sourceField.equals("nocopy")) {
        ff.setDerived(false);
        ff.setSourceField("");
      } else {
        ff.setDerived(true);
        ff.setSourceField(sourceField);
      }
      FormField updatedFormField = formFieldRepository.updateFormField(ff);
      if (updatedFormField == null) {
        success = false;
        errMsg = "Internal Server Error";
      }
    } catch (Exception ex) {
      ex.printStackTrace();
      success = false;
      errMsg = "Internal Server Error";
    }
    
    Map<String, Object> m = new HashMap<String, Object>();
    m.put("success", success);
    m.put("errMsg", errMsg);
    return m;
  }
  
  @RequestMapping("/configureForms")
  public ModelAndView configureForms(HttpServletRequest request,
                              Model model) {
    ModelAndView mv = new ModelAndView("admin/selectFormToConfigure");

    Map<String, Object> m = model.asMap();
    m.put("requestUrl", getUrl(request));    
    mv.addObject("model", m);
    return mv;
  }

  @RequestMapping("/createSampleDataFormGenerator")
  public ModelAndView createSampleDataFormGenerator(
                HttpServletRequest request, Map<String, Object> params) {

    ModelAndView mv = new ModelAndView("admin/createSampleDataForm");
    return mv;
  }

  @RequestMapping(value="/createSampleData", method=RequestMethod.POST)
  public @ResponseBody Map<String, ? extends Object> createSampleData(
                HttpServletRequest request,
                @RequestParam Map<String, String> params) {

    boolean success = true;
    String errMsg = "";
    try {
      Integer numDonors = Integer.parseInt(params.get("numDonors"));
      Integer numCollections = Integer.parseInt(params.get("numCollections"));
      Integer numProducts = Integer.parseInt(params.get("numProducts"));
      Integer numTestResults = Integer.parseInt(params.get("numTestResults"));
      Integer numRequests = Integer.parseInt(params.get("numRequests"));
      Integer numUsages = Integer.parseInt(params.get("numUsages"));
      Integer numIssues = Integer.parseInt(params.get("numIssues"));

      createDataController.createDonors(numDonors);
      createDataController.createCollectionsWithTestResults(numCollections);
    }
    catch (Exception ex) {
      ex.printStackTrace();
      success = false;
      errMsg = "Internal Server Error";
    }
    Map<String, Object> m = new HashMap<String, Object>();
    m.put("requestUrl", getUrl(request));
    m.put("success", success);
    m.put("errMsg", errMsg);
    return m;
  }

}
