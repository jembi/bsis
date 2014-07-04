package controller;

import backingform.DonationBatchBackingForm;
import backingform.FindDonationBatchBackingForm;
import backingform.validator.DonationBatchBackingFormValidator;
import static controller.CollectedSampleController.getCollectionViewModels;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityExistsException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import model.collectedsample.CollectedSample;
import model.donationbatch.DonationBatch;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
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
import repository.DonationBatchRepository;
import repository.LocationRepository;
import utils.PermissionConstants;
import viewmodel.DonationBatchViewModel;

@Controller
public class DonationBatchController {

  @Autowired
  private DonationBatchRepository donationBatchRepository;

  @Autowired
  private LocationRepository locationRepository;

  @Autowired
  private UtilController utilController;

  public DonationBatchController() {
  }

  @InitBinder
  protected void initBinder(WebDataBinder binder) {
    binder.setValidator(new DonationBatchBackingFormValidator(binder.getValidator(),
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

  @RequestMapping(value = "/findDonationBatchFormGenerator", method = RequestMethod.GET)
  @PreAuthorize("hasRole('"+PermissionConstants.VIEW_DONATION_BATCH+"')")
  public ModelAndView findDonationFormGenerator(HttpServletRequest request, Model model) {

    FindDonationBatchBackingForm form = new FindDonationBatchBackingForm();
    model.addAttribute("findDonationBatchForm", form);

    ModelAndView mv = new ModelAndView("donationbatch/findDonationBatchForm");
    addEditSelectorOptions(mv.getModelMap());
    Map<String, Object> tips = new HashMap<String, Object>();
    utilController.addTipsToModel(tips, "donationbatch.find");
    mv.addObject("tips", tips);
    // to ensure custom field names are displayed in the form
    mv.addObject("donationBatchFields", utilController.getFormFieldsForForm("donationBatch"));
    mv.addObject("refreshUrl", getUrl(request));
    return mv;
  }

  @RequestMapping(value="/findDonationBatch")
  @PreAuthorize("hasRole('"+PermissionConstants.VIEW_DONATION_BATCH+"')")
  public ModelAndView findDonationBatch(HttpServletRequest request,
      @ModelAttribute("findDonationBatchForm") FindDonationBatchBackingForm form,
       Model model) {

    List<Long> centerIds = new ArrayList<Long>();
    centerIds.add((long) -1);
    if (form.getDonationCenters() != null) {
      for (String center : form.getDonationCenters()) {
        centerIds.add(Long.parseLong(center));
      }
    }

    List<Long> siteIds = new ArrayList<Long>();
    siteIds.add((long) -1);
    if (form.getDonationSites() != null) {
      for (String site : form.getDonationSites()) {
        siteIds.add(Long.parseLong(site));
      }
    }
      int period = form.getPeriod();

      DateFormat formatter;
      formatter = new SimpleDateFormat("MM/dd/yyyy");
      Date startDate;
      Date endDate;

      try {
          Date exactDate = formatter.parse(form.getExactDate());
          startDate = DateUtils.addDays(exactDate, -period);
          endDate = DateUtils.addDays(exactDate, period);
      } catch (ParseException ex) {
          startDate = null;
          endDate = null;
      }


    List<DonationBatch> donationBatches =
        donationBatchRepository.findDonationBatches(form.getDin(), centerIds, siteIds,startDate, endDate);
      

    ModelAndView modelAndView = new ModelAndView("donationbatch/donationBatchesTable");
    Map<String, Object> m = model.asMap();
    m.put("donationBatchFields", utilController.getFormFieldsForForm("donationBatch"));
    m.put("allDonationBatches", getDonationBatchViewModels(donationBatches));
    m.put("refreshUrl", getUrl(request));

    addEditSelectorOptions(m);

    modelAndView.addObject("model", m);
    return modelAndView;
  }

  private void addEditSelectorOptions(Map<String, Object> m) {
    m.put("centers", locationRepository.getAllCenters());
    m.put("sites", locationRepository.getAllCollectionSites());
  }

  @RequestMapping(value = "/addDonationBatchFormGenerator", method = RequestMethod.GET)
  @PreAuthorize("hasRole('"+PermissionConstants.ADD_DONATION_BATCH+"')")
  public ModelAndView addDonationBatchFormGenerator(HttpServletRequest request,
      Model model) {

    DonationBatchBackingForm form = new DonationBatchBackingForm();

    ModelAndView mv = new ModelAndView("donationbatch/addDonationBatchForm");
    mv.addObject("requestUrl", getUrl(request));
    mv.addObject("firstTimeRender", true);
    mv.addObject("addDonationBatchForm", form);
    mv.addObject("refreshUrl", getUrl(request));
    Map<String, Map<String, Object>> formFields = utilController.getFormFieldsForForm("donationbatch");
    addEditSelectorOptions(mv.getModelMap());
    // to ensure custom field names are displayed in the form
    mv.addObject("donationBatchFields", formFields);
    return mv;
  }

  @RequestMapping(value = "/addDonationBatch", method = RequestMethod.POST)
  @PreAuthorize("hasRole('"+PermissionConstants.ADD_DONATION_BATCH+"')") 
  public ModelAndView addDonationBatch(
      HttpServletRequest request,
      HttpServletResponse response,
      @ModelAttribute("addDonationBatchForm") @Valid DonationBatchBackingForm form,
      BindingResult result, Model model) {

    ModelAndView mv = new ModelAndView();
    boolean success = false;

    Map<String, Map<String, Object>> formFields = utilController.getFormFieldsForForm("donationBatch");
    mv.addObject("donationBatchFields", formFields);

    DonationBatch savedDonationBatch = null;
    if (result.hasErrors()) {
      mv.addObject("hasErrors", true);
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      success = false;
    } else {
      try {
        DonationBatch donationBatch = form.getDonationBatch();
        donationBatch.setIsDeleted(false);
        savedDonationBatch = donationBatchRepository.addDonationBatch(donationBatch);
        mv.addObject("hasErrors", false);
        success = true;
        form = new DonationBatchBackingForm();
      } catch (EntityExistsException ex) {
        ex.printStackTrace();
        success = false;
      } catch (Exception ex) {
        ex.printStackTrace();
        success = false;
      }
    }

    if (success) {
      mv.addObject("donationBatchId", savedDonationBatch.getId());
      mv.addObject("donationBatch", getDonationBatchViewModel(savedDonationBatch));
      mv.addObject("addAnotherDonationBatchUrl", "addDonationBatchFormGenerator.html");
      mv.setViewName("donationbatch/addDonationBatchSuccess");
    } else {
      mv.addObject("errorMessage", "Error creating donation batch. Please fix the errors noted below.");
      mv.addObject("firstTimeRender", false);
      mv.addObject("addDonationBatchForm", form);
      mv.addObject("refreshUrl", "addDonationBatchFormGenerator.html");
      mv.setViewName("donationbatch/addDonationBatchError");
    }
    addEditSelectorOptions(mv.getModelMap());
    mv.addObject("success", success);
    return mv;
  }

  private DonationBatchViewModel getDonationBatchViewModel(DonationBatch donationBatch) {
    DonationBatchViewModel donationBatchViewModel = new DonationBatchViewModel(donationBatch);
    return donationBatchViewModel;
  }

  public  List<DonationBatchViewModel> getDonationBatchViewModels(
      List<DonationBatch> donationBatches) {
    if (donationBatches == null)
      return Arrays.asList(new DonationBatchViewModel[0]);
    List<DonationBatchViewModel> donationBatchViewModels = new ArrayList<DonationBatchViewModel>();
    for (DonationBatch donationBatch : donationBatches) {
      Long numberOfDonations = donationBatchRepository.numberOfDonationsInBatch(donationBatch.getId());
      donationBatchViewModels.add(new DonationBatchViewModel(donationBatch,numberOfDonations));
    }
    return donationBatchViewModels;
  }

  @RequestMapping(value = "/donationBatchSummary", method = RequestMethod.GET)
  @PreAuthorize("hasRole('"+PermissionConstants.VIEW_DONATION_BATCH+"')")
  public @ResponseBody ModelAndView donationBatchSummaryGenerator(HttpServletRequest request, Model model,
      @RequestParam(value = "donationBatchId", required = false) Integer donationBatchId) {

    ModelAndView mv = new ModelAndView("donationbatch/donationBatchSummary");

    mv.addObject("requestUrl", getUrl(request));

    DonationBatch donationBatch = null;
    if (donationBatchId != null) {
      donationBatch = donationBatchRepository.findDonationBatchById(donationBatchId);
      if (donationBatch != null) {
        mv.addObject("existingDonationBatch", true);
      }
      else {
        mv.addObject("existingDonationBatch", false);
      }
    }

    Map<String, Object> tips = new HashMap<String, Object>();
    utilController.addTipsToModel(tips, "donationBatches.finddonationbatch.donationbatchsummary");
    mv.addObject("tips", tips);
    
    Map<String, Object> m = model.asMap();
    List<CollectedSample> donations = donationBatchRepository.findAllDonationsOfBatchById(donationBatchId);
    
    DonationBatchViewModel donationBatchViewModel = getDonationBatchViewModel(donationBatch);
    mv.addObject("donationBatch", donationBatchViewModel);
    m.put("collectedSampleFields", utilController.getFormFieldsForForm("collectedSample"));
    m.put("allCollections", getCollectionViewModels(donations));
    mv.addObject("model",m);
    mv.addObject("refreshUrl", getUrl(request));
    // to ensure custom field names are displayed in the form
    mv.addObject("donationBatchFields", utilController.getFormFieldsForForm("donationBatch"));
    return mv;
  }
}
