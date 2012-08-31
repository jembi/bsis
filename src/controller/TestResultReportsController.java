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

import model.TestResult;
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
import repository.ReportConfigRepository;
import repository.TestResultRepository;
import utils.ChartUtil;
import utils.ControllerUtil;
import viewmodel.DailyAggregate;
import viewmodel.MonthlyAggregate;
import viewmodel.TestResultViewModel;
import viewmodel.YearlyAggregate;

@Controller
public class TestResultReportsController {

	@Autowired
	private TestResultRepository testResultRepository;

	@Autowired
	private ReportConfigRepository reportConfigRepository;

	@Autowired
	private DisplayNamesRepository displayNamesRepository;

	@RequestMapping("/testResultReport")
	public ModelAndView getCollectionReportsPage(HttpServletRequest request) {

		ModelAndView modelAndView = new ModelAndView("testResultReport");
		Map<String, Object> model = new HashMap<String, Object>();
		ControllerUtil.addReportsDisplayNamesToModel(model,
				displayNamesRepository);
		modelAndView.addObject("model", model);

		return modelAndView;
	}

	@RequestMapping("/getTestResultReport")
	public ModelAndView getCollectionReport(
			@RequestParam Map<String, String> params, HttpServletRequest request) {

		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		Date fromDate = getDate(params.get("collectionFromDate"));
		Date toDate = getDate(params.get("collectionToDate"));
		ModelAndView modelAndView = new ModelAndView("testResultReport");
		Map<String, Object> model = new HashMap<String, Object>();

		List<TestResult> testResults = testResultRepository.getTestResults(
				fromDate, toDate);
		if (testResults != null && testResults.size() > 0) {

			ArrayList<TestResult> filteredTestResults = applyFilters(
					testResults, params);

			Collections.sort(filteredTestResults, new Comparator<TestResult>() {
				public int compare(TestResult testResult, TestResult testResult1) {
					return testResult.getDateCollected().compareTo(
							testResult1.getDateCollected());
				}
			});

			if (params.get("testResultsAggregateType").equals("")) {
				model.put("reportTestResults",
						getTestResultViewModels(filteredTestResults));
				// if (numberOfIndicatorsSelected(params) <= 1) {
				model.put("hasTestResultsReport", true);
				// } else {
				model.put("hasTestResultsGraph", true);
				String chartFilename = "indicatorBarChart" + user.getUsername();
				model.put("chartName", chartFilename);
				createGraphForIndicators(params, testResults, chartFilename);
				// }
			} else if (params.get("testResultsAggregateType").equals("daily")) {
				DailyAggregate dailyAggregates = DailyAggregate
						.createWithTestResults(filteredTestResults);
				model.put("hasDailyCollectionReport", true);
				LinkedHashMap<String, String> aggregates = dailyAggregates
						.getAggregates();
				model.put("dailyCollectionAggregates", aggregates);
				String chartFilename = "dailyCollectionsIndicatorChart"
						+ user.getUsername();
				model.put("hasTestResultsGraph", true);
				model.put("chartName", chartFilename);
				createDailyGraphForIndicators(dailyAggregates, chartFilename);
			} else if (params.get("testResultsAggregateType").equals("monthly")) {
				MonthlyAggregate monthlyAggregate = MonthlyAggregate
						.createWithTestResults(filteredTestResults);
				model.put("hasMonthlyCollectionReport", true);
				LinkedHashMap<String, String> aggregates = monthlyAggregate
						.getAggregates();
				model.put("monthlyCollectionAggregates", aggregates);
				String chartFilename = "monthlyCollectionsIndicatorChart"
						+ user.getUsername();
				model.put("hasTestResultsGraph", true);
				model.put("chartName", chartFilename);
				createMonthlyGraphForIndicators(monthlyAggregate, chartFilename);

			} else if (params.get("testResultsAggregateType").equals("yearly")) {
				YearlyAggregate yearlyAggregate = YearlyAggregate
						.createWithTestResults(filteredTestResults);
				model.put("hasYearlyCollectionReport", true);
				LinkedHashMap<String, String> aggregates = yearlyAggregate
						.getAggregates();
				model.put("yearlyCollectionAggregates", aggregates);

				String chartFilename = "yearlyCollectionsIndicatorChart"
						+ user.getUsername();
				model.put("hasTestResultsGraph", true);
				model.put("chartName", chartFilename);
				createYearlyGraphForIndicators(yearlyAggregate, chartFilename);
			}
		} else {
			model.put("noTestResultsFound", true);
		}
		model.put("hasTestResultsDetails", true);
		model.put("fromDate", params.get("collectionFromDate"));
		model.put("toDate", params.get("collectionToDate"));
		model.put("hiv", params.get("hiv"));
		model.put("hbv", params.get("hbv"));
		model.put("hcv", params.get("hcv"));
		model.put("syphilis", params.get("syphilis"));
		model.put("noIndicator", params.get("noIndicator"));
		model.put("allTestResults", params.get("allTestResults"));
		model.put("testResultsAggregateType",
				params.get("testResultsAggregateType"));
		ControllerUtil.addTestResultDisplayNamesToModel(model,
				displayNamesRepository);
		ControllerUtil.addTestResultsReportConfigFieldsToModel(model,
				reportConfigRepository);
		ControllerUtil.addReportsDisplayNamesToModel(model,
				displayNamesRepository);

		modelAndView.addObject("model", model);

		return modelAndView;
	}

