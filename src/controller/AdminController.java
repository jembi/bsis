package controller;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import model.admin.FormField;
import model.bloodbagtype.BloodBagType;
import model.bloodtesting.BloodTest;
import model.bloodtesting.rules.BloodTestingRule;
import model.compatibility.CrossmatchType;
import model.donationtype.DonationType;
import model.requesttype.RequestType;
import model.tips.Tips;
import org.apache.http.conn.util.InetAddressUtils;
import org.apache.log4j.Logger;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import repository.BloodBagTypeRepository;
import repository.CrossmatchTypeRepository;
import repository.DonationTypeRepository;
import repository.FormFieldRepository;
import repository.GenericConfigRepository;
import repository.LabSetupRepository;
import repository.LocationRepository;
import repository.ProductTypeRepository;
import repository.RequestTypeRepository;
import repository.TipsRepository;
import repository.UserRepository;
import repository.WorksheetTypeRepository;
import repository.bloodtesting.BloodTestingRepository;
import utils.PermissionConstants;
import viewmodel.BloodTestViewModel;
import viewmodel.BloodTestingRuleViewModel;

@RestController
public class AdminController {
	
	private static final Logger LOGGER = Logger.getLogger(AdminController.class);

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
  BloodTestingRepository bloodTestingRepository;

  @Autowired
  WorksheetTypeRepository worksheetTypeRepository;

  @Autowired
  TipsRepository tipsRepository;

  @Autowired
  UserRepository userRepository;

  @Autowired
  GenericConfigRepository genericConfigRepository;
  
  @Autowired
  ServletContext servletContext;

  @Autowired
  LabSetupRepository labSetupRepository;
  
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

  @RequestMapping(value = "/getform", method = RequestMethod.GET)
  public  Map<String, Object> getFormToConfigure(HttpServletRequest request,
          @RequestParam(value="formToConfigure", required=false) String formToConfigure) {
    Map<String, Object> map = new HashMap<String, Object>();

    map.put("formName", formToConfigure);
    map.put("formFields", formFieldRepository.getFormFields(formToConfigure));
    return map;
  }

  @RequestMapping(value="/formfieldchange", method=RequestMethod.POST)
  @PreAuthorize("hasRole('"+PermissionConstants.MANAGE_FORMS+"')")
  public  Map<String, ? extends Object>
    configureFormFieldChange(@RequestParam Map<String, String> params) {

    boolean success = true;
    String errMsg = "";

    try {
      LOGGER.debug(params);

      FormField ff = new FormField();
      String id = params.get("id");
      ff.setId(Long.parseLong(id));

      Boolean hidden = params.get("hidden").equals("true") ? true : false;
      ff.setHidden(hidden);

      Boolean isRequired = params.get("isRequired").equals("true") ? true : false;
      ff.setIsRequired(isRequired);

      Boolean autoGenerate = params.get("autoGenerate").equals("true") ? true : false;
      ff.setAutoGenerate(autoGenerate);

      Boolean useCurrentTime = params.get("useCurrentTime").equals("true") ? true : false;
      ff.setUseCurrentTime(useCurrentTime);

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
    	LOGGER.debug(ex.getMessage() + ex.getStackTrace());
      success = false;
      errMsg = "Internal Server Error";
    }
    
    Map<String, Object> m = new HashMap<String, Object>();
    m.put("success", success);
    m.put("errMsg", errMsg);
    return m;
  }

  @RequestMapping(value = "/bloodtests", method = RequestMethod.GET)
  @PreAuthorize("hasRole('"+PermissionConstants.MANAGE_BLOOD_TESTS+"')")
  public  Map<String, Object> configureBloodTests(HttpServletRequest request) {
    Map<String, Object> map = new HashMap<String, Object>();
    List<BloodTestViewModel> bloodTests = new ArrayList<BloodTestViewModel>();
    for (BloodTest bt : bloodTestingRepository.getAllBloodTestsIncludeInactive()) {
      bloodTests.add(new BloodTestViewModel(bt));
    }
    map.put("bloodTests", bloodTests);
    map.put("worksheetTypes", worksheetTypeRepository.getAllWorksheetTypes());
    return map;
  }

