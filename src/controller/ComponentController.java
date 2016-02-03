package controller;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import model.component.Component;
import model.component.ComponentStatus;
import model.componentmovement.ComponentStatusChange;
import model.componentmovement.ComponentStatusChangeReason;
import model.componentmovement.ComponentStatusChangeReasonCategory;
import model.componenttype.ComponentType;
import model.componenttype.ComponentTypeCombination;
import model.componenttype.ComponentTypeTimeUnits;
import model.donation.Donation;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import repository.ComponentRepository;
import repository.ComponentStatusChangeReasonRepository;
import repository.ComponentTypeRepository;
import repository.DonationRepository;
import utils.CustomDateFormatter;
import utils.PermissionConstants;
import viewmodel.ComponentStatusChangeViewModel;
import viewmodel.ComponentTypeCombinationViewModel;
import viewmodel.ComponentTypeViewModel;
import viewmodel.ComponentViewModel;
import backingform.ComponentCombinationBackingForm;
import backingform.RecordComponentBackingForm;
import backingform.validator.ComponentCombinationBackingFormValidator;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("components")
public class ComponentController {

  @Autowired
  private ComponentRepository componentRepository;

  @Autowired
  private DonationRepository donationRepository;

  @Autowired
  private ComponentStatusChangeReasonRepository componentStatusChangeReasonRepository;

  @Autowired
  private ComponentTypeRepository componentTypeRepository;

  @Autowired
  private ComponentCombinationBackingFormValidator componentCombinationBackingFormValidator;

  public ComponentController() {
  }

  @InitBinder("componentCombinationForm")
  protected void initBinder(WebDataBinder binder) {
    binder.setValidator(componentCombinationBackingFormValidator);
  }

  public static String getUrl(HttpServletRequest req) {
    String reqUrl = req.getRequestURL().toString();
    String queryString = req.getQueryString();   // d=789
    if (queryString != null) {
      reqUrl += "?" + queryString;
    }
    return reqUrl;
  }

  public static String getNextPageUrl(HttpServletRequest req) {
    String reqUrl = req.getRequestURL().toString().replaceFirst("findComponent.html", "search.html");
    String queryString = req.getQueryString();   // d=789
    if (queryString != null) {
      reqUrl += "?" + queryString;
    }
    return reqUrl;
  }

  @RequestMapping(value = "{id}", method = RequestMethod.GET)
  @PreAuthorize("hasRole('" + PermissionConstants.VIEW_COMPONENT + "')")
  public Map<String, Object> componentSummaryGenerator(HttpServletRequest request,
                                                       @PathVariable Long id) {

    Map<String, Object> map = new HashMap<String, Object>();
    Component component = componentRepository.findComponentById(id);

    ComponentViewModel componentViewModel = getComponentViewModels(Arrays.asList(component)).get(0);
    addEditSelectorOptions(map);
    map.put("component", componentViewModel);
    map.put("componentStatusChangeReasons",
        componentStatusChangeReasonRepository.getAllComponentStatusChangeReasonsAsMap());
    return map;
  }

  @RequestMapping(value = "/form", method = RequestMethod.GET)
  @PreAuthorize("hasRole('" + PermissionConstants.VIEW_COMPONENT_INFORMATION + "')")
  public Map<String, Object> findComponentFormGenerator(HttpServletRequest request) {
    Map<String, Object> map = new HashMap<String, Object>();
    addEditSelectorOptions(map);
    List<ComponentStatusChangeReason> statusChangeReasons =
        componentStatusChangeReasonRepository.getComponentStatusChangeReasons(ComponentStatusChangeReasonCategory.RETURNED);
    map.put("returnReasons", statusChangeReasons);
    statusChangeReasons =
        componentStatusChangeReasonRepository.getComponentStatusChangeReasons(ComponentStatusChangeReasonCategory.DISCARDED);
    map.put("discardReasons", statusChangeReasons);
    map.put("findComponentByPackNumberForm", new RecordComponentBackingForm());
    return map;
  }