	private void createYearlyGraphForIndicators(
			YearlyAggregate yearlyAggregates, String chartFilename) {
		ChartUtil.createYearlyChart(yearlyAggregates, chartFilename,
				"TestResults");
	}

	private void createMonthlyGraphForIndicators(
			MonthlyAggregate monthlyAggregates, String chartFilename) {
		ChartUtil.createMonthlyChart(monthlyAggregates, chartFilename,
				"TestResults");
	}

	private void createDailyGraphForIndicators(DailyAggregate dailyAggregates,
			String chartFilename) {
		ChartUtil.createDailyChart(dailyAggregates, chartFilename,
				"TestResults");
	}

	private void createGraphForIndicators(Map<String, String> params,
			List<TestResult> testResults, String chartFilename) {
		final DefaultCategoryDataset dataset = getDatasetForBarGraph(params,
				testResults);
		ChartUtil.createBarGraph(chartFilename, dataset, "Indicator",
				"TestResults");
	}

	private DefaultCategoryDataset getDatasetForBarGraph(
			Map<String, String> params, List<TestResult> testResults) {
		final DefaultCategoryDataset dataset = new DefaultCategoryDataset();

		if ("true".equals(params.get("allTestResults"))) {
			dataset.addValue(filterCollectionsByIndicator(testResults, "hiv")
					.size(), "", "HIV");
			dataset.addValue(filterCollectionsByIndicator(testResults, "hbv")
					.size(), "", "HBV");
			dataset.addValue(filterCollectionsByIndicator(testResults, "hcv")
					.size(), "", "HCV");
			dataset.addValue(
					filterCollectionsByIndicator(testResults, "syphilis")
							.size(), "", "Syphilis");
			dataset.addValue(filterCollectionsByIndicator(testResults, "none")
					.size(), "", "None");
		} else {
			if ("true".equals(params.get("hiv"))) {
				dataset.addValue(
						filterCollectionsByIndicator(testResults, "hiv").size(),
						"", "HIV");
			}
			if ("true".equals(params.get("hbv"))) {
				dataset.addValue(
						filterCollectionsByIndicator(testResults, "hbv").size(),
						"", "HBV");
			}
			if ("true".equals(params.get("hcv"))) {
				dataset.addValue(
						filterCollectionsByIndicator(testResults, "hcv").size(),
						"", "HCV");
			}
			if ("true".equals(params.get("syphilis"))) {
				dataset.addValue(
						filterCollectionsByIndicator(testResults, "syphilis")
								.size(), "", "Syphilis");
			}
			if ("true".equals(params.get("noIndicator"))) {
				dataset.addValue(
						filterCollectionsByIndicator(testResults, "none")
								.size(), "", "None");
			}
		}
		return dataset;
	}

