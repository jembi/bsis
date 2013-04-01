package controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityExistsException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import model.admin.ConfigPropertyConstants;
import model.collectedsample.CollectedSample;
import model.worksheet.FindWorksheetBackingForm;
import model.worksheet.Worksheet;
import model.worksheet.WorksheetBackingForm;
import model.worksheet.WorksheetBackingFormValidator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import repository.CollectedSampleRepository;
import repository.GenericConfigRepository;
import repository.WorksheetRepository;
import repository.WorksheetTypeRepository;
import viewmodel.WorksheetViewModel;

@Controller
public class WorksheetController {

  @Autowired
  private CollectedSampleRepository collectedSampleRepository;

  @Autowired
  private WorksheetTypeRepository worksheetTypeRepository;

  @Autowired
  private WorksheetRepository worksheetRepository;

  @Autowired
  private GenericConfigRepository genericConfigRepository;

  @Autowired
  private UtilController utilController;

  public WorksheetController() {
  }

  @InitBinder
  protected void initBinder(WebDataBinder binder) {
    binder.setValidator(new WorksheetBackingFormValidator(binder.getValidator(),
                        utilController));
  }

  public static String getUrl(HttpServletRequest req) {
    String reqUrl = req.getRequestURL().toString();
    String queryString = req.getQueryString();   // d=789
    if (queryString != null) {
        reqUrl += "?"+queryString;
    }
    return reqUrl;
  }

  @RequestMapping(value = "/addWorksheetFormGenerator", method = RequestMethod.GET)
  public ModelAndView addWorksheetFormGenerator(HttpServletRequest request,
      Model model) {

    WorksheetBackingForm form = new WorksheetBackingForm();

    ModelAndView mv = new ModelAndView("worksheets/addWorksheetForm");
    mv.addObject("requestUrl", getUrl(request));
    mv.addObject("firstTimeRender", true);
    mv.addObject("addWorksheetForm", form);
    mv.addObject("refreshUrl", getUrl(request));
    addEditSelectorOptions(mv.getModelMap());
    Map<String, Object> formFields = utilController.getFormFieldsForForm("worksheet");
    // to ensure custom field names are displayed in the form
    mv.addObject("worksheetFields", formFields);
    return mv;
  }

  private void addEditSelectorOptions(Map<String, Object> m) {
    m.put("worksheetTypes", worksheetTypeRepository.getAllWorksheetTypes());
  }

  @RequestMapping(value = "/addWorksheet", method = RequestMethod.POST)
  public ModelAndView addWorksheet(
      HttpServletRequest request,
      HttpServletResponse response,
      @ModelAttribute("addWorksheetForm") @Valid WorksheetBackingForm form,
      BindingResult result, Model model) {

    ModelAndView mv = new ModelAndView();
    boolean success = false;

    addEditSelectorOptions(mv.getModelMap());
    Map<String, Object> formFields = utilController.getFormFieldsForForm("worksheet");
    mv.addObject("worksheetFields", formFields);

    Worksheet savedWorksheet = null;
    if (result.hasErrors()) {
      mv.addObject("hasErrors", true);
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      success = false;
    } else {
      try {
        Worksheet worksheet = form.getWorksheet();
        worksheet.setIsDeleted(false);
        savedWorksheet = worksheetRepository.addWorksheet(worksheet);
        mv.addObject("hasErrors", false);
        success = true;
        form = new WorksheetBackingForm();
      } catch (EntityExistsException ex) {
        ex.printStackTrace();
        success = false;
      } catch (Exception ex) {
        ex.printStackTrace();
        success = false;
      }
    }

    if (success) {
      mv.addObject("worksheetId", savedWorksheet.getId());
      mv.addObject("worksheet", getWorksheetViewModel(savedWorksheet));
      mv.addObject("addAnotherWorksheetUrl", "addWorksheetFormGenerator.html");
      mv.setViewName("worksheets/addWorksheetSuccess");
    } else {
      mv.addObject("errorMessage", "Error creating worksheet. Please fix the errors noted below.");
      mv.addObject("firstTimeRender", false);
      mv.addObject("addWorksheetForm", form);
      mv.addObject("refreshUrl", "addWorksheetFormGenerator.html");
      mv.setViewName("worksheets/addWorksheetError");
    }

    mv.addObject("success", success);
    return mv;
  }

  private Object getWorksheetViewModel(Worksheet savedWorksheet) {
    return new WorksheetViewModel(savedWorksheet);
  }