  @RequestMapping(value = "/combination/form", method = RequestMethod.GET)
  @PreAuthorize("hasRole('" + PermissionConstants.MANAGE_COMPONENT_COMBINATIONS + "')")
  public Map<String, Object> addComponentCombinationFormGenerator() {

    ComponentCombinationBackingForm form = new ComponentCombinationBackingForm();

    Map<String, Object> map = new HashMap<String, Object>();
    map.put("addComponentCombinationForm", form);

    addOptionsForAddComponentsCombinationForm(map);
    addEditSelectorOptions(map);

    return map;
  }

  @RequestMapping(value = "/combination", method = RequestMethod.POST)
  @PreAuthorize("hasRole('" + PermissionConstants.ADD_COMPONENT + "')")
  public ResponseEntity<Map<String, Object>> addComponentCombination(
      @Valid @RequestBody ComponentCombinationBackingForm componentCombinationForm) throws ParseException {

    Map<String, Object> map = new HashMap<String, Object>();
    HttpStatus httpStatus = HttpStatus.CREATED;
    addEditSelectorOptions(map);

    List<Component> savedComponents = null;
    savedComponents = componentRepository.addComponentCombination(componentCombinationForm);
    map.put("hasErrors", false);
    componentCombinationForm = new ComponentCombinationBackingForm();

    // at least one  should be created, all s should have the same donation number
    map.put("donationIdentificationNumber", savedComponents.get(0).getDonationIdentificationNumber());
    map.put("createdComponents", getComponentViewModels(savedComponents));
    List<Component> allComponentsForDonation = componentRepository.findComponentsByDonationIdentificationNumber(savedComponents.get(0).getDonationIdentificationNumber());
    map.put("allComponentsForDonation", getComponentViewModels(allComponentsForDonation));
    map.put("addAnotherComponentUrl", "addComponentCombinationFormGenerator.html");

    return new ResponseEntity<Map<String, Object>>(map, httpStatus);
  }

  private ComponentViewModel getComponentViewModel(Component component) {
    return new ComponentViewModel(component);
  }

  @SuppressWarnings("unchecked")
  @RequestMapping(value = "/search", method = RequestMethod.GET)
  @PreAuthorize("hasRole('" + PermissionConstants.VIEW_COMPONENT + "')")
  public Map<String, Object> findComponentPagination(HttpServletRequest request,
                                                     @RequestParam(value = "componentNumber", required = false, defaultValue = "") String componentNumber,
                                                     @RequestParam(value = "donationIdentificationNumber", required = false, defaultValue = "") String donationIdentificationNumber,
                                                     @RequestParam(value = "componentTypes", required = false, defaultValue = "") List<Long> componentTypeIds,
                                                     @RequestParam(value = "status", required = false, defaultValue = "") List<String> status,
                                                     @RequestParam(value = "donationDateFrom", required = false, defaultValue = "") String donationDateFrom,
                                                     @RequestParam(value = "donationDateTo", required = false, defaultValue = "") String donationDateTo) throws ParseException {


    Map<String, Object> map = new HashMap<String, Object>();

    Map<String, Object> pagingParams = new HashMap<String, Object>();
    pagingParams.put("sortColumn", "id");
    //pagingParams.put("start", "0");
    //pagingParams.put("length", "10");
    pagingParams.put("sortDirection", "asc");

    List<Component> results = new ArrayList<Component>();
    Date dateFrom = null;
    Date dateTo = null;

    if (!donationDateFrom.equals("")) {
      dateFrom = CustomDateFormatter.getDateFromString(donationDateFrom);
    }
    if (!donationDateTo.equals("")) {
      dateTo = CustomDateFormatter.getDateFromString(donationDateTo);
    }

    results = componentRepository.findAnyComponent(
        donationIdentificationNumber, componentTypeIds, statusStringToComponentStatus(status),
        dateFrom, dateTo, pagingParams);

    List<ComponentViewModel> components = new ArrayList<ComponentViewModel>();

    if (results != null) {
      for (Component component : results) {
        ComponentViewModel componentViewModel = getComponentViewModel(component);
        components.add(componentViewModel);
      }
    }

    map.put("components", components);
    return map;
  }

