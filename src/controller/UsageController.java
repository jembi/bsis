package controller;

import static utils.ControllerUtil.getDate;
import static utils.ControllerUtil.getOptionalParamValue;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import model.ProductUsage;
import model.RecordFieldsConfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import repository.DisplayNamesRepository;
import repository.RecordFieldsConfigRepository;
import repository.UsageRepository;
import utils.ControllerUtil;

@Controller
public class UsageController {

	@Autowired
	private UsageRepository usageRepository;

	@Autowired
	private DisplayNamesRepository displayNamesRepository;

	@Autowired
	private RecordFieldsConfigRepository recordFieldsConfigRepository;

	@RequestMapping("/usageLandingPage")
	public ModelAndView getUsagePage(HttpServletRequest request) {

		return new ModelAndView("usageLandingPage");
	}

	@RequestMapping("/usageAdd")
	public ModelAndView getAddUsagePage(HttpServletRequest request) {

		ModelAndView modelAndView = new ModelAndView("usageAdd");
		Map<String, Object> model = new HashMap<String, Object>();
		ControllerUtil.addUsageDisplayNamesToModel(model,
				displayNamesRepository);
		ControllerUtil.addFieldsToDisplay("usage", model,
				recordFieldsConfigRepository);

		modelAndView.addObject("model", model);
		return modelAndView;
	}

	@RequestMapping("/updateUsage")
	public ModelAndView getUpdateUsagePage(HttpServletRequest request) {

		ModelAndView modelAndView = new ModelAndView("usageFind");
		Map<String, Object> model = new HashMap<String, Object>();
		ControllerUtil.addUsageDisplayNamesToModel(model,
				displayNamesRepository);
		ControllerUtil.addFieldsToDisplay("usage", model,
				recordFieldsConfigRepository);

		modelAndView.addObject("model", model);
		return modelAndView;
	}

	@RequestMapping("/addNewUsage")
	public ModelAndView addNewUsage(@RequestParam Map<String, String> params,
			HttpServletRequest request) {

		ModelAndView modelAndView = new ModelAndView("usageUpdateExisting");
		Map<String, Object> model = new HashMap<String, Object>();
		RecordFieldsConfig recordFieldsConfig = recordFieldsConfigRepository
				.getRecordFieldsConfig("usage");
		ProductUsage productUsage = new ProductUsage(
				params.get("productNumber"), getOptionalParamValue(
						getDate(params.get("usageDate")), recordFieldsConfig,
						"usageDate"),
				getOptionalParamValue(params.get("hospital"),
						recordFieldsConfig, "hospital"), getOptionalParamValue(
						params.get("ward"), recordFieldsConfig, "ward"),
				params.get("useIndication"), Boolean.FALSE,
				getOptionalParamValue(params.get("comment"),
						recordFieldsConfig, "comment"));
		usageRepository.saveUsage(productUsage);
		model.put("usageAdded", true);
		model.put("hasUsage", true);
		model.put("usage", productUsage);
		ControllerUtil.addUsageDisplayNamesToModel(model,
				displayNamesRepository);
		ControllerUtil.addFieldsToDisplay("usage", model,
				recordFieldsConfigRepository);

		modelAndView.addObject("model", model);
		return modelAndView;
	}

	@RequestMapping("/updateSelectedUsage")
	public ModelAndView updateSelectedUsage(
			@RequestParam Map<String, String> params, HttpServletRequest request) {

		ModelAndView modelAndView = new ModelAndView("usageUpdateExisting");
		Map<String, Object> model = new HashMap<String, Object>();
		RecordFieldsConfig recordFieldsConfig = recordFieldsConfigRepository
				.getRecordFieldsConfig("usage");
		ProductUsage productUsage = new ProductUsage(
				params.get("productNumber"), getOptionalParamValue(
						getDate(params.get("usageDate")), recordFieldsConfig,
						"usageDate"),
				getOptionalParamValue(params.get("hospital"),
						recordFieldsConfig, "hospital"), getOptionalParamValue(
						params.get("ward"), recordFieldsConfig, "ward"),
				params.get("useIndication"), Boolean.FALSE,
				getOptionalParamValue(params.get("comment"),
						recordFieldsConfig, "comment"));
		usageRepository.updateUsage(productUsage);
		model.put("usageUpdated", true);
		model.put("hasUsage", true);
		model.put("usage", productUsage);
		ControllerUtil.addUsageDisplayNamesToModel(model,
				displayNamesRepository);
		ControllerUtil.addFieldsToDisplay("usage", model,
				recordFieldsConfigRepository);

		modelAndView.addObject("model", model);
		return modelAndView;
	}

	@RequestMapping("/deleteExistingUsage")
	public ModelAndView deleteExistingUsage(
			@RequestParam Map<String, String> params, HttpServletRequest request) {

		ModelAndView modelAndView = new ModelAndView("usageAdd");
		Map<String, Object> model = new HashMap<String, Object>();

		String productNumber = params.get("productNumber");
		ProductUsage productUsage = usageRepository
				.findProductUsage(productNumber);
		ProductUsage deletedProductUsage = new ProductUsage();
		deletedProductUsage.copy(productUsage);
		deletedProductUsage.setDeleted(Boolean.TRUE);
		usageRepository.updateUsage(deletedProductUsage);
		model.put("usageDeleted", true);
		model.put("deletedUsageProductNumber", productNumber);
		ControllerUtil.addUsageDisplayNamesToModel(model,
				displayNamesRepository);
		ControllerUtil.addFieldsToDisplay("usage", model,
				recordFieldsConfigRepository);

		modelAndView.addObject("model", model);
		return modelAndView;
	}

	@RequestMapping("/findUsage")
	public ModelAndView findUsage(@RequestParam Map<String, String> params,
			HttpServletRequest request) {

		ModelAndView modelAndView = new ModelAndView("usageUpdateExisting");
		Map<String, Object> model = new HashMap<String, Object>();

		String productNumber = params.get("productNumber");
		ProductUsage productUsage = usageRepository
				.findProductUsage(productNumber);
		if (productUsage == null) {
			modelAndView = new ModelAndView("usageFind");
			model.put("usageNotFound", true);
			model.put("productNumber", productNumber);
		} else {
			model.put("hasUsage", true);
			model.put("usage", productUsage);
		}
		ControllerUtil.addUsageDisplayNamesToModel(model,
				displayNamesRepository);
		ControllerUtil.addFieldsToDisplay("usage", model,
				recordFieldsConfigRepository);

		modelAndView.addObject("model", model);
		return modelAndView;
	}
}
