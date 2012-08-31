package viewmodel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

import model.Collection;
import model.Product;
import model.TestResult;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.Transformer;
import org.joda.time.DateTime;

public class DailyAggregate {
	private HashMap<Date, Integer> aggregates;

	private DailyAggregate() {
	}

	public static DailyAggregate createWithCollections(
			ArrayList<Collection> collections) {
		DailyAggregate dailyAggregate = new DailyAggregate();
		dailyAggregate.aggregates = new HashMap<Date, Integer>();
		List<Date> dateList = (List<Date>) CollectionUtils.collect(collections,
				new Transformer() {
					public Object transform(Object o) {
						Collection collection = (Collection) o;
						return collection.getDateCollected();
					}
				});
		HashSet<Date> uniqueDates = new HashSet<Date>(dateList);
		for (Date uniqueDate : uniqueDates) {
			final Date collectionDate = uniqueDate;
			List<Collection> dailyCollections = (List<Collection>) CollectionUtils
					.select(collections, new Predicate() {
						public boolean evaluate(Object o) {
							Collection collection = (Collection) o;
							if (collection.getDateCollected().equals(
									collectionDate)) {
								return true;
							}
							return false;
						}
					});
			dailyAggregate.aggregates.put(uniqueDate, dailyCollections.size());
		}
		return dailyAggregate;
	}

	public static DailyAggregate createWithProducts(
			ArrayList<Product> filteredProducts) {
		DailyAggregate dailyAggregate = new DailyAggregate();
		dailyAggregate.aggregates = new HashMap<Date, Integer>();
		List<Date> dateList = (List<Date>) CollectionUtils.collect(
				filteredProducts, new Transformer() {
					public Object transform(Object o) {
						Product product = (Product) o;
						return product.getDateCollected();
					}
				});
		HashSet<Date> uniqueDates = new HashSet<Date>(dateList);
		for (Date uniqueDate : uniqueDates) {
			final Date collectionDate = uniqueDate;
			List<Product> dailyProducts = (List<Product>) CollectionUtils
					.select(filteredProducts, new Predicate() {
						public boolean evaluate(Object o) {
							Product product = (Product) o;
							if (product.getDateCollected().equals(
									collectionDate)) {
								return true;
							}
							return false;
						}
					});
			dailyAggregate.aggregates.put(uniqueDate, dailyProducts.size());
		}
		return dailyAggregate;
	}

	public static DailyAggregate createWithTestResults(
			ArrayList<TestResult> filteredTestResults) {
		DailyAggregate dailyAggregate = new DailyAggregate();
		dailyAggregate.aggregates = new HashMap<Date, Integer>();
		List<Date> dateList = (List<Date>) CollectionUtils.collect(
				filteredTestResults, new Transformer() {
					public Object transform(Object o) {
						TestResult testResult = (TestResult) o;
						return testResult.getDateCollected();
					}
				});
		HashSet<Date> uniqueDates = new HashSet<Date>(dateList);
		for (Date uniqueDate : uniqueDates) {
			final Date collectionDate = uniqueDate;
			List<TestResult> dailyTestResults = (List<TestResult>) CollectionUtils
					.select(filteredTestResults, new Predicate() {
						public boolean evaluate(Object o) {
							TestResult testResult = (TestResult) o;
							if (testResult.getDateCollected().equals(
									collectionDate)) {
								return true;
							}
							return false;
						}
					});
			dailyAggregate.aggregates.put(uniqueDate, dailyTestResults.size());
		}
		return dailyAggregate;
	}

	public LinkedHashMap<String, String> getAggregates() {
		LinkedHashMap<String, String> viewAggregates = new LinkedHashMap<String, String>();
		DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
		Set<Date> dates = aggregates.keySet();
		ArrayList<Date> sortedDates = new ArrayList<Date>(dates);
		Collections.sort(sortedDates);
		for (Date date : sortedDates) {
			viewAggregates.put(formatter.format(date), aggregates.get(date)
					.toString());
		}
		return viewAggregates;
	}

	public LinkedHashMap<Date, Integer> getOrderedAggregates() {
		LinkedHashMap<Date, Integer> orderedAggregates = new LinkedHashMap<Date, Integer>();
		Set<Date> dates = aggregates.keySet();
		ArrayList<Date> sortedDates = new ArrayList<Date>(dates);
		Collections.sort(sortedDates);
		for (Date date : sortedDates) {
			orderedAggregates.put(date, aggregates.get(date));
		}
		return orderedAggregates;
	}

	public LinkedHashMap<Date, Integer> getFilledDailyAggregate(
			LinkedHashMap<Date, Integer> orderedAggregates) {
		LinkedHashMap<Date, Integer> filledOrderedAggregates = new LinkedHashMap<Date, Integer>();
		TreeMap<Date, Integer> treeMap = new TreeMap(orderedAggregates);

		DateTime startDate = new DateTime(Collections.min(orderedAggregates
				.keySet()));

		DateTime endDate = new DateTime(Collections.max(orderedAggregates
				.keySet()));

		DateTime currentDate = startDate;

		while (currentDate.getMillis() != endDate.getMillis()) {
			Date date = currentDate.toDate();
			if (treeMap.get(date) == null) {
				filledOrderedAggregates.put(date, 0);
			} else {
				filledOrderedAggregates.put(date, treeMap.get(date));
			}
			currentDate = currentDate.plusDays(1);
		}
		return filledOrderedAggregates;
	}
}
