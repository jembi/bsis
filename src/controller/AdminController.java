package controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import model.admin.FormField;
import model.compatibility.CrossmatchType;
import model.tips.Tips;
import org.apache.http.conn.util.InetAddressUtils;
import org.apache.log4j.Logger;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import repository.*;
import utils.PermissionConstants;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

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
  DonationTypeRepository donationTypesRepository;

  @Autowired
  CrossmatchTypeRepository crossmatchTypesRepository;

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
      reqUrl += "?" + queryString;
    }
    return reqUrl;
  }

  @RequestMapping(value = "/getform", method = RequestMethod.GET)
  public Map<String, Object> getFormToConfigure(HttpServletRequest request,
                                                @RequestParam(value = "formToConfigure", required = false) String formToConfigure) {
    Map<String, Object> map = new HashMap<>();

    map.put("formName", formToConfigure);
    map.put("formFields", formFieldRepository.getFormFields(formToConfigure));
    return map;
  }

  @RequestMapping(value = "/formfieldchange", method = RequestMethod.POST)
  @PreAuthorize("hasRole('" + PermissionConstants.MANAGE_FORMS + "')")
  public Map<String, ?>
  configureFormFieldChange(@RequestParam Map<String, String> params) {

    boolean success = true;
    String errMsg = "";

    try {
      LOGGER.debug(params);

      FormField ff = new FormField();
      String id = params.get("id");
      ff.setId(Long.parseLong(id));

      Boolean hidden = params.get("hidden").equals("true");
      ff.setHidden(hidden);

      Boolean isRequired = params.get("isRequired").equals("true");
      ff.setIsRequired(isRequired);

      Boolean autoGenerate = params.get("autoGenerate").equals("true");
      ff.setAutoGenerate(autoGenerate);

      Boolean useCurrentTime = params.get("useCurrentTime").equals("true");
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

    Map<String, Object> m = new HashMap<>();
    m.put("success", success);
    m.put("errMsg", errMsg);
    return m;
  }

  @RequestMapping(value = "/forms", method = RequestMethod.GET)
  @PreAuthorize("hasRole('" + PermissionConstants.MANAGE_FORMS + "')")
  public Map<String, Object> configureForms() {
    Map<String, Object> map = new HashMap<>();
//    map.put("model", map);
    return map;
  }

  @RequestMapping(value = "/createsampledata", method = RequestMethod.POST)
  @PreAuthorize("hasRole('" + PermissionConstants.MANAGE_DATA_SETUP + "')")
  public Map<String, ?> createSampleData(
          @RequestParam Map<String, String> params) {

    boolean success = true;
    String errMsg = "";
    try {
      Integer numDonors = Integer.parseInt(params.get("numDonors"));
      Integer numDonations = Integer.parseInt(params.get("numDonations"));
      Integer numComponents = Integer.parseInt(params.get("numComponents"));
      Integer numRequests = Integer.parseInt(params.get("numRequests"));

      createDataController.createDonors(numDonors);
      createDataController.createDonationsWithTestResults(numDonations);
      createDataController.createComponents(numComponents);
      createDataController.createRequests(numRequests);
    } catch (Exception ex) {
      LOGGER.debug(ex.getMessage() + ex.getStackTrace());
      success = false;
      errMsg = "Internal Server Error";
    }
    Map<String, Object> m = new HashMap<>();
    m.put("success", success);
    m.put("errMsg", errMsg);
    return m;
  }

  @RequestMapping(value = "/tipsform", method = RequestMethod.GET)
  @PreAuthorize("hasRole('" + PermissionConstants.MANAGE_TIPS + "')")
  public Map<String, Object> configureTipsFormGenerator() {

    Map<String, Object> map = new HashMap<>();
    addAllTipsToModel(map);
    return map;
  }

  @RequestMapping(value = "/labsetuppage", method = RequestMethod.GET)
  @PreAuthorize("hasRole('" + PermissionConstants.MANAGE_LAB_SETUP + "')")
  public Map<String, Object> labSetupFormGenerator() {

    Map<String, Object> map = new HashMap<>();
    map.put("labsetup", genericConfigRepository.getConfigProperties("labsetup"));
    return map;
  }

  @SuppressWarnings("unchecked")
  @PreAuthorize("hasRole('" + PermissionConstants.MANAGE_LAB_SETUP + "')")
  @RequestMapping(value = "/updateLabSetup", method = RequestMethod.POST)
  public Map<String, Object> updateLabSetup(HttpServletRequest request,
                                            @RequestParam(value = "labSetupParams") String params) {

    ObjectMapper mapper = new ObjectMapper();
    Map<String, String> paramsMap = null;
    try {
      paramsMap = mapper.readValue(params, HashMap.class);
    } catch (IOException e) {
      // TODO Auto-generated catch block
      LOGGER.debug(e.getMessage() + e.getStackTrace());
    }
    LOGGER.debug("here");
    LOGGER.debug(params);
    labSetupRepository.updateLabSetup(paramsMap);
    Map<String, Object> m = new HashMap<>();
    m.put("success", true);
    return m;
  }

  @RequestMapping(value = "/crossmatchtypes", method = RequestMethod.GET)
  @PreAuthorize("hasRole('" + PermissionConstants.MANAGE_CROSS_MATCH_TYPES + "')")
  public Map<String, Object> configureCrossmatchTypesFormGenerator() {

    Map<String, Object> map = new HashMap<>();
    addAllCrossmatchTypesToModel(map);
    return map;
  }

  @RequestMapping(value = "/backupdata", method = RequestMethod.GET)
  @PreAuthorize("hasRole('" + PermissionConstants.MANAGE_BACKUP_DATA + "')")
  public void backupData(
          HttpServletRequest request, HttpServletResponse response) {

    DateFormat dateFormat = new SimpleDateFormat("MM_dd_yyyy_HH_mm_ss");

    String currentDate = dateFormat.format(new Date());
    String fileName = "mysql_backup_" + currentDate;
    String fullFileName = servletContext.getRealPath("") + "/tmp/" + fileName + ".zip";

    LOGGER.debug("Writing backup to " + fullFileName);

    try {
      Properties prop = utilController.getDatabaseProperties();
      String mysqldumpPath = (String) prop.get("dbbackup.mysqldumppath");
      String username = (String) prop.get("dbbackup.username");
      String password = (String) prop.get("dbbackup.password");
      String dbname = (String) prop.get("dbbackup.dbname");

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
    } catch (IOException | InterruptedException e) {
      LOGGER.debug(e.getMessage() + e.getStackTrace());
    }
  }


  private void addAllCrossmatchTypesToModel(Map<String, Object> m) {
    m.put("allCrossmatchTypes", crossmatchTypesRepository.getAllCrossmatchTypes());
  }

  private void addAllTipsToModel(Map<String, Object> m) {
    m.put("allTips", tipsRepository.getAllTips());
  }

  @RequestMapping(value = "/tips", method = RequestMethod.POST)
  @PreAuthorize("hasRole('" + PermissionConstants.MANAGE_TIPS + "')")
  public Map<String, Object> configureTips(
          HttpServletRequest request, HttpServletResponse response,
          @RequestParam(value = "params") String paramsAsJson) {
    Map<String, Object> map = new HashMap<>();
    LOGGER.debug(paramsAsJson);
    List<Tips> allTips = new ArrayList<>();
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

  @RequestMapping(value = "/crossmatchtypes", method = RequestMethod.POST)
  @PreAuthorize("hasRole('" + PermissionConstants.MANAGE_CROSS_MATCH_TYPES + "')")
  public Map<String, Object> configureCrossmatchTypes(
          HttpServletRequest request, HttpServletResponse response,
          @RequestParam(value = "params") String paramsAsJson) {
    Map<String, Object> map = new HashMap<>();
    LOGGER.debug(paramsAsJson);
    List<CrossmatchType> allCrossmatchTypes = new ArrayList<>();
    try {
      @SuppressWarnings("unchecked")
      Map<String, Object> params = new ObjectMapper().readValue(paramsAsJson, HashMap.class);
      for (String id : params.keySet()) {
        String crossmatchType = (String) params.get(id);
        CrossmatchType ct = new CrossmatchType();
        ct.setId(Long.parseLong(id));
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

    addAllCrossmatchTypesToModel(map);
    return map;
  }


  List<InetAddress> getServerNetworkAddresses() {
    List<InetAddress> listOfServerAddresses = new ArrayList<>();
    Enumeration<NetworkInterface> list;
    try {
      list = NetworkInterface.getNetworkInterfaces();

      while (list.hasMoreElements()) {
        NetworkInterface iface = list.nextElement();

        if (iface == null) continue;

        if (!iface.isLoopback() && iface.isUp()) {
          LOGGER.debug("Found non-loopback, up interface:" + iface);

          for (InterfaceAddress address : iface.getInterfaceAddresses()) {
            LOGGER.debug("Found address: " + address);

            if (address == null) continue;
            if (InetAddressUtils.isIPv4Address(address.getAddress().getHostAddress()))
              listOfServerAddresses.add(address.getAddress());
          }
        }
      }
    } catch (SocketException ex) {
      return new ArrayList<>();
    }
    return listOfServerAddresses;
  }
}

