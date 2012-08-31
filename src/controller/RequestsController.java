package controller;

import static utils.ControllerUtil.addFieldsToDisplay;
import static utils.ControllerUtil.addRequestDisplayNamesToModel;
import static utils.ControllerUtil.getDate;
import static utils.ControllerUtil.getOptionalParamValue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import model.Location;
import model.RecordFieldsConfig;
import model.Request;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import repository.DisplayNamesRepository;
import repository.LocationRepository;
import repository.RecordFieldsConfigRepository;
import repository.RequestRepository;
import utils.LoggerUtil;
import viewmodel.RequestViewModel;

@Controller
public class RequestsController {

	@Autowired
	private RequestRepository requestRepository;

	@Autowired
	private LocationRepository locationRepository;

	@Autowired
	private DisplayNamesRepository displayNamesRepository;
	@Autowired
	private RecordFieldsConfigRepository recordFieldsConfigRepository;

	@RequestMapping("/requestsLandingPage")
	public ModelAndView getRequestsPage(HttpServletRequest request) {

		return new ModelAndView("requestsLandingPage");
	}

	@RequestMapping("/requestsAdd")
	public ModelAndView getAddRequestsPage(HttpServletRequest request) {

		ModelAndView modelAndView = new ModelAndView("requestsAdd");
		Map<String, Object> model = new HashMap<String, Object>();
		addUsageSitesToModel(model);
		addRequestDisplayNamesToModel(model, displayNamesRepository);
		addFieldsToDisplay("request", model, recordFieldsConfigRepository);

		modelAndView.addObject("model", model);
		return modelAndView;
	}

	@RequestMapping("/requestsUpdate")
	public ModelAndView getUpdateRequestsPage(
			@RequestParam Map<String, String> params,
			HttpServletRequest httpServletRequest) {
		LoggerUtil.logUrl(httpServletRequest);
		Long requestId = getParam(params, "selectedRequestId");
		Request request = requestRepository.findRequest(requestId);
		ModelAndView modelAndView = new ModelAndView("requestsUpdate");
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("hasRequest", true);
		List<Location> sites = addUsageSitesToModel(model);
		model.put("request", new RequestViewModel(request, sites));
		addRequestDisplayNamesToModel(model, displayNamesRepository);
		addFieldsToDisplay("request", model, recordFieldsConfigRepository);

		modelAndView.addObject("model", model);
		return modelAndView;
	}

	@RequestMapping("/addNewRequest")
	public ModelAndView addNewRequests(
			@RequestParam Map<String, String> params,
			HttpServletRequest httpServletRequest) {
		LoggerUtil.logUrl(httpServletRequest);
		ModelAndView modelAndView = new ModelAndView("requestsUpdate");
		Map<String, Object> model = new HashMap<String, Object>();

		RecordFieldsConfig recordFieldsConfig = recordFieldsConfigRepository
				.getRecordFieldsConfig("request");
		Request request = new Request(params.get("requestNumber"),
				getOptionalParamValue(getDate(params.get("requestDate")),
						recordFieldsConfig, "requestDate"),
				getOptionalParamValue(getDate(params.get("requiredDate")),
						recordFieldsConfig, "requiredDate"), getParam(params,
						"site"), params.get("productType"), params.get("abo"),
				params.get("rhd"), getParam(params, "quantity").intValue(),
				params.get("status"), Boolean.FALSE, getOptionalParamValue(
						params.get("comment"), recordFieldsConfig, "comment"));
		requestRepository.saveRequest(request);
		model.put("requestAdded", true);
		model.put("hasRequest", true);
		List<Location> sites = addUsageSitesToModel(model);
		model.put("request", new RequestViewModel(request, sites));
		addRequestDisplayNamesToModel(model, displayNamesRepository);
		addFieldsToDisplay("request", model, recordFieldsConfigRepository);

		modelAndView.addObject("model", model);
		return modelAndView;
	}

