package controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.admin.FormField;
import model.bloodbagtype.BloodBagType;
import model.producttype.ProductType;
import model.tips.Tips;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import repository.BloodBagTypeRepository;
import repository.FormFieldRepository;
import repository.LocationRepository;
import repository.ProductTypeRepository;
import repository.TipsRepository;

import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
public class AdminController {

  @Autowired
  FormFieldRepository formFieldRepository;

  @Autowired
  CreateDataController createDataController;

  @Autowired
  LocationRepository locationRepository;

  @Autowired
  ProductTypeRepository productTypesRepository;

  @Autowired
  BloodBagTypeRepository bloodBagTypesRepository;

  @Autowired
  TipsRepository tipsRepository;
  
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

      Boolean isRequired = params.get("isRequired").equals("true") ? true : false;
      ff.setIsRequired(isRequired);

      Boolean autoGenerate = params.get("autoGenerate").equals("true") ? true : false;
      ff.setAutoGenerate(autoGenerate);

      String displayName = params.get("displayName").trim();
      ff.setDisplayName(displayName);

      String defaultValue = params.get("defaultValue").trim();
      ff.setDefaultValue(defaultValue);

//      String sourceField = params.get("sourceField").trim();
//      if (sourceField.equals("nocopy")) {
//        ff.setDerived(false);
//        ff.setSourceField("");
//      } else {
//        ff.setDerived(true);
//        ff.setSourceField(sourceField);
//      }
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
      createDataController.createProducts(numProducts);
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

  @RequestMapping(value="/configureTipsFormGenerator", method=RequestMethod.GET)
  public ModelAndView configureTipsFormGenerator(
      HttpServletRequest request, HttpServletResponse response,
      Model model) {

    ModelAndView mv = new ModelAndView("admin/configureTips");
    Map<String, Object> m = model.asMap();
    addAllTipsToModel(m);
    m.put("refreshUrl", getUrl(request));
    mv.addObject("model", model);
    return mv;
  }

  @RequestMapping(value="/configureProductTypesFormGenerator", method=RequestMethod.GET)
  public ModelAndView configureProductTypesFormGenerator(
      HttpServletRequest request, HttpServletResponse response,
      Model model) {

    ModelAndView mv = new ModelAndView("admin/configureProductTypes");
    Map<String, Object> m = model.asMap();
    addAllProductTypesToModel(m);
    m.put("refreshUrl", getUrl(request));
    mv.addObject("model", model);
    return mv;
  }


  @RequestMapping(value="/configureBloodBagTypesFormGenerator", method=RequestMethod.GET)
  public ModelAndView configureBloodBagTypesFormGenerator(
      HttpServletRequest request, HttpServletResponse response,
      Model model) {

    ModelAndView mv = new ModelAndView("admin/configureBloodBagTypes");
    Map<String, Object> m = model.asMap();
    addAllBloodBagTypesToModel(m);
    m.put("refreshUrl", getUrl(request));
    mv.addObject("model", model);
    return mv;
  }

  private void addAllBloodBagTypesToModel(Map<String, Object> m) {
    m.put("allBloodBagTypes", bloodBagTypesRepository.getAllBloodBagTypes());
  }


  private void addAllProductTypesToModel(Map<String, Object> m) {
    m.put("allProductTypes", productTypesRepository.getAllProductTypes());
  }

  private void addAllTipsToModel(Map<String, Object> m) {
    m.put("allTips", tipsRepository.getAllTips());
  }

  @RequestMapping("/configureTips")
  public ModelAndView configureTips(
      HttpServletRequest request, HttpServletResponse response,
      @RequestParam(value="params") String paramsAsJson, Model model) {
    ModelAndView mv = new ModelAndView("admin/configureTips");
    System.out.println(paramsAsJson);
    List<Tips> allTips = new ArrayList<Tips>();
    try {
      Map<String, Object> params = new ObjectMapper().readValue(paramsAsJson, HashMap.class);
      for (String tipsKey : params.keySet()) {
        String tipsContent = (String) params.get(tipsKey);
        Tips tips = new Tips();
        tips.setTipsKey(tipsKey);
        tips.setTipsContent(tipsContent);
        allTips.add(tips);
      }
      tipsRepository.saveAllTips(allTips);
      System.out.println(params);
    } catch (Exception ex) {
      ex.printStackTrace();
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }

    Map<String, Object> m = model.asMap();
    addAllTipsToModel(m);
    m.put("refreshUrl", "configureTipsFormGenerator.html");
    mv.addObject("model", model);
    return mv;
  }

  @RequestMapping("/configureProductTypes")
  public ModelAndView configureProductTypes(
      HttpServletRequest request, HttpServletResponse response,
      @RequestParam(value="params") String paramsAsJson, Model model) {
    ModelAndView mv = new ModelAndView("admin/configureProductTypes");
    System.out.println(paramsAsJson);
    List<ProductType> allProducTypes = new ArrayList<ProductType>();
    try {
      Map<String, Object> params = new ObjectMapper().readValue(paramsAsJson, HashMap.class);
      for (String productType : params.keySet()) {
        String productTypeName = (String) params.get(productType);
        ProductType pt = new ProductType();
        pt.setProductType(productType);
        pt.setProductTypeName(productTypeName);
        pt.setIsDeleted(false);
        allProducTypes.add(pt);
      }
      productTypesRepository.saveAllProductTypes(allProducTypes);
      System.out.println(params);
    } catch (Exception ex) {
      ex.printStackTrace();
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }

    Map<String, Object> m = model.asMap();
    addAllProductTypesToModel(m);
    m.put("refreshUrl", "configureProductTypesFormGenerator.html");
    mv.addObject("model", model);
    return mv;
  }

  @RequestMapping("/configureBloodBagTypes")
  public ModelAndView configureBloodBagTypes(
      HttpServletRequest request, HttpServletResponse response,
      @RequestParam(value="params") String paramsAsJson, Model model) {
    ModelAndView mv = new ModelAndView("admin/configureBloodBagTypes");
    System.out.println(paramsAsJson);
    List<BloodBagType> allBloodBagTypes = new ArrayList<BloodBagType>();
    try {
      Map<String, Object> params = new ObjectMapper().readValue(paramsAsJson, HashMap.class);
      for (String bloodBagType : params.keySet()) {
        String bloodBagTypeName = (String) params.get(bloodBagType);
        BloodBagType pt = new BloodBagType();
        pt.setBloodBagType(bloodBagType);
        pt.setBloodBagTypeName(bloodBagTypeName);
        pt.setIsDeleted(false);
        allBloodBagTypes.add(pt);
      }
      bloodBagTypesRepository.saveAllBloodBagTypes(allBloodBagTypes);
      System.out.println(params);
    } catch (Exception ex) {
      ex.printStackTrace();
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }

    Map<String, Object> m = model.asMap();
    addAllBloodBagTypesToModel(m);
    m.put("refreshUrl", "configureBloodBagTypesFormGenerator.html");
    mv.addObject("model", model);
    return mv;
  }

}