  public static List<ComponentStatusChangeViewModel> getComponentStatusChangeViewModels(List<ComponentStatusChange> componentStatusChanges) {
    if (componentStatusChanges == null)
      return Arrays.asList(new ComponentStatusChangeViewModel[0]);
    List<ComponentStatusChangeViewModel> componentStatusChangeViewModels = new ArrayList<ComponentStatusChangeViewModel>();
    for (ComponentStatusChange componentStatusChange : componentStatusChanges) {
      componentStatusChangeViewModels.add(new ComponentStatusChangeViewModel(componentStatusChange));
    }
    return componentStatusChangeViewModels;
  }

  public static List<ComponentViewModel> getComponentViewModels(
      List<Component> components) {
    if (components == null)
      return Arrays.asList(new ComponentViewModel[0]);
    List<ComponentViewModel> componentViewModels = new ArrayList<ComponentViewModel>();
    for (Component component : components) {
      componentViewModels.add(new ComponentViewModel(component));
    }
    return componentViewModels;
  }

  public static List<ComponentTypeViewModel> getComponentTypeViewModels(
      List<ComponentType> componentTypes) {
    if (componentTypes == null)
      return Arrays.asList(new ComponentTypeViewModel[0]);
    List<ComponentTypeViewModel> componentTypeViewModels = new ArrayList<ComponentTypeViewModel>();
    for (ComponentType componentType : componentTypes) {
      componentTypeViewModels.add(new ComponentTypeViewModel(componentType));
    }
    return componentTypeViewModels;
  }

  @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
  @PreAuthorize("hasRole('" + PermissionConstants.VOID_COMPONENT + "')")
  public HttpStatus deleteComponent(
      @PathVariable Long id) {

    componentRepository.deleteComponent(id);
    return HttpStatus.NO_CONTENT;
  }

  @RequestMapping(value = "{id}/discard", method = RequestMethod.PUT)
  @PreAuthorize("hasRole('" + PermissionConstants.DISCARD_COMPONENT + "')")
  public ResponseEntity discardComponent(
      @PathVariable Long id,
      @RequestParam(value = "discardReasonId") Long discardReasonId,
      @RequestParam(value = "discardReasonText", required = false) String discardReasonText) {

    ComponentStatusChangeReason statusChangeReason = new ComponentStatusChangeReason();
    statusChangeReason.setId(discardReasonId);
    Donation donation = componentRepository.findComponentById(id).getDonation();
    componentRepository.discardComponent(id, statusChangeReason, discardReasonText);

    Map<String, Object> map = new HashMap<String, Object>();
    Map<String, Object> pagingParams = new HashMap<String, Object>();
    pagingParams.put("sortColumn", "id");
    pagingParams.put("sortDirection", "asc");
    List<Component> results = new ArrayList<Component>();
    List<ComponentStatus> statusList = Arrays.asList(ComponentStatus.values());

    results = componentRepository.findComponentByDonationIdentificationNumber(
        donation.getDonationIdentificationNumber(), statusList,
        pagingParams);

    List<ComponentViewModel> components = new ArrayList<ComponentViewModel>();

    if (results != null) {
      for (Component component : results) {
        ComponentViewModel componentViewModel = getComponentViewModel(component);
        components.add(componentViewModel);
      }
    }

    map.put("components", components);

    return new ResponseEntity<Map<String, Object>>(map, HttpStatus.OK);
  }

