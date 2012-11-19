package controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import model.DisplayNames;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import repository.DisplayNamesRepository;
import utils.ControllerUtil;

@Controller
public class DisplayNameConfigController {

	@Autowired
	private DisplayNamesRepository displayNamesRepository;

	public DisplayNameConfigController() {

	}

	@RequestMapping("/admin-displayNamesConfigLandingPage.html")
	public ModelAndView displayLandingPage(HttpServletRequest request) {

		ModelAndView modelAndView = new ModelAndView(
				"displayNamesConfigLandingPage");

		Map<String, Object> model = new HashMap<String, Object>();

		modelAndView.addObject("model", model);
		return modelAndView;
	}

	@RequestMapping("/admin-collectionsDisplayNamesConfig.html")
	public ModelAndView display(HttpServletRequest request) {

		ModelAndView modelAndView = new ModelAndView(
				"collectionsDisplayNamesConfig");

		Map<String, Object> model = new HashMap<String, Object>();

		model.put("hasCollection", true);

		ControllerUtil.addCollectedSampleDisplayNamesToModel(model,
				displayNamesRepository);
		modelAndView.addObject("model", model);
		return modelAndView;
	}

	@RequestMapping("/admin-donorsDisplayNamesConfig.html")
	public ModelAndView displayDonors(HttpServletRequest request) {

		ModelAndView modelAndView = new ModelAndView("donorsDisplayNamesConfig");

		Map<String, Object> model = new HashMap<String, Object>();

		model.put("hasNames", true);

		ControllerUtil.addDonorDisplayNamesToModel(model,
				displayNamesRepository);
		modelAndView.addObject("model", model);
		return modelAndView;
	}

	@RequestMapping("/admin-saveCollectionsDisplayNamesConfig")
	public ModelAndView saveConfig(@RequestParam Map<String, String> params,
			HttpServletRequest request) {

		ModelAndView modelAndView = new ModelAndView(
				"collectionsDisplayNamesConfig");

		Map<String, Object> model = new HashMap<String, Object>();
		saveFieldDisplayNames(params, model, "collection");

		model.put("configSaved", true);
		model.put("hasCollection", true);
		ControllerUtil.addCollectedSampleDisplayNamesToModel(model,
				displayNamesRepository);
		modelAndView.addObject("model", model);
		return modelAndView;
	}

	@RequestMapping("/admin-saveDonorsDisplayNamesConfig")
	public ModelAndView saveDonorsConfig(
			@RequestParam Map<String, String> params, HttpServletRequest request) {

		ModelAndView modelAndView = new ModelAndView("donorsDisplayNamesConfig");

		Map<String, Object> model = new HashMap<String, Object>();
		saveFieldDisplayNames(params, model, "donor");

		model.put("configSaved", true);
		model.put("hasNames", true);
		ControllerUtil.addDonorDisplayNamesToModel(model,
				displayNamesRepository);
		modelAndView.addObject("model", model);
		return modelAndView;
	}

	@RequestMapping("/admin-testResultsDisplayNamesConfig.html")
	public ModelAndView displayTestResults(HttpServletRequest request) {

		ModelAndView modelAndView = new ModelAndView(
				"testResultsDisplayNamesConfig");

		Map<String, Object> model = new HashMap<String, Object>();

		model.put("hasNames", true);

		ControllerUtil.addTestResultDisplayNamesToModel(model,
				displayNamesRepository);
		modelAndView.addObject("model", model);
		return modelAndView;
	}

	@RequestMapping("/admin-saveTestResultsDisplayNamesConfig")
	public ModelAndView saveTestResultsConfig(
			@RequestParam Map<String, String> params, HttpServletRequest request) {

		ModelAndView modelAndView = new ModelAndView(
				"testResultsDisplayNamesConfig");

		Map<String, Object> model = new HashMap<String, Object>();
		saveFieldDisplayNames(params, model, "testResults");

		model.put("configSaved", true);
		model.put("hasNames", true);
		ControllerUtil.addTestResultDisplayNamesToModel(model,
				displayNamesRepository);
		modelAndView.addObject("model", model);
		return modelAndView;
	}

	@RequestMapping("/admin-productsDisplayNamesConfig.html")
	public ModelAndView displayProducts(HttpServletRequest request) {

		ModelAndView modelAndView = new ModelAndView(
				"productsDisplayNamesConfig");

		Map<String, Object> model = new HashMap<String, Object>();

		model.put("hasNames", true);

		ControllerUtil.addProductDisplayNamesToModel(model,
				displayNamesRepository);
		modelAndView.addObject("model", model);
		return modelAndView;
	}

	@RequestMapping("/admin-saveProductsDisplayNamesConfig")
	public ModelAndView saveProductsConfig(
			@RequestParam Map<String, String> params, HttpServletRequest request) {

		ModelAndView modelAndView = new ModelAndView(
				"productsDisplayNamesConfig");

		Map<String, Object> model = new HashMap<String, Object>();
		saveFieldDisplayNames(params, model, "product");

		model.put("configSaved", true);
		model.put("hasNames", true);
		ControllerUtil.addProductDisplayNamesToModel(model,
				displayNamesRepository);
		modelAndView.addObject("model", model);
		return modelAndView;
	}

