package controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import model.Product;
import model.User;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.jfree.data.category.DefaultCategoryDataset;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import repository.DisplayNamesRepository;
import repository.ProductRepository;
import utils.ChartUtil;
import utils.ControllerUtil;

@Controller
public class InventoryController {
	@Autowired
	private ProductRepository productRepository;
	@Autowired
	private DisplayNamesRepository displayNamesRepository;

	private final String WHOLE_BLOOD = "wholeBlood";
	private final String RCC = "rcc";
	private final String FFP = "ffp";
	private final String PLATELETS = "platelets";
	private final String PARTIAL_PLATELETS = "partialPlatelets";

	@RequestMapping("/reports")
	public ModelAndView getReportsPage(HttpServletRequest request) {

		return new ModelAndView("reports");
	}

	@RequestMapping("/inventoryDetails")
	public ModelAndView getInventory(HttpServletRequest request) {

		ModelAndView modelAndView = new ModelAndView("inventoryDetails");
		Map<String, Object> model = new HashMap<String, Object>();
		List<Product> products = productRepository
				.getAllUnissuedThirtyFiveDayProducts();
		Collections.sort(products, new Comparator<Product>() {
			public int compare(Product product, Product product1) {
				return product.getDateCollected().compareTo(
						product1.getDateCollected());
			}
		});
		ControllerUtil.addProductDisplayNamesToModel(model,
				displayNamesRepository);
		ControllerUtil.addReportsDisplayNamesToModel(model,
				displayNamesRepository);
		model.put("products", products);
		modelAndView.addObject("model", model);

		return modelAndView;
	}

	@RequestMapping("/inventorySummary")
	public ModelAndView getInventorySummary(HttpServletRequest request) {

		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		ModelAndView modelAndView = new ModelAndView("inventorySummary");
		Map<String, Object> model = new HashMap<String, Object>();
		List<Product> products = productRepository
				.getAllUnissuedThirtyFiveDayProducts();
		model.put("totalProducts", products.size());
		model.put("totalWholeBlood", getProductsOfType(WHOLE_BLOOD, products)
				.size());
		model.put("totalRCC", getProductsOfType(RCC, products).size());
		model.put("totalFFP", getProductsOfType(FFP, products).size());
		model.put("totalPlatelets", getProductsOfType(PLATELETS, products)
				.size());
		model.put("totalPartialPlatelets",
				getProductsOfType(PARTIAL_PLATELETS, products).size());
		// String chartFilename = "inventoryBarChart" + user.getUsername();
		// model.put("inventoryChartName", chartFilename);
		// model.put("hasInventoryReportGraph", true);
		// createGraphForInventory(getProductCountsByType(products),
		// chartFilename);
		ControllerUtil.addReportsDisplayNamesToModel(model,
				displayNamesRepository);

		modelAndView.addObject("model", model);

		return modelAndView;
	}

	private void createGraphForInventory(
			LinkedHashMap<String, Integer> productCountsByType,
			String chartFilename) {
		final DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		for (String productType : productCountsByType.keySet()) {
			dataset.addValue(productCountsByType.get(productType), "",
					productType);
		}

		ChartUtil.createBarGraph(chartFilename, dataset, "Product Type",
				"Products");
	}

	private LinkedHashMap<String, Integer> getProductCountsByType(
			List<Product> products) {
		LinkedHashMap<String, Integer> productCounts = new LinkedHashMap<String, Integer>();
		ArrayList<Product> wholeBlood = getProductsOfType(WHOLE_BLOOD, products);
		productCounts.put(WHOLE_BLOOD, wholeBlood.size());

		ArrayList<Product> rcc = getProductsOfType(RCC, products);
		productCounts.put(RCC, rcc.size());

		ArrayList<Product> ffp = getProductsOfType(FFP, products);
		productCounts.put(FFP, ffp.size());

		ArrayList<Product> platelets = getProductsOfType(PLATELETS, products);
		productCounts.put(PLATELETS, platelets.size());

		ArrayList<Product> partialPlatelets = getProductsOfType(
				PARTIAL_PLATELETS, products);
		productCounts.put(PARTIAL_PLATELETS, platelets.size());

		return productCounts;
	}

	private ArrayList<Product> getProductsOfType(final String productType,
			List<Product> products) {
		return (ArrayList<Product>) CollectionUtils.select(products,
				new Predicate() {
					public boolean evaluate(Object o) {
						Product product = (Product) o;
						if (product.getType().equals(productType)) {
							return true;
						}
						return false;
					}
				});
	}
}