  @RequestMapping(value = "{id}/split", method = RequestMethod.PUT)
  @PreAuthorize("hasRole('" + PermissionConstants.ADD_COMPONENT + "')")
  public ResponseEntity discardComponent(
      @PathVariable Long id,
      @RequestParam("numComponentsAfterSplitting") Integer numComponentsAfterSplitting) {

    boolean success = true;
    success = componentRepository.splitComponent(id, numComponentsAfterSplitting);
    if (!success)
      return new ResponseEntity(HttpStatus.BAD_GATEWAY);

    return new ResponseEntity(HttpStatus.NO_CONTENT);
  }

  @RequestMapping(value = "{id}/history", method = RequestMethod.GET)
  @PreAuthorize("hasRole('" + PermissionConstants.VIEW_COMPONENT + "')")
  public Map<String, Object> viewComponentHistory(
      @PathVariable Long id) {

    Map<String, Object> map = new HashMap<String, Object>();
    Component component = componentRepository.findComponentById(id);
    ComponentViewModel componentViewModel = getComponentViewModel(component);
    map.put("component", componentViewModel);
    List<ComponentStatusChange> componentStatusChangeList = componentRepository.getComponentStatusChanges(component);
    List<ComponentStatusChangeViewModel> componentStatusChanges = getComponentStatusChangeViewModels(componentStatusChangeList);

    map.put("componentStatusChanges", componentStatusChanges);
    return map;
  }

  @RequestMapping(value = "{id}/return", method = RequestMethod.PUT)
  @PreAuthorize("hasRole('" + PermissionConstants.DISCARD_COMPONENT + "')")
  public HttpStatus returnComponent(
      @PathVariable Long id,
      @RequestParam("returnReasonId") Long returnReasonId,
      @RequestParam("returnReasonText") String returnReasonText) {

    ComponentStatusChangeReason statusChangeReason = new ComponentStatusChangeReason();
    statusChangeReason.setId(returnReasonId);
    componentRepository.returnComponent(id, statusChangeReason, returnReasonText);

    return HttpStatus.NO_CONTENT;
  }

  @SuppressWarnings("unchecked")
  @RequestMapping(value = "/donations/{donationNumber}", method = RequestMethod.GET)
  @PreAuthorize("hasRole('" + PermissionConstants.VIEW_COMPONENT + "')")
  public Map<String, Object> findComponentByPackNumberPagination(HttpServletRequest request, @PathVariable String donationNumber) {

    Map<String, Object> map = new HashMap<String, Object>();

    List<Component> components = Arrays.asList(new Component[0]);

    Map<String, Object> pagingParams = new HashMap<String, Object>();
    pagingParams.put("sortColumn", "id");
    //pagingParams.put("start", "0");
    //pagingParams.put("length", "10");
    pagingParams.put("sortDirection", "asc");

    List<Component> results = new ArrayList<Component>();
    List<ComponentStatus> status = Arrays.asList(ComponentStatus.values());

    results = componentRepository.findComponentByDonationIdentificationNumber(
        donationNumber, status,
        pagingParams);

    List<ComponentViewModel> componentViewModels = new ArrayList<ComponentViewModel>();

    if (results != null) {
      for (Component component : results) {
        ComponentViewModel componentViewModel = getComponentViewModel(component);
        componentViewModels.add(componentViewModel);
      }
    }

    map.put("components", componentViewModels);
    return map;
  }

  @RequestMapping(value = "/combinations", method = RequestMethod.GET)
  @PreAuthorize("hasRole('" + PermissionConstants.VIEW_COMPONENT + "')")
  public Map<String, Object> getComponentTypeCombinations() {
    Map<String, Object> map = new HashMap<String, Object>();
    List<ComponentTypeCombination> allComponentTypeCombinationsIncludeDeleted = componentTypeRepository.getAllComponentTypeCombinationsIncludeDeleted();
    map.put("combinations", getComponentTypeCombinationViewModels(allComponentTypeCombinationsIncludeDeleted));
    return map;
  }