	@RequestMapping("/updateExistingRequest")
	public ModelAndView updateExistingRequests(
			@RequestParam Map<String, String> params,
			HttpServletRequest httpServletRequest) {
		LoggerUtil.logUrl(httpServletRequest);
		ModelAndView modelAndView = new ModelAndView("requestsUpdate");
		Map<String, Object> model = new HashMap<String, Object>();

		RecordFieldsConfig recordFieldsConfig = recordFieldsConfigRepository
				.getRecordFieldsConfig("request");
		Request request = new Request(params.get("requestNumber"),
				getOptionalParamValue(getDate(params.get("requestDate")),
						recordFieldsConfig, "requestDate"),
				getOptionalParamValue(getDate(params.get("requiredDate")),
						recordFieldsConfig, "requiredDate"), getParam(params,
						"site"), params.get("productType"), params.get("abo"),
				params.get("rhd"), getParam(params, "quantity").intValue(),
				params.get("status"), Boolean.FALSE, getOptionalParamValue(
						params.get("comment"), recordFieldsConfig, "comment"));
		String requestNumber = params.get("requestNumber");
		requestRepository.updateRequest(request, requestNumber);
		model.put("requestUpdated", true);
		model.put("hasRequest", true);
		List<Location> sites = addUsageSitesToModel(model);
		model.put("request", new RequestViewModel(request, sites));
		addRequestDisplayNamesToModel(model, displayNamesRepository);
		addFieldsToDisplay("request", model, recordFieldsConfigRepository);

		modelAndView.addObject("model", model);
		return modelAndView;
	}

	@RequestMapping("/deleteExistingRequest")
	public ModelAndView deleteExistingRequests(
			@RequestParam Map<String, String> params,
			HttpServletRequest httpServletRequest) {
		LoggerUtil.logUrl(httpServletRequest);
		ModelAndView modelAndView = new ModelAndView("requestsAdd");
		Map<String, Object> model = new HashMap<String, Object>();

		String requestNumber = params.get("requestNumber");

		requestRepository.delete(requestNumber);
		model.put("requestDeleted", true);
		model.put("deletedRequestNumber", requestNumber);
		List<Location> sites = addUsageSitesToModel(model);
		addRequestDisplayNamesToModel(model, displayNamesRepository);
		addFieldsToDisplay("request", model, recordFieldsConfigRepository);

		modelAndView.addObject("model", model);
		return modelAndView;
	}

	@RequestMapping("/viewAllRequests")
	public ModelAndView findAllRequestsByCollection(
			@RequestParam Map<String, String> params,
			HttpServletRequest httpServletRequest) {
		LoggerUtil.logUrl(httpServletRequest);
		ModelAndView modelAndView = new ModelAndView("requestsTable");
		Map<String, Object> model = new HashMap<String, Object>();
		ArrayList<Request> allRequests = requestRepository.getAllRequests();
		if (allRequests.size() == 0) {
			model.put("noRequestsFound", true);
		} else {
			List<RequestViewModel> allRequestViewModels = new ArrayList<RequestViewModel>();
			List<Location> sites = addUsageSitesToModel(model);

			for (Request request : allRequests) {
				allRequestViewModels.add(new RequestViewModel(request, sites));
			}
			model.put("allRequests", allRequestViewModels);
		}
		addRequestDisplayNamesToModel(model, displayNamesRepository);
		addFieldsToDisplay("request", model, recordFieldsConfigRepository);

		modelAndView.addObject("model", model);
		return modelAndView;
	}

	private Long getParam(Map<String, String> params, String paramName) {
		String paramValue = params.get(paramName);
		return paramValue == null || paramValue.isEmpty() ? null : Long
				.parseLong(paramValue);
	}

	private List<Location> addUsageSitesToModel(Map<String, Object> model) {
		List<Location> allUsageSites = locationRepository.getAllUsageSites();
		model.put("sites", allUsageSites);
		return allUsageSites;
	}
}