  @RequestMapping(value = "/bloodtypingrules", method = RequestMethod.GET)
  @PreAuthorize("hasRole('"+PermissionConstants.MANAGE_BLOOD_TYPING_RULES+"')")
  public  Map<String, Object> configureBloodTypingTests(HttpServletRequest request) {
     Map<String, Object> map = new HashMap<String, Object>();
    map.put("bloodTypingTests", bloodTestingRepository.getBloodTypingTests());
    List<BloodTestingRuleViewModel> rules = new ArrayList<BloodTestingRuleViewModel>();
    for (BloodTestingRule rule : bloodTestingRepository.getBloodTypingRules(true)) {
      rules.add(new BloodTestingRuleViewModel(rule));
    }
    map.put("bloodTypingRules", rules);
    return map;
  }
  
  @RequestMapping(value = "/forms", method = RequestMethod.GET)
  @PreAuthorize("hasRole('"+PermissionConstants.MANAGE_FORMS+"')")
  public  Map<String, Object> configureForms(HttpServletRequest request,
                              Model model) {
    Map<String, Object> map = new HashMap<String, Object>();

    Map<String, Object> m = model.asMap();   
    map.put("model", m);
    return map;
  }

  /**
   * issue #209
   * seems method does nothing
   *
  @RequestMapping("/createSampleDataFormGenerator")
  @PreAuthorize("hasRole('"+PermissionConstants.MANAGE_DATA_SETUP+"')")
  public  Map<String, Object> createSampleDataFormGenerator(
                HttpServletRequest request, Map<String, Object> params) {

    Map<String, Object> map = new HashMap<String, Object>();
    return map;
  }
  */

  @RequestMapping(value="/createsampledata", method=RequestMethod.POST)
  @PreAuthorize("hasRole('"+PermissionConstants.MANAGE_DATA_SETUP+"')")
  public  Map<String, ? extends Object> createSampleData(
                HttpServletRequest request,
                @RequestParam Map<String, String> params) {

    boolean success = true;
    String errMsg = "";
    try {
      Integer numDonors = Integer.parseInt(params.get("numDonors"));
      Integer numCollections = Integer.parseInt(params.get("numCollections"));
      Integer numProducts = Integer.parseInt(params.get("numProducts"));
      Integer numRequests = Integer.parseInt(params.get("numRequests"));

      createDataController.createDonors(numDonors);
      createDataController.createCollectionsWithTestResults(numCollections);
      createDataController.createProducts(numProducts);
      createDataController.createRequests(numRequests);
    }
    catch (Exception ex) {
    	LOGGER.debug(ex.getMessage() + ex.getStackTrace());
      success = false;
      errMsg = "Internal Server Error";
    }
    Map<String, Object> m = new HashMap<String, Object>();
    m.put("success", success);
    m.put("errMsg", errMsg);
    return m;
  }

  @RequestMapping(value="/tipsform", method=RequestMethod.GET)
  @PreAuthorize("hasRole('"+PermissionConstants.MANAGE_TIPS+"')")
  public  Map<String, Object> configureTipsFormGenerator(
      HttpServletRequest request, HttpServletResponse response,
      Model model) {

    Map<String, Object> map = new HashMap<String, Object>();
    Map<String, Object> m = model.asMap();
    addAllTipsToModel(m);
    map.put("model", model);
    return map;
  }

  @RequestMapping(value="/labsetuppage", method=RequestMethod.GET)
  @PreAuthorize("hasRole('"+PermissionConstants.MANAGE_LAB_SETUP+"')")
  public  Map<String, Object> labSetupFormGenerator(
      HttpServletRequest request, HttpServletResponse response,
      Model model) {

    Map<String, Object> map = new HashMap<String, Object>();
    map.put("labsetup", genericConfigRepository.getConfigProperties("labsetup"));
    return map;
  }

  @SuppressWarnings("unchecked")
  @PreAuthorize("hasRole('"+PermissionConstants.MANAGE_LAB_SETUP+"')")
  @RequestMapping(value="/updateLabSetup", method=RequestMethod.POST)
  public  Map<String, Object> updateLabSetup(HttpServletRequest request,
      @RequestParam(value="labSetupParams") String params) {

    ObjectMapper mapper = new ObjectMapper();
    Map<String, String> paramsMap = null;
    try {
      paramsMap = mapper.readValue(params, HashMap.class);
    } catch (JsonParseException e) {
      LOGGER.debug(e.getMessage() + e.getStackTrace());
    } catch (JsonMappingException e) {
      LOGGER.debug(e.getMessage() + e.getStackTrace());
    } catch (IOException e) {
      // TODO Auto-generated catch block
      LOGGER.debug(e.getMessage() + e.getStackTrace());
    }
    LOGGER.debug("here");
    LOGGER.debug(params);
    labSetupRepository.updateLabSetup(paramsMap);
    Map<String, Object> m = new HashMap<String, Object>();
    m.put("success", true);
    return m;
  }
  