  @RequestMapping(value = "/recordcombinations", method = RequestMethod.POST)
  @PreAuthorize("hasRole('" + PermissionConstants.ADD_COMPONENT + "')")
  public ResponseEntity<Map<String, Object>> recordNewComponentCombinations(
      @RequestBody RecordComponentBackingForm recordComponentForm) throws ParseException {

    Component parentComponent = componentRepository.findComponentById(Long.valueOf(recordComponentForm.getParentComponentId()));
    Donation donation = parentComponent.getDonation();
    String donationIdentificationNumber = donation.getDonationIdentificationNumber();
    ComponentStatus parentStatus = parentComponent.getStatus();
    long componentId = Long.valueOf(recordComponentForm.getParentComponentId());


    // map of new components, storing component type and num. of units
    Map<ComponentType, Integer> newComponents = new HashMap<ComponentType, Integer>();

    // iterate over components in combination, adding them to the new components map, along with the num. of units of each component
    for (ComponentType pt : recordComponentForm.getComponentTypeCombination().getComponentTypes()) {
      boolean check = false;
      for (ComponentType ptm : newComponents.keySet()) {
        if (pt.getId() == ptm.getId()) {
          Integer count = newComponents.get(ptm) + 1;
          newComponents.put(ptm, count);
          check = true;
          break;
        }
      }
      if (!check) {
        newComponents.put(pt, 1);
      }
    }

    // If the parent is unsafe then set new components to unsafe as well
    ComponentStatus initialComponentStatus = parentStatus == ComponentStatus.UNSAFE ?
        ComponentStatus.UNSAFE : ComponentStatus.QUARANTINED;

    for (ComponentType pt : newComponents.keySet()) {

      String componentTypeCode = pt.getComponentTypeNameShort();
      int noOfUnits = newComponents.get(pt);
      String createdPackNumber = donationIdentificationNumber + "-" + componentTypeCode;

      // Add New component
      if (!parentStatus.equals(ComponentStatus.PROCESSED) && !parentStatus.equals(ComponentStatus.DISCARDED)) {

        for (int i = 1; i <= noOfUnits; i++) {
          Component component = new Component();
          component.setIsDeleted(false);

          // if there is more than one unit of the component, append unit number suffix
          if (noOfUnits > 1) {
            component.setComponentIdentificationNumber(createdPackNumber + "-0" + i);
          } else {
            component.setComponentIdentificationNumber(createdPackNumber);
          }
          component.setComponentType(pt);
          component.setDonation(donation);
          component.setParentComponent(parentComponent);
          component.setStatus(initialComponentStatus);
          component.setCreatedOn(donation.getDonationDate());

          Calendar cal = Calendar.getInstance();
          Date createdOn = cal.getTime();
          cal.setTime(component.getCreatedOn());

          //set component expiry date
          if (pt.getExpiresAfterUnits() == ComponentTypeTimeUnits.DAYS)
            cal.add(Calendar.DAY_OF_YEAR, pt.getExpiresAfter());
          else if (pt.getExpiresAfterUnits() == ComponentTypeTimeUnits.HOURS)
            cal.add(Calendar.HOUR, pt.getExpiresAfter());
          else if (pt.getExpiresAfterUnits() == ComponentTypeTimeUnits.YEARS)
            cal.add(Calendar.YEAR, pt.getExpiresAfter());

          Date expiresOn = cal.getTime();
          component.setCreatedOn(createdOn);
          component.setExpiresOn(expiresOn);

          componentRepository.addComponent(component);

          // Set source component status to PROCESSED
          componentRepository.setComponentStatusToProcessed(componentId);
        }
      }
    }

    Map<String, Object> map = new HashMap<String, Object>();
    Map<String, Object> pagingParams = new HashMap<String, Object>();
    pagingParams.put("sortColumn", "id");
    pagingParams.put("sortDirection", "asc");
    List<Component> results = new ArrayList<Component>();
    List<ComponentStatus> statusList = Arrays.asList(ComponentStatus.values());

    results = componentRepository.findComponentByDonationIdentificationNumber(
        donation.getDonationIdentificationNumber(), statusList,
        pagingParams);

    List<ComponentViewModel> components = new ArrayList<ComponentViewModel>();

    if (results != null) {
      for (Component component : results) {
        ComponentViewModel componentViewModel = getComponentViewModel(component);
        components.add(componentViewModel);
      }
    }

    map.put("components", components);

    return new ResponseEntity<Map<String, Object>>(map, HttpStatus.CREATED);
  }