	private int numberOfIndicatorsSelected(Map<String, String> params) {
		int count = 0;
		if ("true".equals(params.get("allTestResults"))) {
			count = 5;
			return count;
		}
		if ("true".equals(params.get("hiv"))) {
			count++;
		}
		if ("true".equals(params.get("hbv"))) {
			count++;
		}
		if ("true".equals(params.get("hcv"))) {
			count++;
		}
		if ("true".equals(params.get("syphilis"))) {
			count++;
		}
		if ("true".equals(params.get("noIndicator"))) {
			count++;
		}
		return count;
	}

	private ArrayList<TestResult> applyFilters(List<TestResult> testResults,
			Map<String, String> params) {
		ArrayList<TestResult> filteredTestResults = new ArrayList<TestResult>();
		if ("true".equals(params.get("allTestResults"))) {
			filteredTestResults.addAll(testResults);
			return filteredTestResults;
		}
		if ("true".equals(params.get("hiv"))) {
			List<TestResult> selectedTestResults = filterCollectionsByIndicator(
					testResults, "hiv");
			filteredTestResults.addAll(selectedTestResults);
		}
		if ("true".equals(params.get("hbv"))) {
			List<TestResult> selectedTestResults = filterCollectionsByIndicator(
					testResults, "hbv");
			filteredTestResults.addAll(selectedTestResults);
		}
		if ("true".equals(params.get("hcv"))) {
			List<TestResult> selectedTestResults = filterCollectionsByIndicator(
					testResults, "hcv");
			filteredTestResults.addAll(selectedTestResults);

		}
		if ("true".equals(params.get("syphilis"))) {
			List<TestResult> selectedTestResults = filterCollectionsByIndicator(
					testResults, "syphilis");
			filteredTestResults.addAll(selectedTestResults);

		}
		if ("true".equals(params.get("noIndicator"))) {
			List<TestResult> selectedTestResults = filterCollectionsByIndicator(
					testResults, "none");
			filteredTestResults.addAll(selectedTestResults);

		}

		return filteredTestResults;
	}

	private List<TestResult> filterCollectionsByIndicator(
			List<TestResult> testResults, final String indicator) {
		return (List<TestResult>) CollectionUtils.select(testResults,
				new Predicate() {
					public boolean evaluate(Object o) {
						TestResult testResult = (TestResult) o;
						if (indicator.equals("hiv")
								&& "reactive".equals(testResult.getHiv())) {
							return true;
						}
						if (indicator.equals("hbv")
								&& "reactive".equals(testResult.getHbv())) {
							return true;
						}
						if (indicator.equals("hcv")
								&& "reactive".equals(testResult.getHcv())) {
							return true;
						}
						if (indicator.equals("syphilis")
								&& "reactive".equals(testResult.getSyphilis())) {
							return true;
						}
						if (indicator.equals("none")
								&& "negative".equals(testResult.getSyphilis())
								&& "negative".equals(testResult.getHiv())
								&& "negative".equals(testResult.getHbv())
								&& "negative".equals(testResult.getHcv())

						) {
							return true;
						}
						return false;
					}
				});
	}

	private List<TestResultViewModel> getTestResultViewModels(
			List<TestResult> testResults) {
		ArrayList<TestResultViewModel> testResultViewModels = new ArrayList<TestResultViewModel>();
		Collections.sort(testResults, new Comparator<TestResult>() {
			public int compare(TestResult testResult, TestResult testResult1) {
				return testResult.getDateCollected().compareTo(
						testResult1.getDateCollected());
			}
		});
		for (TestResult testResult : testResults) {
			testResultViewModels.add(new TestResultViewModel(testResult));
		}
		return testResultViewModels;
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
}
