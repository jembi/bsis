package controller;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import model.Issue;
import model.Location;
import model.Product;
import model.Request;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import repository.DisplayNamesRepository;
import repository.IssueRepository;
import repository.LocationRepository;
import repository.ProductRepository;
import repository.RecordFieldsConfigRepository;
import repository.RequestRepository;
import utils.ControllerUtil;
import utils.LoggerUtil;
import viewmodel.RequestViewModel;

@Controller
public class IssueController {

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private IssueRepository issueRepository;

	@Autowired
	private RequestRepository requestRepository;

	@Autowired
	private DisplayNamesRepository displayNamesRepository;

	@Autowired
	private RecordFieldsConfigRepository recordFieldsConfigRepository;

	@Autowired
	private LocationRepository locationRepository;

	@RequestMapping("/issueLandingPage")
	public ModelAndView getRequestsPage(HttpServletRequest request) {

		return new ModelAndView("issueLandingPage");
	}

	@RequestMapping("/issueViewRequests")
	public ModelAndView getAddRequestsPage(HttpServletRequest request) {

		ModelAndView modelAndView = new ModelAndView("issueViewRequests");
		Map<String, Object> model = new HashMap<String, Object>();
		setUpModelForIssueViewRequests(model);
		modelAndView.addObject("model", model);
		return modelAndView;
	}

	@RequestMapping("/issueProduct")
	public ModelAndView getUpdateRequestsPage(
			@RequestParam Map<String, String> params,
			HttpServletRequest httpServletRequest) {
		LoggerUtil.logUrl(httpServletRequest);
		Long requestId = getParam(params, "selectedRequestId");
		Map<String, Object> model = new HashMap<String, Object>();
		ModelAndView modelAndView = new ModelAndView("issueMatchingProducts");
		Request request = requestRepository.findRequest(requestId);
		List<Location> sites = addCollectionSitesToModel(model);
		model.put("siteId", request.getSiteId());
		model.put("request", new RequestViewModel(request, sites));
		List<Product> matchingProducts = productRepository
				.getAllUnissuedThirtyFiveDayProducts(request.getProductType(),
						request.getAbo(), request.getRhd());
		if (request.getProductType().equals("platelets")) {
			matchingProducts.addAll(productRepository
					.getAllUnissuedThirtyFiveDayProducts("partialPlatelets",
							request.getAbo(), request.getRhd()));
		}
		Collections.sort(matchingProducts, new Comparator<Product>() {
			public int compare(Product product, Product product1) {
				return product.getDateCollected().compareTo(
						product1.getDateCollected());
			}
		});
		model.put("products", matchingProducts);
		ControllerUtil.addProductDisplayNamesToModel(model,
				displayNamesRepository);
		ControllerUtil.addIssueDisplayNamesToModel(model,
				displayNamesRepository);
		ControllerUtil.addRequestDisplayNamesToModel(model,
				displayNamesRepository);
		ControllerUtil.addFieldsToDisplay("request", model,
				recordFieldsConfigRepository);
		modelAndView.addObject("model", model);
		return modelAndView;
	}

	@RequestMapping("/issueAnyProduct")
	public ModelAndView issueAnyProductPage(
			@RequestParam Map<String, String> params, HttpServletRequest request) {

		Map<String, Object> model = new HashMap<String, Object>();
		ModelAndView modelAndView = new ModelAndView("issueAllProducts");
		setUpModelForIssueAnyProducts(model);

		modelAndView.addObject("model", model);
		return modelAndView;
	}

	@RequestMapping("/issueSelectedProducts")
	public ModelAndView addNewRequests(
			@RequestParam Map<String, String> params,
			HttpServletRequest httpServletRequest) {
		LoggerUtil.logUrl(httpServletRequest);
		ModelAndView modelAndView = new ModelAndView("issueAllProducts");
		Map<String, Object> model = new HashMap<String, Object>();
		ArrayList<Long> productIds = getAllProductIds(params);
		TreeMap<Long, String> sites = getAllProductIdValueMaps("site", params);
		TreeMap<Long, String> dateIssued = getAllProductIdValueMaps(
				"issueDate", params);
		int plateletsIssued = 0;
		int partialPlateletsIssued = 0;
		for (Long productId : productIds) {
			Product product = productRepository.findProduct(productId);
			Issue issue = new Issue(product.getProductNumber(),
					getDate(dateIssued.get(product.getProductId())),
					getSiteForIssue(sites, product), Boolean.FALSE,
					product.getComments());
			issueRepository.saveIssue(issue);
			Product issuedProduct = new Product();
			issuedProduct.copy(product);
			issuedProduct.setIssued(Boolean.TRUE);
			productRepository.updateProduct(issuedProduct,
					product.getProductNumber());
			if (issuedProduct.getType().equals("platelets")) {
				plateletsIssued++;
			}
			if (issuedProduct.getType().equals("partialPlatelets")) {
				partialPlateletsIssued++;
			}
		}
		if (params.containsKey("requestId")) {
			modelAndView = new ModelAndView("issueViewRequests");
			Request request = requestRepository.findRequest(getParam(params,
					"requestId"));
			Request updatedRequest = new Request();
			updatedRequest.copy(request);
			Long quantityIssued = getParam(params, "quantityIssued");
			updateRequestStatus(request, updatedRequest, quantityIssued,
					plateletsIssued, partialPlateletsIssued);
			requestRepository.updateRequest(updatedRequest,
					request.getRequestNumber());
			model.put("requestNumber", request.getRequestNumber());
			setUpModelForIssueViewRequests(model);
		} else {
			setUpModelForIssueAnyProducts(model);
		}
		model.put("productsIssued", true);
		modelAndView.addObject("model", model);
		return modelAndView;
	}