  @RequestMapping(value = "/record/form", method = RequestMethod.GET)
  @PreAuthorize("hasRole('" + PermissionConstants.VIEW_COMPONENT + "')")
  public Map<String, Object> getRecordNewComponents(HttpServletRequest request,
                                                    @RequestParam(value = "componentTypeNames") List<String> componentTypes,
                                                    @RequestParam(value = "donationIdentificationNumber") String donationIdentificationNumber) {

    ComponentType componentType = null;
    if (componentTypes != null) {
      String componentTypeName = componentTypes.get(componentTypes.size() - 1);
      componentType = componentRepository.findComponentTypeByComponentTypeName(componentTypeName);
    }
    List<Component> components = Arrays.asList(new Component[0]);

    Map<String, Object> map = new HashMap<String, Object>();
    map.put("allComponents", getComponentViewModels(components));
    map.put("nextPageUrl", getNextPageUrlForNewRecordComponent(request, donationIdentificationNumber));

    if (componentTypes != null) {
      addEditSelectorOptionsForNewRecordByList(map, componentType);
    } else {
      addEditSelectorOptionsForNewRecord(map);
    }

    return map;
  }

  private void addOptionsForAddComponentsCombinationForm(Map<String, Object> m) {
    m.put("componentTypes", componentTypeRepository.getAllComponentTypes());

    List<ComponentTypeCombination> componentTypeCombinations = componentTypeRepository.getAllComponentTypeCombinations();
    m.put("componentTypeCombinations", componentTypeCombinations);

    ObjectMapper mapper = new ObjectMapper();
    Map<Long, String> componentTypeCombinationsMap = new HashMap<Long, String>();
    for (ComponentTypeCombination componentTypeCombination : componentTypeCombinations) {
      Map<String, String> componentExpiryIntervals = new HashMap<String, String>();
      for (ComponentType componentType : componentTypeCombination.getComponentTypes()) {
        Integer expiryIntervalMinutes = componentType.getExpiryIntervalMinutes();
        componentExpiryIntervals.put(componentType.getId().toString(), expiryIntervalMinutes.toString());
      }

      try {
        componentTypeCombinationsMap.put(componentTypeCombination.getId(), mapper.writeValueAsString(componentExpiryIntervals));
      } catch (JsonGenerationException e) {
        e.printStackTrace();
      } catch (JsonMappingException e) {
        e.printStackTrace();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    m.put("componentTypeCombinationsMap", componentTypeCombinationsMap);
  }

  public static String getNextPageUrlForRecordComponent(HttpServletRequest req) {
    String reqUrl = req.getRequestURL().toString().replaceFirst("findComponentByPackNumber.html", "findComponentByPackNumberPagination.html");
    String queryString = req.getQueryString();   // d=789
    if (queryString != null) {
      reqUrl += "?" + queryString;
    }
    return reqUrl;
  }

  private void addEditSelectorOptions(Map<String, Object> m) {
    m.put("componentTypes", getComponentTypeViewModels(componentTypeRepository.getAllComponentTypes()));
  }

  private void addEditSelectorOptionsForNewRecordByList(Map<String, Object> m, ComponentType componentType) {
    m.put("componentTypes", getComponentTypeViewModels(componentTypeRepository.getComponentTypeByIdList(componentType.getId())));
  }

  private void addEditSelectorOptionsForNewRecord(Map<String, Object> m) {
    m.put("componentTypes", getComponentTypeViewModels(componentTypeRepository.getAllParentComponentTypes()));
  }

  public static String getUrlForNewComponent(HttpServletRequest req, String qString) {
    String reqUrl = req.getRequestURL().toString();
    String queryString[] = qString.split("-");
    if (queryString != null) {
      reqUrl += "?donationIdentificationNumber=" + queryString[0];
    }
    return reqUrl;
  }

  public static String getNextPageUrlForNewRecordComponent(HttpServletRequest req, String qString) {
    String reqUrl = "";
    if (req.getRequestURI().contains("recordnewcomponents")) {
      reqUrl = req.getRequestURL().toString().replaceFirst("recordNewComponents.html", "findComponentByPackNumberPagination.html");
    } else {
      reqUrl = req.getRequestURL().toString().replaceFirst("getRecordNewComponents.html", "findComponentByPackNumberPagination.html");
    }
    String queryString[] = qString.split("-");
    if (queryString != null) {
      reqUrl += "?donationIdentificationNumber=" + queryString[0];
    }
    return reqUrl;
  }

  /**
   * Datatables on the client side expects a json response for rendering data from the server in
   * jquery datatables. Remember of columns is important and should match the column headings in
   * recordComponentTable.jsp.
   */
  private Map<String, Object> generateRecordComponentTablesMap(List<Component> components, Long totalRecords, Map<String, Map<String, Object>> formFields) {
    Map<String, Object> componentsMap = new HashMap<String, Object>();
    ArrayList<Object> componentList = new ArrayList<Object>();
    for (ComponentViewModel component : getComponentViewModels(components)) {
      List<Object> row = new ArrayList<Object>();

      row.add(component.getId().toString());
      row.add(component.getDonation().getId());
      for (String property : Arrays.asList("componentType", "donationIdentificationNumber", "createdOn", "expiresOn", "status", "createdBy")) {
        if (formFields.containsKey(property)) {
          Map<String, Object> properties = (Map<String, Object>) formFields.get(property);
          if (properties.get("hidden").equals(false)) {
            String propertyValue = property;
            try {
              propertyValue = BeanUtils.getProperty(component, property);
            } catch (IllegalAccessException e) {
              e.printStackTrace();
            } catch (InvocationTargetException e) {
              e.printStackTrace();
            } catch (NoSuchMethodException e) {
              e.printStackTrace();
            }
            if (property.equals("componentType") &&
                StringUtils.isNotBlank(component.getSubdivisionCode())) {
              propertyValue = propertyValue + " (" + component.getSubdivisionCode() + ")";
            }
            row.add(propertyValue.toString());
          }
        }
      }

      componentList.add(row);
    }
    componentsMap.put("aaData", componentList);
    componentsMap.put("iTotalRecords", totalRecords);
    componentsMap.put("iTotalDisplayRecords", totalRecords);
    return componentsMap;
  }

  private List<ComponentStatus> statusStringToComponentStatus(List<String> statusList) {
    List<ComponentStatus> componentStatusList = new ArrayList<ComponentStatus>();
    if (statusList != null) {
      for (String status : statusList) {
        componentStatusList.add(ComponentStatus.lookup(status));
      }
    }
    return componentStatusList;
  }

  public List<ComponentTypeCombinationViewModel>
  getComponentTypeCombinationViewModels(List<ComponentTypeCombination> componentTypeCombinations) {
    List<ComponentTypeCombinationViewModel> componentTypeCombinationViewModels
        = new ArrayList<ComponentTypeCombinationViewModel>();
    for (ComponentTypeCombination componentTypeCombination : componentTypeCombinations)
      componentTypeCombinationViewModels.add(new ComponentTypeCombinationViewModel(componentTypeCombination));

    return componentTypeCombinationViewModels;

  }

}