  @RequestMapping(value="/componenttypes", method=RequestMethod.GET)
  @PreAuthorize("hasRole('"+PermissionConstants.MANAGE_COMPONENT_COMBINATIONS+"')")
  public  Map<String, Object> configureProductTypes(
      HttpServletRequest request, HttpServletResponse response) {

    Map<String, Object> map = new HashMap<String, Object>();
    map.put("productTypes", productTypesRepository.getAllProductTypesIncludeDeleted());
    return map;
  }

  @RequestMapping(value="/componenttypecombinations", method=RequestMethod.GET)
  @PreAuthorize("hasRole('"+PermissionConstants.MANAGE_COMPONENT_COMBINATIONS+"')")
  public  Map<String, Object> configureProductTypeCombinations(
      HttpServletRequest request, HttpServletResponse response) {

    Map<String, Object> map = new HashMap<String, Object>();
    map.put("productTypeCombinations", productTypesRepository.getAllProductTypeCombinationsIncludeDeleted());
    map.put("productTypes", productTypesRepository.getAllProductTypes());
    return map;
  }

  @RequestMapping(value="/requesttypes", method=RequestMethod.GET)
  @PreAuthorize("hasRole('"+PermissionConstants.MANAGE_REQUESTS+"')")
  public  Map<String, Object> configureRequestTypesFormGenerator(
      HttpServletRequest request, HttpServletResponse response,
      Model model) {
    Map<String, Object> map = new HashMap<String, Object>();
    Map<String, Object> m = model.asMap();
    addAllRequestTypesToModel(m);
    map.put("model", model);
    return map;
  }

  @RequestMapping(value="/crossmatchtypes", method=RequestMethod.GET)
  @PreAuthorize("hasRole('"+PermissionConstants.MANAGE_CROSS_MATCH_TYPES+"')")
  public  Map<String, Object> configureCrossmatchTypesFormGenerator(
      HttpServletRequest request, HttpServletResponse response,
      Model model) {

    Map<String, Object> map = new HashMap<String, Object>();
    Map<String, Object> m = model.asMap();
    addAllCrossmatchTypesToModel(m);
    map.put("model", model);
    return map;
  }

  @RequestMapping(value="/packtypes", method=RequestMethod.GET)
  public  Map<String, Object> configureBloodBagTypesFormGenerator(
      HttpServletRequest request, HttpServletResponse response,
      Model model) {

    Map<String, Object> map = new HashMap<String, Object>();
    Map<String, Object> m = model.asMap();
    addAllBloodBagTypesToModel(m);
    map.put("model", model);
    return map;
  }

  /*
  * -- Does Nothing #209  
  
  @RequestMapping(value="/backupdata", method=RequestMethod.GET)
  public  Map<String, Object> backupDataFormGenerator(
      HttpServletRequest request, HttpServletResponse response,
      Model model) {

    Map<String, Object> map = new HashMap<String, Object>();
    Map<String, Object> m = model.asMap();
    map.put("model", model);
    return map;
  }
  */

  @RequestMapping(value="/backupdata", method=RequestMethod.GET)
  @PreAuthorize("hasRole('"+PermissionConstants.MANAGE_BACKUP_DATA+"')")
  public void backupData(
      HttpServletRequest request, HttpServletResponse response) {

    DateFormat dateFormat = new SimpleDateFormat("MM_dd_yyyy_HH_mm_ss");

    String currentDate = dateFormat.format(new Date());
    String fileName = "mysql_backup_" + currentDate;
    String fullFileName = servletContext.getRealPath("") + "/tmp/" + fileName + ".zip";

    LOGGER.debug("Writing backup to " + fullFileName);

    try {
      Properties prop = utilController.getV2VProperties();
      String mysqldumpPath = (String) prop.get("v2v.dbbackup.mysqldumppath");
      String username = (String) prop.get("v2v.dbbackup.username");
      String password = (String) prop.get("v2v.dbbackup.password");
      String dbname = (String) prop.get("v2v.dbbackup.dbname");

      LOGGER.debug(mysqldumpPath);
      LOGGER.debug(username);
      LOGGER.debug(password);
      LOGGER.debug(dbname);

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
      LOGGER.debug("Exit value: " + p.exitValue());
    } catch (IOException e) {
      LOGGER.debug(e.getMessage() + e.getStackTrace());
    } catch (InterruptedException e) {
      LOGGER.debug(e.getMessage() + e.getStackTrace());
    }
  }
  @RequestMapping(value="/donationtypes", method=RequestMethod.GET)
  @PreAuthorize("hasRole('"+PermissionConstants.MANAGE_DONATION_TYPES+"')")
  public  Map<String, Object> configureDonationTypesFormGenerator(
      HttpServletRequest request, HttpServletResponse response) {

    Map<String, Object> map = new HashMap<String, Object>();
    addAllDonationTypesToModel(map);
    return map;
  }