	private void updateRequestStatus(Request request, Request updatedRequest,
			Long quantityIssued, int plateletsIssued, int partialPlateletsIssued) {
		if (request.getProductType().equals("platelets")) {
			quantityIssued = (long) plateletsIssued
					+ (partialPlateletsIssued / 6);
		}
		if (request.getQuantity() > quantityIssued) {
			updatedRequest.setStatus("partiallyFulfilled");
			updatedRequest.setQuantity(new Long(request.getQuantity()
					- quantityIssued).intValue());
		} else {
			updatedRequest.setStatus("fulfilled");
		}
	}

	private Long getSiteForIssue(TreeMap<Long, String> sites, Product product) {
		String site = sites.get(product.getProductId());
		if (StringUtils.hasText(site)) {
			return Long.valueOf(site);
		}
		return null;
	}

	private void setUpModelForIssueAnyProducts(Map<String, Object> model) {
		List<Location> sites = addCollectionSitesToModel(model);
		List<Product> unissuedProducts = productRepository
				.getAllUnissuedThirtyFiveDayProducts();
		Collections.sort(unissuedProducts, new Comparator<Product>() {
			public int compare(Product product, Product product1) {
				return product.getDateCollected().compareTo(
						product1.getDateCollected());
			}
		});
		model.put("products", unissuedProducts);
		ControllerUtil.addProductDisplayNamesToModel(model,
				displayNamesRepository);
		ControllerUtil.addIssueDisplayNamesToModel(model,
				displayNamesRepository);
		ControllerUtil.addRequestDisplayNamesToModel(model,
				displayNamesRepository);
		ControllerUtil.addFieldsToDisplay("request", model,
				recordFieldsConfigRepository);
	}

	private TreeMap<Long, String> getAllProductIdValueMaps(String field,
			Map<String, String> params) {
		TreeMap<Long, String> fieldsMap = new TreeMap<Long, String>();
		for (String paramName : params.keySet()) {
			if (paramName.startsWith(field + "-")) {
				String fieldValue = params.get(paramName);
				Long productId = Long.valueOf(paramName.split("-")[1].replace(
						"productId", ""));
				fieldsMap.put(productId, fieldValue);
			}
		}
		return fieldsMap;
	}

	private ArrayList<Long> getAllProductIds(Map<String, String> params) {
		ArrayList<Long> result = new ArrayList<Long>();
		for (String paramName : params.keySet()) {
			if (paramName.startsWith("productId-")) {
				result.add(getParam(params, paramName));
			}
		}
		return result;
	}

	private void setUpModelForIssueViewRequests(Map<String, Object> model) {
		ArrayList<Request> allRequests = requestRepository
				.getAllUnfulfilledRequests();
		if (allRequests.size() == 0) {
			model.put("noRequestsFound", true);
		} else {
			List<RequestViewModel> allRequestViewModels = new ArrayList<RequestViewModel>();
			List<Location> allCollectionSites = locationRepository
					.getAllCollectionSites();
			model.put("sites", allCollectionSites);
			for (Request request : allRequests) {
				allRequestViewModels.add(new RequestViewModel(request,
						allCollectionSites));
			}
			model.put("allRequests", allRequestViewModels);
			ControllerUtil.addProductDisplayNamesToModel(model,
					displayNamesRepository);
			ControllerUtil.addIssueDisplayNamesToModel(model,
					displayNamesRepository);
			ControllerUtil.addRequestDisplayNamesToModel(model,
					displayNamesRepository);
			ControllerUtil.addFieldsToDisplay("request", model,
					recordFieldsConfigRepository);

		}
	}

	private Long getParam(Map<String, String> params, String paramName) {
		String paramValue = params.get(paramName);
		return paramValue == null || paramValue.isEmpty() ? null : Long
				.parseLong(paramValue);
	}

	private List<Location> addCollectionSitesToModel(Map<String, Object> model) {
		List<Location> allCollectionSites = locationRepository
				.getAllCollectionSites();
		model.put("sites", allCollectionSites);
		return allCollectionSites;
	}

	private Date getDate(String dateParam) {
		DateFormat formatter;
		formatter = new SimpleDateFormat("MM/dd/yyyy");
		Date collectionDate = null;
		try {
			String collectionDateEntered = dateParam;
			if (collectionDateEntered.length() > 0) {
				collectionDate = (Date) formatter.parse(collectionDateEntered);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return collectionDate;
	}
}
