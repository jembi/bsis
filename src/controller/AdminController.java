package controller;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.admin.ConfigPropertyConstants;
import model.admin.FormField;
import model.bloodbagtype.BloodBagType;
import model.bloodtest.BloodTest;
import model.compatibility.CrossmatchType;
import model.donationtype.DonationType;
import model.producttype.ProductType;
import model.requesttype.RequestType;
import model.tips.Tips;

import org.apache.http.conn.util.InetAddressUtils;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import repository.BloodBagTypeRepository;
import repository.BloodTestRepository;
import repository.CrossmatchTypeRepository;
import repository.DonationTypeRepository;
import repository.FormFieldRepository;
import repository.GenericConfigRepository;
import repository.LocationRepository;
import repository.ProductTypeRepository;
import repository.RequestTypeRepository;
import repository.TipsRepository;
import repository.UserRepository;

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
  DonationTypeRepository donationTypesRepository;

  @Autowired
  RequestTypeRepository requestTypesRepository;

  @Autowired
  CrossmatchTypeRepository crossmatchTypesRepository;

  @Autowired
  TipsRepository tipsRepository;

  @Autowired
  UserRepository userRepository;

  @Autowired
  BloodTestRepository bloodTestRepository;

  @Autowired
  GenericConfigRepository genericConfigRepository;
  
  @Autowired
  ServletContext servletContext;

  @Autowired
  UtilController utilController;
  
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
    m.put("refreshUrl", getUrl(request));
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

      String maxLength = params.get("maxLength").trim();
      ff.setMaxLength(Integer.parseInt(maxLength));

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

  @RequestMapping(value="/configureRequestTypesFormGenerator", method=RequestMethod.GET)
  public ModelAndView configureRequestTypesFormGenerator(
      HttpServletRequest request, HttpServletResponse response,
      Model model) {

    ModelAndView mv = new ModelAndView("admin/configureRequestTypes");
    Map<String, Object> m = model.asMap();
    addAllRequestTypesToModel(m);
    m.put("refreshUrl", getUrl(request));
    mv.addObject("model", model);
    return mv;
  }

  @RequestMapping(value="/configureCrossmatchTypesFormGenerator", method=RequestMethod.GET)
  public ModelAndView configureCrossmatchTypesFormGenerator(
      HttpServletRequest request, HttpServletResponse response,
      Model model) {

    ModelAndView mv = new ModelAndView("admin/configureCrossmatchTypes");
    Map<String, Object> m = model.asMap();
    addAllCrossmatchTypesToModel(m);
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

  @RequestMapping(value="/backupDataFormGenerator", method=RequestMethod.GET)
  public ModelAndView backupDataFormGenerator(
      HttpServletRequest request, HttpServletResponse response,
      Model model) {

    ModelAndView mv = new ModelAndView("admin/backupData");
    Map<String, Object> m = model.asMap();
    m.put("refreshUrl", getUrl(request));
    mv.addObject("model", model);
    return mv;
  }

  @RequestMapping(value="/backupData", method=RequestMethod.GET)
  public void backupData(
      HttpServletRequest request, HttpServletResponse response,
      Model model) {

    DateFormat dateFormat = new SimpleDateFormat("MM_dd_yyyy_HH_mm_ss");

    String currentDate = dateFormat.format(new Date());
    String fileName = "mysql_backup_" + currentDate;
    String fullFileName = servletContext.getRealPath("") + "/tmp/" + fileName + ".zip";

    System.out.println("Writing backup to " + fullFileName);

    try {
      Properties prop = utilController.getV2VProperties();
      String mysqldumpPath = (String) prop.get("v2v.dbbackup.mysqldumppath");
      String username = (String) prop.get("v2v.dbbackup.username");
      String password = (String) prop.get("v2v.dbbackup.password");
      String dbname = (String) prop.get("v2v.dbbackup.dbname");

      System.out.println(mysqldumpPath);
      System.out.println(username);
      System.out.println(password);
      System.out.println(dbname);

      ProcessBuilder pb = new ProcessBuilder(mysqldumpPath,
                    "--single-transaction",
                    "-u", username, "-p" + password, dbname);

      pb.redirectErrorStream(true); // equivalent of 2>&1
      Process p = pb.start();

      InputStream in = p.getInputStream();
      BufferedInputStream buf = new BufferedInputStream(in);

      response.setContentType("application/zip");
      response.addHeader("content-disposition", "attachment; filename=" + fileName + ".zip");

      ZipOutputStream zipOut = new ZipOutputStream(response.getOutputStream());
      zipOut.putNextEntry(new ZipEntry(fileName + ".sql"));
      
      IOUtils.copy(buf, zipOut);

      zipOut.finish();
      zipOut.close();
      p.waitFor();
      System.out.println("Exit value: " + p.exitValue());
    } catch (IOException e) {
      e.printStackTrace();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
  @RequestMapping(value="/configureDonationTypesFormGenerator", method=RequestMethod.GET)
  public ModelAndView configureDonationTypesFormGenerator(
      HttpServletRequest request, HttpServletResponse response,
      Model model) {

    ModelAndView mv = new ModelAndView("admin/configureDonationTypes");
    Map<String, Object> m = model.asMap();
    addAllDonationTypesToModel(m);
    m.put("refreshUrl", getUrl(request));
    mv.addObject("model", model);
    return mv;
  }

  private void addAllDonationTypesToModel(Map<String, Object> m) {
    m.put("allDonationTypes", donationTypesRepository.getAllDonationTypes());
  }

  private void addAllBloodBagTypesToModel(Map<String, Object> m) {
    m.put("allBloodBagTypes", bloodBagTypesRepository.getAllBloodBagTypes());
  }

  private void addAllProductTypesToModel(Map<String, Object> m) {
    m.put("allProductTypes", productTypesRepository.getAllProductTypes());
  }

  private void addAllRequestTypesToModel(Map<String, Object> m) {
    m.put("allRequestTypes", requestTypesRepository.getAllRequestTypes());
  }

  private void addAllCrossmatchTypesToModel(Map<String, Object> m) {
    m.put("allCrossmatchTypes", crossmatchTypesRepository.getAllCrossmatchTypes());
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
    List<ProductType> allProductTypes = new ArrayList<ProductType>();
    try {
      Map<String, Object> params = new ObjectMapper().readValue(paramsAsJson, HashMap.class);
      for (String id : params.keySet()) {
        String productType = (String) params.get(id);
        ProductType pt = new ProductType();
        try {
          pt.setId(Integer.parseInt(id));
        } catch (NumberFormatException ex) {
          ex.printStackTrace();
          pt.setId(null);
        }
        pt.setProductType(productType);
        pt.setIsDeleted(false);
        allProductTypes.add(pt);
      }
      productTypesRepository.saveAllProductTypes(allProductTypes);
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

  @RequestMapping("/configureRequestTypes")
  public ModelAndView configureRequestTypes(
      HttpServletRequest request, HttpServletResponse response,
      @RequestParam(value="params") String paramsAsJson, Model model) {
    ModelAndView mv = new ModelAndView("admin/configureRequestTypes");
    System.out.println(paramsAsJson);
    List<RequestType> allRequestTypes = new ArrayList<RequestType>();
    try {
      Map<String, Object> params = new ObjectMapper().readValue(paramsAsJson, HashMap.class);
      for (String id : params.keySet()) {
        String requestType = (String) params.get(id);
        RequestType rt = new RequestType();
        try {
          rt.setId(Integer.parseInt(id));
        } catch (NumberFormatException ex) {
          ex.printStackTrace();
          rt.setId(null);
        }
        rt.setRequestType(requestType);
        rt.setIsDeleted(false);
        allRequestTypes.add(rt);
      }
      requestTypesRepository.saveAllRequestTypes(allRequestTypes);
      System.out.println(params);
    } catch (Exception ex) {
      ex.printStackTrace();
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }

    Map<String, Object> m = model.asMap();
    addAllRequestTypesToModel(m);
    m.put("refreshUrl", "configureRequestTypesFormGenerator.html");
    mv.addObject("model", model);
    return mv;
  }

  @RequestMapping("/configureCrossmatchTypes")
  public ModelAndView configureCrossmatchTypes(
      HttpServletRequest request, HttpServletResponse response,
      @RequestParam(value="params") String paramsAsJson, Model model) {
    ModelAndView mv = new ModelAndView("admin/configureCrossmatchTypes");
    System.out.println(paramsAsJson);
    List<CrossmatchType> allCrossmatchTypes = new ArrayList<CrossmatchType>();
    try {
      Map<String, Object> params = new ObjectMapper().readValue(paramsAsJson, HashMap.class);
      for (String id : params.keySet()) {
        String crossmatchType = (String) params.get(id);
        CrossmatchType ct = new CrossmatchType();
        try {
          ct.setId(Integer.parseInt(id));
        } catch (NumberFormatException ex) {
          ex.printStackTrace();
          ct.setId(null);
        }
        ct.setCrossmatchType(crossmatchType);
        ct.setIsDeleted(false);
        allCrossmatchTypes.add(ct);
      }
      crossmatchTypesRepository.saveAllCrossmatchTypes(allCrossmatchTypes);
      System.out.println(params);
    } catch (Exception ex) {
      ex.printStackTrace();
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }

    Map<String, Object> m = model.asMap();
    addAllCrossmatchTypesToModel(m);
    m.put("refreshUrl", "configureCrossmatchTypesFormGenerator.html");
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
      for (String id : params.keySet()) {
        String bloodBagType = (String) params.get(id);
        BloodBagType bt = new BloodBagType();
        try {
          bt.setId(Integer.parseInt(id));
        } catch (NumberFormatException ex) {
          ex.printStackTrace();
          bt.setId(null);
        }
        bt.setBloodBagType(bloodBagType);
        bt.setIsDeleted(false);
        allBloodBagTypes.add(bt);
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

  @RequestMapping("/configureDonationTypes")
  public ModelAndView configureDonationTypes(
      HttpServletRequest request, HttpServletResponse response,
      @RequestParam(value="params") String paramsAsJson, Model model) {
    ModelAndView mv = new ModelAndView("admin/configureDonationTypes");
    List<DonationType> allDonationTypes = new ArrayList<DonationType>();
    try {
      Map<String, Object> params = new ObjectMapper().readValue(paramsAsJson, HashMap.class);
      for (String id : params.keySet()) {
        String donationType = (String) params.get(id);
        DonationType dt = new DonationType();
        try {
          dt.setId(Integer.parseInt(id));
        } catch (NumberFormatException ex) {
          ex.printStackTrace();
          dt.setId(null);
        }
        dt.setDonationType(donationType);
        dt.setIsDeleted(false);

        allDonationTypes.add(dt);
      }
      donationTypesRepository.saveAllDonationTypes(allDonationTypes);
      System.out.println(params);
    } catch (Exception ex) {
      ex.printStackTrace();
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }

    Map<String, Object> m = model.asMap();
    addAllDonationTypesToModel(m);
    m.put("refreshUrl", "configureDonationTypesFormGenerator.html");
    mv.addObject("model", model);
    return mv;
  }

  @RequestMapping(value="/configureWorksheetsFormGenerator", method=RequestMethod.GET)
  public ModelAndView configureWorksheetsFormGenerator(
      HttpServletRequest request, HttpServletResponse response,
      Model model) {

    ModelAndView mv = new ModelAndView("admin/configureWorksheets");
    Map<String, Object> m = model.asMap();
    m.put("refreshUrl", getUrl(request));

    List<String> propertyOwners = Arrays.asList(ConfigPropertyConstants.COLLECTIONS_WORKSHEET);
    Map<String, String> worksheetProperties = genericConfigRepository.getConfigProperties(propertyOwners);
    m.put("worksheetConfig", worksheetProperties);    
    m.put("bloodTests", bloodTestRepository.getAllBloodTests());

    mv.addObject("model", model);
    return mv;
  }

  @RequestMapping(value="/configureWorksheets")
  public @ResponseBody Map<String, Object> configureWorksheet(
      HttpServletRequest request,
      HttpServletResponse response,
      @RequestParam Map<String, String> params) {

    Map<String, Object> result = new HashMap<String, Object>();
    try {
      Long.parseLong(params.get(ConfigPropertyConstants.COLLECTIONS_WORKSHEET_ROW_HEIGHT));
      Long.parseLong(params.get(ConfigPropertyConstants.COLLECTIONS_WORKSHEET_COLUMN_WIDTH));

      if (!params.containsKey("collectionNumber"))
        params.put("collectionNumber", "false");

      if (!params.containsKey("testedOn"))
        params.put("testedOn", "false");

      for (BloodTest bt : bloodTestRepository.getAllBloodTests()) {
        if (!params.containsKey(bt.getName()))
          params.put(bt.getName(), "false");
      }
      
      genericConfigRepository.updateWorksheetProperties(params);
      result.put("success", true);
    }
    catch (Exception ex) {
      ex.printStackTrace();
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      result.put("success", false);
    }

    return result;
  }

  @RequestMapping(value="/adminWelcomePageGenerator")
  public ModelAndView adminWelcomePageGenerator(HttpServletRequest request, Model model) {
    ModelAndView mv = new ModelAndView("admin/adminWelcomePage");
    Map<String, Object> m = model.asMap();
    try {
      InetAddress ip = InetAddress.getLocalHost();
      List<InetAddress> wirelessAddresses = getServerNetworkAddresses();
      List<String> serverAddresses = new ArrayList<String>();
      for (InetAddress addr : wirelessAddresses) {
        serverAddresses.add("http://" + addr.getHostAddress() + ":" + request.getServerPort() + "/v2v");
      }
      m.put("serverAddresses", serverAddresses);
    } catch (UnknownHostException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    mv.addObject("model", m);
    return mv;
  }

  List<InetAddress> getServerNetworkAddresses() {
    List<InetAddress> listOfServerAddresses = new ArrayList();
    Enumeration<NetworkInterface> list;
    try {
        list = NetworkInterface.getNetworkInterfaces();

        while(list.hasMoreElements()) {
            NetworkInterface iface = (NetworkInterface) list.nextElement();

            if(iface == null) continue;

            if(!iface.isLoopback() && iface.isUp()) {
                System.out.println("Found non-loopback, up interface:" + iface);

                Iterator<InterfaceAddress> it = iface.getInterfaceAddresses().iterator();
                while (it.hasNext()) {
                    InterfaceAddress address = (InterfaceAddress) it.next();

                    System.out.println("Found address: " + address);

                    if(address == null) continue;
                    if (InetAddressUtils.isIPv4Address(address.getAddress().getHostAddress()))
                        listOfServerAddresses.add(address.getAddress());
                }
            }
        }
    } catch (SocketException ex) {
        return new ArrayList<InetAddress>();
    }
    return listOfServerAddresses;
  }
}