	@RequestMapping("/admin-requestsDisplayNamesConfig.html")
	public ModelAndView displayRequests(HttpServletRequest request) {

		ModelAndView modelAndView = new ModelAndView(
				"requestsDisplayNamesConfig");

		Map<String, Object> model = new HashMap<String, Object>();

		model.put("hasNames", true);

		ControllerUtil.addRequestDisplayNamesToModel(model,
				displayNamesRepository);
		modelAndView.addObject("model", model);
		return modelAndView;
	}

	@RequestMapping("/admin-saveRequestsDisplayNamesConfig")
	public ModelAndView saveRequestsConfig(
			@RequestParam Map<String, String> params, HttpServletRequest request) {

		ModelAndView modelAndView = new ModelAndView(
				"requestsDisplayNamesConfig");

		Map<String, Object> model = new HashMap<String, Object>();
		saveFieldDisplayNames(params, model, "request");

		model.put("configSaved", true);
		model.put("hasNames", true);
		ControllerUtil.addRequestDisplayNamesToModel(model,
				displayNamesRepository);
		modelAndView.addObject("model", model);
		return modelAndView;
	}

	@RequestMapping("/admin-issueDisplayNamesConfig.html")
	public ModelAndView displayIssue(HttpServletRequest request) {

		ModelAndView modelAndView = new ModelAndView("issueDisplayNamesConfig");

		Map<String, Object> model = new HashMap<String, Object>();

		model.put("hasNames", true);

		ControllerUtil.addIssueDisplayNamesToModel(model,
				displayNamesRepository);
		modelAndView.addObject("model", model);
		return modelAndView;
	}

	@RequestMapping("/admin-saveIssueDisplayNamesConfig")
	public ModelAndView saveIssueConfig(
			@RequestParam Map<String, String> params, HttpServletRequest request) {

		ModelAndView modelAndView = new ModelAndView("issueDisplayNamesConfig");

		Map<String, Object> model = new HashMap<String, Object>();
		saveFieldDisplayNames(params, model, "issue");

		model.put("configSaved", true);
		model.put("hasNames", true);
		ControllerUtil.addIssueDisplayNamesToModel(model,
				displayNamesRepository);
		modelAndView.addObject("model", model);
		return modelAndView;
	}

	@RequestMapping("/admin-usageDisplayNamesConfig.html")
	public ModelAndView displayUsage(HttpServletRequest request) {

		ModelAndView modelAndView = new ModelAndView("usageDisplayNamesConfig");

		Map<String, Object> model = new HashMap<String, Object>();

		model.put("hasNames", true);

		ControllerUtil.addUsageDisplayNamesToModel(model,
				displayNamesRepository);
		modelAndView.addObject("model", model);
		return modelAndView;
	}

	@RequestMapping("/admin-saveUsageDisplayNamesConfig")
	public ModelAndView saveUsageConfig(
			@RequestParam Map<String, String> params, HttpServletRequest request) {

		ModelAndView modelAndView = new ModelAndView("usageDisplayNamesConfig");

		Map<String, Object> model = new HashMap<String, Object>();
		saveFieldDisplayNames(params, model, "usage");

		model.put("configSaved", true);
		model.put("hasNames", true);
		ControllerUtil.addUsageDisplayNamesToModel(model,
				displayNamesRepository);
		modelAndView.addObject("model", model);
		return modelAndView;
	}

	@RequestMapping("/admin-reportsDisplayNamesConfig")
	public ModelAndView displayReports(HttpServletRequest request) {

		ModelAndView modelAndView = new ModelAndView(
				"reportsDisplayNamesConfig");

		Map<String, Object> model = new HashMap<String, Object>();

		model.put("hasNames", true);

		ControllerUtil.addReportsDisplayNamesToModel(model,
				displayNamesRepository);
		modelAndView.addObject("model", model);
		return modelAndView;
	}

	@RequestMapping("/admin-saveReportsDisplayNamesConfig")
	public ModelAndView saveReportsConfig(
			@RequestParam Map<String, String> params, HttpServletRequest request) {

		ModelAndView modelAndView = new ModelAndView(
				"reportsDisplayNamesConfig");

		Map<String, Object> model = new HashMap<String, Object>();
		saveFieldDisplayNames(params, model, "reports");

		model.put("configSaved", true);
		model.put("hasNames", true);
		ControllerUtil.addReportsDisplayNamesToModel(model,
				displayNamesRepository);

		modelAndView.addObject("model", model);
		return modelAndView;
	}

	private void saveFieldDisplayNames(Map<String, String> params,
			Map<String, Object> model, String recordType) {
		Set<String> fields = params.keySet();
		String fieldNames = "";
		for (String field : fields) {
			fieldNames = fieldNames + field + ":";
			fieldNames = fieldNames + params.get(field) + ",";

			model.put(field, params.get(field));
		}
		fieldNames = fieldNames.substring(0, fieldNames.length() - 1);
		if (displayNamesRepository.getDisplayName(recordType) == null) {
			displayNamesRepository.saveDisplayNames(new DisplayNames(
					recordType, fieldNames));
		} else {
			displayNamesRepository.updateDisplayNames(recordType, fieldNames);
		}
	}

}
