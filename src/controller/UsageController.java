package controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityExistsException;

import model.ProductUsage;
import model.ProductUsageBackingForm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import repository.DisplayNamesRepository;
import repository.RecordFieldsConfigRepository;
import repository.UsageRepository;
import utils.ControllerUtil;
import viewmodel.UsageViewModel;

@Controller
public class UsageController {

	@Autowired
	private UsageRepository usageRepository;

	@Autowired
	private DisplayNamesRepository displayNamesRepository;

	@Autowired
	private RecordFieldsConfigRepository recordFieldsConfigRepository;


  @RequestMapping(value = "/findUsageFormGenerator", method = RequestMethod.GET)
  public ModelAndView findUsageFormInit(Model model) {

    ProductUsageBackingForm form = new ProductUsageBackingForm();
    model.addAttribute("findUsageForm", form);

    ModelAndView mv = new ModelAndView("findUsageForm");
    Map<String, Object> m = model.asMap();

    // to ensure custom field names are displayed in the form
    ControllerUtil.addUsageDisplayNamesToModel(m, displayNamesRepository);
    mv.addObject("model", m);
    return mv;
  }

  @RequestMapping("/findUsage")
  public ModelAndView findUsage(
      @ModelAttribute("findUsageForm") ProductUsageBackingForm form,
      BindingResult result, Model model) {

    List<ProductUsage> requests = usageRepository.findAnyUsageMatching(
        form.getProductNumber(), form.getDateUsedFrom(),
        form.getDateUsedTo(), form.getUseIndications());

    ModelAndView modelAndView = new ModelAndView("usageTable");
    Map<String, Object> m = model.asMap();
    m.put("tableName", "findUsageTable");

    ControllerUtil.addUsageDisplayNamesToModel(m, displayNamesRepository);
    ControllerUtil.addFieldsToDisplay("usage", m,
        recordFieldsConfigRepository);
    m.put("allUsage", getUsageViewModels(requests));

    modelAndView.addObject("model", m);
    return modelAndView;
  }

  @RequestMapping(value = "/editUsageFormGenerator", method = RequestMethod.GET)
  public ModelAndView editUsageFormGenerator(
      Model model,
      @RequestParam(value = "productNumber", required = false) String productNumber,
      @RequestParam(value = "isDialog", required = false) String isDialog) {

    ProductUsageBackingForm form = new ProductUsageBackingForm();
    Map<String, Object> m = model.asMap();
    m.put("isDialog", isDialog);

    if (productNumber != null) {
      form.setProductNumber(productNumber);
      ProductUsage usage = usageRepository
          .findUsageByProductNumber(productNumber);
      if (usage != null) {
        form = new ProductUsageBackingForm(usage);
      }
    }

    m.put("editUsageForm", form);
    // to ensure custom field names are displayed in the form
    ControllerUtil.addUsageDisplayNamesToModel(m, displayNamesRepository);
    ModelAndView mv = new ModelAndView("editUsageForm");
    mv.addObject("model", m);
    return mv;
  }

  @RequestMapping(value = "/updateUsage", method = RequestMethod.POST)
  public @ResponseBody
  Map<String, ? extends Object> updateOrAddUsage(
      @ModelAttribute("editUsageForm") ProductUsageBackingForm form) {

    boolean success = true;
    String errMsg = "";
    try {
      ProductUsage usage = form.getUsage();
      usage.setComment("");
      usageRepository.updateOrAddUsage(usage);
    } catch (EntityExistsException ex) {
      // TODO: Replace with logger
      System.err.println("Entity Already exists");
      System.err.println(ex.getMessage());
      success = false;
      errMsg = "Usage Already Exists";
    } catch (Exception ex) {
      // TODO: Replace with logger
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

  private List<UsageViewModel> getUsageViewModels(List<ProductUsage> usages) {
    if (usages == null)
      return Arrays.asList(new UsageViewModel[0]);
    List<UsageViewModel> usageViewModels = new ArrayList<UsageViewModel>();
    for (ProductUsage usage : usages) {
      usageViewModels.add(new UsageViewModel(usage));
    }
    return usageViewModels;
  }
}
