package controller;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import model.Collection;
import model.Product;
import model.TestResult;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import repository.CollectionRepository;
import repository.DisplayNamesRepository;
import repository.ProductRepository;
import repository.TestResultRepository;
import utils.ControllerUtil;
import utils.LoggerUtil;

@Controller
public class ProductsController {
	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private DisplayNamesRepository displayNamesRepository;

	@Autowired
	private CollectionRepository collectionRepository;

	@Autowired
	private TestResultRepository testResultRepository;

	@RequestMapping("/productsLandingPage")
	public ModelAndView getProductsLandingPage(
			HttpServletRequest httpServletRequest) {
		LoggerUtil.logUrl(httpServletRequest);

		return new ModelAndView("productsLandingPage");
	}

	@RequestMapping("/products")
	public ModelAndView getProducts(HttpServletRequest httpServletRequest) {
		LoggerUtil.logUrl(httpServletRequest);

		ModelAndView modelAndView = new ModelAndView("products");
		Map<String, Object> model = new HashMap<String, Object>();

		ControllerUtil.addProductDisplayNamesToModel(model,
				displayNamesRepository);
		modelAndView.addObject("model", model);

		return modelAndView;
	}

	@RequestMapping("/findProduct")
	public ModelAndView findProduct(@RequestParam Map<String, String> params,
			HttpServletRequest httpServletRequest) {
		LoggerUtil.logUrl(httpServletRequest);
		String productNumber = params.get("findProductNumber");
		Product product = productRepository.findProduct(productNumber);
		ModelAndView modelAndView = new ModelAndView("products");
		Map<String, Object> model = new HashMap<String, Object>();

		if (product == null) {
			model.put("productNotFound", true);
			model.put("productNumber", productNumber);

		} else {
			model.put("productFound", true);
			model.put("hasProduct", true);
			model.put("product", product);
		}
		ControllerUtil.addProductDisplayNamesToModel(model,
				displayNamesRepository);
		modelAndView.addObject("model", model);

		return modelAndView;

	}

	@RequestMapping("/addProduct")
	public ModelAndView addProduct(@RequestParam Map<String, String> params,
			HttpServletRequest httpServletRequest) {
		LoggerUtil.logUrl(httpServletRequest);
		Map<String, Object> model = new HashMap<String, Object>();

		String collectionNumber = params.get("collectionNumber");
		Collection collection = collectionRepository
				.findCollectionByNumber(collectionNumber);

		ModelAndView modelAndView = new ModelAndView("products");
		if (collection == null) {
			model.put("collectionNotFound", true);
			model.put("collectionNumber", collectionNumber);
		} else {
			if (isCollectionSafe(collection)) {

				Product product = new Product(params.get("productNumber"),
						collection, params.get("productType"), Boolean.FALSE,
						Boolean.FALSE, params.get("comments"));
				product.setAbo(collection.getAbo());
				product.setRhd(collection.getRhd());
				productRepository.saveProduct(product);
				model.put("productCreated", true);

				model.put("product", product);
				model.put("hasProduct", true);
			} else {
				model.put("collectionUnsafe", true);
				model.put("quarantineCollectionNo", collectionNumber);
			}
		}

		ControllerUtil.addProductDisplayNamesToModel(model,
				displayNamesRepository);
		modelAndView.addObject("model", model);

		return modelAndView;

	}

	private boolean isCollectionSafe(Collection collection) {
		List<TestResult> allTestResults = testResultRepository
				.getAllTestResults(collection.getCollectionNumber());
		TestResult latestTestResult = null;
		if (allTestResults.size() > 0) {
			latestTestResult = Collections.max(allTestResults,
					new Comparator<TestResult>() {
						public int compare(TestResult testResult,
								TestResult testResult1) {
							return testResult.getDateTested().compareTo(
									testResult1.getDateTested());
						}
					});
		}
		if (latestTestResult != null
				&& isNegativeTestResult(latestTestResult.getHbv())
				&& isNegativeTestResult(latestTestResult.getHcv())
				&& isNegativeTestResult(latestTestResult.getHiv())
				&& isNegativeTestResult(latestTestResult.getSyphilis())) {
			return true;
		}
		return false;
	}

	private boolean isNegativeTestResult(String testResult) {
		return testResult != null
				&& testResult.toLowerCase().equals("negative");
	}

	@RequestMapping("/updateProduct")
	public ModelAndView updateProduct(@RequestParam Map<String, String> params,
			HttpServletRequest httpServletRequest) {
		LoggerUtil.logUrl(httpServletRequest);
		Map<String, Object> model = new HashMap<String, Object>();
		String existingProductNumber = params.get("updateProductNumber");

		String collectionNumber = params.get("updateCollectionNumber");
		Collection collection = collectionRepository
				.findCollectionByNumber(collectionNumber);
		ModelAndView modelAndView = new ModelAndView("products");

		if (collection == null) {
			model.put("collectionNotFound", true);
			model.put("collectionNumber", collectionNumber);
			Collection dummyCollection = new Collection();
			dummyCollection.setCollectionNumber(collectionNumber);
			Product product = new Product(params.get("updateProductNumber"),
					dummyCollection, params.get("updateProductType"),
					Boolean.FALSE, Boolean.FALSE, params.get("comments"));
			model.put("product", product);
			model.put("hasProduct", true);
			model.put("productNotUpdated", true);

		} else {
			if (isCollectionSafe(collection)) {

				Product product1 = new Product(
						params.get("updateProductNumber"), collection,
						params.get("updateProductType"), Boolean.FALSE,
						Boolean.FALSE, params.get("comments"));
				product1.setAbo(collection.getAbo());
				product1.setRhd(collection.getRhd());
				Product existingProduct = productRepository.updateProduct(
						product1, existingProductNumber);
				model.put("productUpdated", true);
				Product product = existingProduct;

				model.put("productUpdated", true);

				model.put("product", product);
				model.put("hasProduct", true);
			} else {
				Product product = productRepository
						.findProduct(existingProductNumber);
				model.put("product", product);
				model.put("hasProduct", true);
				model.put("productNotUpdated", true);
				model.put("quarantineCollectionNo", collectionNumber);
				model.put("collectionUnsafe", true);
			}
		}
		ControllerUtil.addProductDisplayNamesToModel(model,
				displayNamesRepository);

		modelAndView.addObject("model", model);

		return modelAndView;

	}

	@RequestMapping("/deleteProduct")
	public ModelAndView deleteProduct(@RequestParam Map<String, String> params,
			HttpServletRequest httpServletRequest) {
		LoggerUtil.logUrl(httpServletRequest);
		Product product = null;
		Map<String, Object> model = new HashMap<String, Object>();
		String existingProductNumber = params.get("updateProductNumber");

		productRepository.delete(existingProductNumber);

		model.put("productDeleted", true);
		model.put("productNumberDeleted", existingProductNumber);

		ModelAndView modelAndView = new ModelAndView("products");

		ControllerUtil.addProductDisplayNamesToModel(model,
				displayNamesRepository);

		modelAndView.addObject("model", model);

		return modelAndView;

	}

}