  @RequestMapping(value="/addCollectionsToWorksheet", method=RequestMethod.POST)
  public ModelAndView addCollectionsToBloodTypingPlate(HttpServletRequest request,
          HttpServletResponse response,
          @RequestParam(value="worksheetId") String worksheetId,
          @RequestParam(value="collectionNumbers[]") List<String> collectionNumbers) {

    // here the key of the map corresponds to the index of the
    // collection number in the parameter collectionNumbers
    // corresponding to the collected sample in the value of the map
    List<CollectedSample> collections = collectedSampleRepository.verifyCollectionNumbers(collectionNumbers);

    int numErrors = 0;
    int numValid = 0;
    Map<String, String> invalidCollectionNumbers = new HashMap<String, String>();
    int index = 0;
    for (CollectedSample c : collections) {
      if (c == null) {
        String invalidCollectionNumber = collectionNumbers.get(index);
        invalidCollectionNumbers.put(invalidCollectionNumber, invalidCollectionNumber);
        numErrors++;
      } else {
        numValid++;
      }
      index++;
    }

    Worksheet worksheet = worksheetRepository.findWorksheetById(Long.parseLong(worksheetId));
    ModelAndView mv = new ModelAndView();
    mv.addObject("worksheet", worksheet);
    mv.addObject("worksheetId", worksheet.getId());
    mv.addObject("worksheetFields", utilController.getFormFieldsForForm("worksheet"));

    if (numErrors > 0) {
      mv.addObject("invalidCollectionNumbers", invalidCollectionNumbers);
      mv.addObject("enteredCollectionNumbers", collectionNumbers);
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
    } else if (numValid > 0) {
      worksheetRepository.addCollectionsToWorksheet(Long.parseLong(worksheetId), collectionNumbers);
    }

    mv.setViewName("worksheets/worksheetDetail");

    return mv;
  }

  @RequestMapping(value="/findWorksheetFormGenerator", method=RequestMethod.GET)
  public ModelAndView findCollectionWorksheetFormGenerator(HttpServletRequest request, Model model) {
    ModelAndView mv = new ModelAndView("worksheets/findWorksheetForm");
    Map<String, Object> m = model.asMap();
    m.put("refreshUrl", getUrl(request));
    FindWorksheetBackingForm findWorksheetForm = new FindWorksheetBackingForm();
    mv.addObject("findWorksheetForm", findWorksheetForm);
    mv.addObject("worksheetTypes", worksheetTypeRepository.getAllWorksheetTypes());
    return mv;
  }

  @RequestMapping(value="/findWorksheet", method=RequestMethod.GET)
  public ModelAndView findWorksheet(HttpServletRequest request,
        HttpServletResponse response, @ModelAttribute(value="findWorksheetForm") FindWorksheetBackingForm form) {

    ModelAndView mv = new ModelAndView();
    List<Worksheet> worksheets = worksheetRepository.findWorksheets(form.getWorksheetNumber(), form.getWorksheetTypes());
    mv.addObject("allWorksheets", getWorksheetViewModels(worksheets));
    mv.addObject("worksheetFields", utilController.getFormFieldsForForm("worksheet"));
    mv.addObject("refreshUrl", getUrl(request));
    mv.setViewName("worksheets/worksheetsTable");
    return mv;
  }

  @RequestMapping(value = "/worksheetSummary", method = RequestMethod.GET)
  public ModelAndView worksheetSummary(
      HttpServletRequest request,
      @RequestParam("worksheetId") Long worksheetId) {
    ModelAndView mv = new ModelAndView();
    Worksheet worksheet = worksheetRepository.findWorksheetFullInformation(worksheetId);
    mv.addObject("worksheet", getWorksheetViewModel(worksheet));
    mv.addObject("allCollectedSamples", worksheet.getCollectedSamples());
    mv.addObject("bloodTests", worksheetTypeRepository.getBloodTestsInWorksheet(worksheet.getWorksheetType().getId()));
    mv.addObject("refreshUrl", getUrl(request));
    mv.addObject("worksheetId", worksheet.getId());
    mv.addObject("worksheetFields", utilController.getFormFieldsForForm("worksheet"));

    mv.addObject("worksheetConfig", genericConfigRepository.getConfigProperties(ConfigPropertyConstants.COLLECTIONS_WORKSHEET));

    mv.setViewName("worksheets/worksheetSummary");
    return mv;
  }

  @RequestMapping(value = "/deleteWorksheet", method = RequestMethod.POST)
  public @ResponseBody
  Map<String, ? extends Object> deleteCollection(
      @RequestParam("worksheetId") Long worksheetId) {

    boolean success = true;
    String errMsg = "";
    try {
      worksheetRepository.deleteWorksheet(worksheetId);
    } catch (Exception ex) {
      System.err.println("Internal Exception");
      System.err.println(ex.getMessage());
      success = false;
      errMsg = "Internal Server Error";
    }

    Map<String, Object> m = new HashMap<String, Object>();
    m.put("success", success);
    m.put("errMsg", errMsg);
    return m;
  }

  private Object getWorksheetViewModels(List<Worksheet> worksheets) {
    List<WorksheetViewModel> worksheetViewModels = new ArrayList<WorksheetViewModel>();
    for (Worksheet worksheet : worksheets) {
      worksheetViewModels.add(new WorksheetViewModel(worksheet));
    }
    return worksheetViewModels;
  }
}
