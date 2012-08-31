package utils;

import org.jfree.data.category.DefaultCategoryDataset;

import viewmodel.DailyAggregate;
import viewmodel.MonthlyAggregate;
import viewmodel.YearlyAggregate;

public class ChartUtil {

	private static String fileBasePath = "../webapps/v2v/charts";

	// NOTE: uncomment for XAMPP version
	// private static String fileBasePath = "tomcat\\webapps\\v2v\\charts";

	public static void createDailyChart(DailyAggregate dailyAggregates,
			String chartFilename, String recordType) {
		// TimeSeries timeSeries = new TimeSeries("");
		// LinkedHashMap<Date, Integer> orderedAggregates =
		// dailyAggregates.getOrderedAggregates();
		// LinkedHashMap<Date, Integer> filledOrderedAggregates =
		// dailyAggregates.getFilledDailyAggregate(orderedAggregates);
		// for (Date date : filledOrderedAggregates.keySet()) {
		// timeSeries.add(new Day(date), filledOrderedAggregates.get(date));
		// }
		//
		// TimeSeriesCollection dataset = new TimeSeriesCollection();
		// dataset.addSeries(timeSeries);
		//
		// JFreeChart chart = ChartFactory.createTimeSeriesChart("", "Date",
		// recordType, dataset, false, false, false);
		// XYPlot plot = (XYPlot) chart.getPlot();
		// ValueAxis rangeAxis = plot.getRangeAxis();
		// rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
		// try {
		// ChartRenderingInfo info = new ChartRenderingInfo
		// (new StandardEntityCollection());
		// File file1 = new File(fileBasePath, chartFilename + ".png");
		// ChartUtilities.saveChartAsPNG(file1, chart, 600, 400, info);
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
	}

	public static void createMonthlyChart(MonthlyAggregate monthlyAggregates,
			String chartFilename, String recordType) {
		// TimeSeries timeSeries = new TimeSeries("");
		// LinkedHashMap<Date, Integer> orderedAggregates =
		// monthlyAggregates.getOrderedAggregates();
		// LinkedHashMap<Date, Integer> filledMonthlyAggregate =
		// monthlyAggregates.getFilledMonthlyAggregate(orderedAggregates);
		// for (Date date : filledMonthlyAggregate.keySet()) {
		// timeSeries.add(new Month(date), filledMonthlyAggregate.get(date));
		// }
		//
		// TimeSeriesCollection dataset = new TimeSeriesCollection();
		// dataset.addSeries(timeSeries);
		//
		// JFreeChart chart = ChartFactory.createTimeSeriesChart("", "Month",
		// recordType, dataset, false, false, false);
		// XYPlot plot = (XYPlot) chart.getPlot();
		// ValueAxis rangeAxis = plot.getRangeAxis();
		// rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
		// // DateAxis axis = (DateAxis) plot.getDomainAxis();
		// // axis.setTickUnit(new DateTickUnit(DateTickUnitType.MONTH, 1, new
		// SimpleDateFormat("MM/yyyy")));
		//
		// try {
		// ChartRenderingInfo info = new ChartRenderingInfo
		// (new StandardEntityCollection());
		// File file1 = new File(fileBasePath, chartFilename + ".png");
		// ChartUtilities.saveChartAsPNG(file1, chart, 600, 400, info);
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
	}

	public static void createYearlyChart(YearlyAggregate yearlyAggregates,
			String chartFilename, String recordType) {
		// TimeSeries timeSeries = new TimeSeries("");
		// LinkedHashMap<Date, Integer> orderedAggregates =
		// yearlyAggregates.getOrderedAggregates();
		// LinkedHashMap<Date, Integer> filledYearlyAggregate =
		// yearlyAggregates.getFilledYearlyAggregate(orderedAggregates);
		// for (Date date : filledYearlyAggregate.keySet()) {
		// timeSeries.add(new Year(date), filledYearlyAggregate.get(date));
		// }
		//
		// TimeSeriesCollection dataset = new TimeSeriesCollection();
		// dataset.addSeries(timeSeries);
		//
		// JFreeChart chart = ChartFactory.createTimeSeriesChart("", "Year",
		// recordType, dataset, false, false, false);
		// XYPlot plot = (XYPlot) chart.getPlot();
		// ValueAxis rangeAxis = plot.getRangeAxis();
		// rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
		// DateAxis axis = (DateAxis) plot.getDomainAxis();
		// axis.setTickUnit(new DateTickUnit(DateTickUnitType.YEAR, 1, new
		// SimpleDateFormat("yyyy")));
		// try {
		// ChartRenderingInfo info = new ChartRenderingInfo
		// (new StandardEntityCollection());
		// File file1 = new File(fileBasePath, chartFilename + ".png");
		// ChartUtilities.saveChartAsPNG(file1, chart, 600, 400, info);
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
	}

	public static void createBarGraph(String chartFilename,
			DefaultCategoryDataset dataset, String categories, String recordType) {
		// JFreeChart chart = null;
		// BarRenderer renderer = null;
		// CategoryPlot plot = null;
		//
		//
		// final CategoryAxis categoryAxis = new CategoryAxis(categories);
		// final NumberAxis valueAxis = new NumberAxis(recordType);
		// valueAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
		// renderer = new BarRenderer();
		//
		// plot = new CategoryPlot(dataset, categoryAxis, valueAxis,
		// renderer);
		// plot.setOrientation(PlotOrientation.VERTICAL);
		// chart = new JFreeChart("", JFreeChart.DEFAULT_TITLE_FONT,
		// plot, false);
		// renderer.setSeriesPaint(0, Color.DARK_GRAY);
		// plot.setRenderer(renderer);
		//
		// try {
		// final ChartRenderingInfo info = new ChartRenderingInfo
		// (new StandardEntityCollection());
		// File file1 = new File(fileBasePath, chartFilename + ".png");
		// ChartUtilities.saveChartAsPNG(file1, chart, 600, 400, info);
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
	}

}
