package controller;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import repository.DisplayNamesRepository;
import repository.ProductRepository;
import repository.ReportConfigRepository;
import utils.ChartUtil;
import utils.ControllerUtil;
import utils.LoggerUtil;
import viewmodel.DailyAggregate;
import viewmodel.MonthlyAggregate;
import viewmodel.YearlyAggregate;

@Controller
public class ProductReportsController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private DisplayNamesRepository displayNamesRepository;

    @Autowired
    private ReportConfigRepository reportConfigRepository;

    private final String ALL_PRODUCTS = "allProducts";
    private final String WHOLE_BLOOD = "wholeBlood";
    private final String RCC = "rcc";
    private final String FFP = "ffp";
    private final String PLATELETS = "platelets";
    private final String PARTIAL_PLATELETS = "partialPlatelets";

    @RequestMapping("/productReport")
    public ModelAndView getCollectionReportsPage(HttpServletRequest httpServletRequest) {
        LoggerUtil.logUrl(httpServletRequest);
        ModelAndView modelAndView = new ModelAndView("productReport");
        Map<String, Object> model = new HashMap<String, Object>();
        ControllerUtil.addReportsDisplayNamesToModel(model, displayNamesRepository);
        modelAndView.addObject("model", model);

        return modelAndView;
    }

    @RequestMapping("/getProductReport")
    public ModelAndView getProductReport(@RequestParam Map<String, String> params, HttpServletRequest request) {

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        Date fromDate = getDate(params.get("collectionFromDate"));
        Date toDate = getDate(params.get("collectionToDate"));
        ModelAndView modelAndView = new ModelAndView("productReport");
        Map<String, Object> model = new HashMap<String, Object>();

        List<Product> products = productRepository.getProducts(fromDate, toDate);
        if (products != null && products.size() > 0) {

            ArrayList<Product> filteredProducts = applyProductFilters(products, params);
            Collections.sort(filteredProducts, new Comparator<Product>() {
                public int compare(Product product, Product product1) {
                    return product.getDateCollected().compareTo(product1.getDateCollected());
                }
            });


            model.put("products", filteredProducts);
            String productAggregateType = params.get("productAggregateType");
            if (productAggregateType.equals("")) {
                model.put("hasProductReport", true);
//            } else if (productAggregateType.equals("") && numberOfProductTypesSelected(params) > 1) {
                String chartName = "productReportChart" + user.getUsername();
                model.put("productReportChartName", chartName);
                createGraphForProducts(params, getProductCountsByType(products), chartName);
                model.put("hasProductReportGraph", true);
            } else if (productAggregateType.equals("daily")) {
                DailyAggregate dailyAggregates = DailyAggregate.createWithProducts(filteredProducts);
                model.put("hasDailyCollectionReport", true);
                LinkedHashMap<String, String> aggregates = dailyAggregates.getAggregates();
                model.put("dailyCollectionAggregates", aggregates);
                String chartFilename = "productReportChart" + user.getUsername();
                model.put("hasProductReportGraph", true);
                model.put("productReportChartName", chartFilename);
                ChartUtil.createDailyChart(dailyAggregates, chartFilename, "Products");
            } else if (productAggregateType.equals("monthly")) {
                MonthlyAggregate monthlyAggregate = MonthlyAggregate.createWithProducts(filteredProducts);
                model.put("hasMonthlyCollectionReport", true);
                LinkedHashMap<String, String> aggregates = monthlyAggregate.getAggregates();
                model.put("monthlyCollectionAggregates", aggregates);
                String chartFilename = "productReportChart" + user.getUsername();
                model.put("hasProductReportGraph", true);
                model.put("productReportChartName", chartFilename);
                ChartUtil.createMonthlyChart(monthlyAggregate, chartFilename, "Products");
            } else if (productAggregateType.equals("yearly")) {
                YearlyAggregate yearlyAggregate = YearlyAggregate.createWithProducts(filteredProducts);
                model.put("hasYearlyCollectionReport", true);
                LinkedHashMap<String, String> aggregates = yearlyAggregate.getAggregates();
                model.put("yearlyCollectionAggregates", aggregates);
                String chartFilename = "productReportChart" + user.getUsername();
                model.put("hasProductReportGraph", true);
                model.put("productReportChartName", chartFilename);
                ChartUtil.createYearlyChart(yearlyAggregate, chartFilename, "Products");
            }
        } else {
            model.put("noProductsFound", true);
        }
        model.put("hasProductDetails", true);
        model.put(ALL_PRODUCTS, params.get(ALL_PRODUCTS));
        model.put(WHOLE_BLOOD, params.get(WHOLE_BLOOD));
        model.put(RCC, params.get(RCC));
        model.put(FFP, params.get(FFP));
        model.put(PLATELETS, params.get(PLATELETS));
        model.put(PARTIAL_PLATELETS, params.get(PARTIAL_PLATELETS));
        model.put("fromDate", params.get("collectionFromDate"));
        model.put("toDate", params.get("collectionToDate"));
        model.put("productAggregateType", params.get("productAggregateType"));

        ControllerUtil.addProductDisplayNamesToModel(model, displayNamesRepository);
        ControllerUtil.addProductReportConfigFieldsToModel(model, reportConfigRepository);
        ControllerUtil.addReportsDisplayNamesToModel(model, displayNamesRepository);

        modelAndView.addObject("model", model);

        return modelAndView;
    }

    private void createGraphForProducts(Map<String, String> params, LinkedHashMap<String, Integer> productCountsByType, String chartName) {
        final DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        if ("true".equals(params.get("allProducts"))) {
            for (String productType : productCountsByType.keySet()) {
                dataset.addValue(productCountsByType.get(productType), "", productType);
            }
        } else {
            for (String productType : productCountsByType.keySet()) {
                if ("true".equals(params.get(productType))) {
                    dataset.addValue(productCountsByType.get(productType), "", productType);
                }
            }
        }

        ChartUtil.createBarGraph(chartName, dataset, "Product Type", "Products");
    }


    private int numberOfProductTypesSelected(Map<String, String> params) {
        int count = 0;
        if ("true".equals(params.get(ALL_PRODUCTS))) {
            count = 4;
            return count;
        }
        if ("true".equals(params.get(WHOLE_BLOOD))) {
            count++;
        }
        if ("true".equals(params.get(RCC))) {
            count++;
        }
        if ("true".equals(params.get(FFP))) {
            count++;
        }
        if ("true".equals(params.get(PLATELETS))) {
            count++;
        }
        if ("true".equals(params.get(PARTIAL_PLATELETS))) {
            count++;
        }
        return count;
    }

    private ArrayList<Product> applyProductFilters(List<Product> products, Map<String, String> params) {
        ArrayList<Product> filteredProducts = new ArrayList<Product>();
        if ("true".equals(params.get(ALL_PRODUCTS))) {
            filteredProducts.addAll(products);
            return filteredProducts;
        }
        if ("true".equals(params.get(WHOLE_BLOOD))) {
            filteredProducts.addAll(filterProductsByType(WHOLE_BLOOD, products));
        }
        if ("true".equals(params.get(RCC))) {
            filteredProducts.addAll(filterProductsByType(RCC, products));
        }
        if ("true".equals(params.get(FFP))) {
            filteredProducts.addAll(filterProductsByType(FFP, products));
        }
        if ("true".equals(params.get(PLATELETS))) {
            filteredProducts.addAll(filterProductsByType(PLATELETS, products));
        }
        if ("true".equals(params.get(PARTIAL_PLATELETS))) {
            filteredProducts.addAll(filterProductsByType(PARTIAL_PLATELETS, products));
        }
        return filteredProducts;
    }

    private List<Product> filterProductsByType(final String type, List<Product> products) {
        List<Product> selectedProducts = (List<Product>) CollectionUtils.select(products, new Predicate() {
            public boolean evaluate(Object o) {
                Product product = (Product) o;
                if (type.equals(product.getType())) {
                    return true;
                }
                return false;
            }
        });
        return selectedProducts;
    }

    private Date getDate(String dateParam) {
        DateFormat formatter;
        formatter = new SimpleDateFormat("MM/dd/yyyy");
        Date date = null;
        try {
            String dateEntered = dateParam;
            if (dateEntered.length() > 0) {
                date = (Date) formatter.parse(dateEntered);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    private LinkedHashMap<String, Integer> getProductCountsByType(List<Product> products) {
        LinkedHashMap<String, Integer> productCounts = new LinkedHashMap<String, Integer>();
        List<Product> wholeBlood = filterProductsByType(WHOLE_BLOOD, products);
        productCounts.put(WHOLE_BLOOD, wholeBlood.size());

        List<Product> rcc = filterProductsByType(RCC, products);
        productCounts.put(RCC, rcc.size());

        List<Product> ffp = filterProductsByType(FFP, products);
        productCounts.put(FFP, ffp.size());

        List<Product> platelets = filterProductsByType(PLATELETS, products);
        productCounts.put(PLATELETS, platelets.size());

        List<Product> partialPlatelets = filterProductsByType(PARTIAL_PLATELETS, products);
        productCounts.put(PARTIAL_PLATELETS, partialPlatelets.size());

        return productCounts;
    }
}