  private void addAllDonationTypesToModel(Map<String, Object> m) {
    m.put("allDonationTypes", donationTypesRepository.getAllDonationTypes());
  }

  private void addAllBloodBagTypesToModel(Map<String, Object> m) {
    m.put("allBloodBagTypes", bloodBagTypesRepository.getAllBloodBagTypes());
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

  @RequestMapping(value = "/tips", method = RequestMethod.POST)
  @PreAuthorize("hasRole('"+PermissionConstants.MANAGE_TIPS+"')") 
  public  Map<String, Object> configureTips(
      HttpServletRequest request, HttpServletResponse response,
      @RequestParam(value="params") String paramsAsJson) {
    Map<String, Object> map = new HashMap<String, Object>();
    LOGGER.debug(paramsAsJson);
    List<Tips> allTips = new ArrayList<Tips>();
    try {
      @SuppressWarnings("unchecked")
      Map<String, Object> params = new ObjectMapper().readValue(paramsAsJson, HashMap.class);
      
      for (String tipsKey : params.keySet()) {
        String tipsContent = (String) params.get(tipsKey);
        Tips tips = new Tips();
        tips.setTipsKey(tipsKey);
        tips.setTipsContent(tipsContent);
        allTips.add(tips);
      }
      tipsRepository.saveAllTips(allTips);
      LOGGER.debug(params);
    } catch (Exception ex) {
    	LOGGER.debug(ex.getMessage() + ex.getStackTrace());
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }

    addAllTipsToModel(map);
    return map;
  }

  @RequestMapping(value = "/requesttypes", method = RequestMethod.POST)
  @PreAuthorize("hasRole('"+PermissionConstants.MANAGE_REQUESTS+"')")
  public  Map<String, Object> configureRequestTypes(
      HttpServletRequest request, HttpServletResponse response,
      @RequestParam(value="params") String paramsAsJson) {
    Map<String, Object> map = new HashMap<String, Object>();
    LOGGER.debug(paramsAsJson);
    List<RequestType> allRequestTypes = new ArrayList<RequestType>();
    try {      
    	@SuppressWarnings("unchecked")
      Map<String, Object> params = new ObjectMapper().readValue(paramsAsJson, HashMap.class);
      for (String id : params.keySet()) {
        @SuppressWarnings("unchecked")
        Map<String, Object> paramValue = (Map<String, Object>) params.get(id);
				
          RequestType rt = new RequestType();

          rt.setRequestType((String) paramValue.get("requestType"));
          rt.setBulkTransfer((Boolean) paramValue.get("bulkTransfer"));
          rt.setId(Integer.parseInt(id));
          rt.setIsDeleted(false);
          allRequestTypes.add(rt);
      }
      requestTypesRepository.saveAllRequestTypes(allRequestTypes);
      LOGGER.debug(params);
    } catch (Exception ex) {
    	LOGGER.debug(ex.getMessage() + ex.getStackTrace());
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }

    addAllRequestTypesToModel(map);
    return map;
  }

  @RequestMapping(value = "/crossmatchtypes", method = RequestMethod.POST)
  @PreAuthorize("hasRole('"+PermissionConstants.MANAGE_CROSS_MATCH_TYPES+"')")
  public  Map<String, Object> configureCrossmatchTypes(
      HttpServletRequest request, HttpServletResponse response,
      @RequestParam(value="params") String paramsAsJson, Model model) {
    Map<String, Object> map = new HashMap<String, Object>();
    LOGGER.debug(paramsAsJson);
    List<CrossmatchType> allCrossmatchTypes = new ArrayList<CrossmatchType>();
    try {
      @SuppressWarnings("unchecked")
      Map<String, Object> params = new ObjectMapper().readValue(paramsAsJson, HashMap.class);
      for (String id : params.keySet()) {
        String crossmatchType = (String) params.get(id);
        CrossmatchType ct = new CrossmatchType();
        ct.setId(Integer.parseInt(id));
        ct.setCrossmatchType(crossmatchType);
        ct.setIsDeleted(false);
        allCrossmatchTypes.add(ct);
      }
      crossmatchTypesRepository.saveAllCrossmatchTypes(allCrossmatchTypes);
      LOGGER.debug(params);
    } catch (Exception ex) {
      LOGGER.debug(ex.getMessage() + ex.getStackTrace());
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }

    Map<String, Object> m = model.asMap();
    addAllCrossmatchTypesToModel(m);
    map.put("model", model);
    return map;
  }

    @RequestMapping(value = "/packtypes", method = RequestMethod.POST)
    @PreAuthorize("hasRole('" + PermissionConstants.MANAGE_BLOOD_BAG_TYPES + "')")
    public 
    Map<String, Object> configureBloodBagTypes(
            HttpServletRequest request, HttpServletResponse response,
            @RequestBody Map<String, String> params) {
        Map<String, Object> map = new HashMap<String, Object>();
        List<BloodBagType> allBloodBagTypes = new ArrayList<BloodBagType>();
            for (String id : params.keySet()) {
                String bloodBagType = (String) params.get(id);
                BloodBagType bt = new BloodBagType();
                bt.setId(Integer.parseInt(id));
                bt.setBloodBagType(bloodBagType);
                bt.setIsDeleted(false);
                allBloodBagTypes.add(bt);
            }
            bloodBagTypesRepository.saveAllBloodBagTypes(allBloodBagTypes);
            LOGGER.debug(params);
        addAllBloodBagTypesToModel(map);
        return map;
    }

  @RequestMapping(value = "/donationtypes", method = RequestMethod.POST)
  @PreAuthorize("hasRole('"+PermissionConstants.MANAGE_DONATION_TYPES+"')")
  public  Map<String, Object> configureDonationTypes(
      HttpServletRequest request, HttpServletResponse response,
    @RequestBody  Map<String,String> params) {
    Map<String, Object> map = new HashMap<String, Object>();
    List<DonationType> allDonationTypes = new ArrayList<DonationType>();
      for (String id : params.keySet()) {
        String donationType = (String) params.get(id);
        DonationType dt = new DonationType();
          dt.setId(Integer.parseInt(id));
      
        dt.setDonationType(donationType);
        dt.setIsDeleted(false);

        allDonationTypes.add(dt);
      }
      donationTypesRepository.saveAllDonationTypes(allDonationTypes);
      LOGGER.debug(params);
   
    addAllDonationTypesToModel(map);
    return map;
  }

/**
 * 
 
  @RequestMapping(value="/adminWelcomePageGenerator", method = RequestMethod.GET)
  @PreAuthorize("hasRole('"+PermissionConstants.VIEW_ADMIN_INFORMATION+"')")
  public  Map<String, Object> adminWelcomePageGenerator(HttpServletRequest request) {
    Map<String, Object> map = new HashMap<String, Object>();
    List<InetAddress> wirelessAddresses = getServerNetworkAddresses();
    List<String> serverAddresses = new ArrayList<String>();
    for (InetAddress addr : wirelessAddresses) {
      serverAddresses.add("http://" + addr.getHostAddress() + ":" + request.getServerPort() + "/v2v");
    }
    map.put("serverAddresses", serverAddresses);
    return map;
  }
  */

  List<InetAddress> getServerNetworkAddresses() {
    List<InetAddress> listOfServerAddresses = new ArrayList<InetAddress>();
    Enumeration<NetworkInterface> list;
    try {
        list = NetworkInterface.getNetworkInterfaces();

        while(list.hasMoreElements()) {
            NetworkInterface iface = (NetworkInterface) list.nextElement();

            if(iface == null) continue;

            if(!iface.isLoopback() && iface.isUp()) {
                LOGGER.debug("Found non-loopback, up interface:" + iface);

                Iterator<InterfaceAddress> it = iface.getInterfaceAddresses().iterator();
                while (it.hasNext()) {
                    InterfaceAddress address = (InterfaceAddress) it.next();

                    LOGGER.debug("Found address: " + address);

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
