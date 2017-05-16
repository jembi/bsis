package org.jembi.bsis.service.report;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.jembi.bsis.constant.CohortConstants;
import org.jembi.bsis.dto.TransfusionSummaryDTO;
import org.jembi.bsis.factory.LocationFactory;
import org.jembi.bsis.model.reporting.Cohort;
import org.jembi.bsis.model.reporting.Comparator;
import org.jembi.bsis.model.reporting.DataValue;
import org.jembi.bsis.model.reporting.Report;
import org.jembi.bsis.repository.TransfusionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransfusionSummaryReportGenerator {

  @Autowired
  private TransfusionRepository transfusionRepository;

  @Autowired
  private LocationFactory locationFactory;

  /**
   * Report summary of transfusions within a selected date range by transfusion site,
   * categorized by transfusion site, transfusion reaction type and order type.
   *
   * @return The report.
   *
   * @param transfusionSiteId
   * The transfusion site related to the transfusion.
   *
   * @param startDate
   * The period start date for date range.
   * Date range is the range of dates you want the report to be generated for.
   *
   * @param endDate
   * The period end date for date range
   */
  public Report generateTransfusionSummaryReport(UUID transfusionSiteId, Date startDate, Date endDate) {
    Report report = new Report();
    report.setStartDate(startDate);
    report.setEndDate(endDate);
    report.setDataValues(generateReportDataValues(transfusionSiteId, startDate, endDate));

    return report;
  }

  private List<DataValue> generateReportDataValues(UUID transfusionSiteId, Date startDate, Date endDate) {
    List<TransfusionSummaryDTO> dtos = transfusionRepository.findTransfusionSummaryRecordedForUsageSiteForPeriod(transfusionSiteId, startDate, endDate);

    List<DataValue> dataValues = new ArrayList<>(dtos.size());

    for (TransfusionSummaryDTO dto : dtos) {

      DataValue dataValue = new DataValue();
      dataValue.setStartDate(startDate);
      dataValue.setEndDate(endDate);
      dataValue.setLocation(locationFactory.createViewModel(dto.getTransfusionSite()));
      dataValue.setValue(dto.getCount());

      Cohort transfusionReactionTypeCohort = new Cohort();
      transfusionReactionTypeCohort.setCategory(CohortConstants.TRANSFUSION_REACTION_TYPE_CATEGORY);
      transfusionReactionTypeCohort.setComparator(Comparator.EQUALS);
      if (dto.getTransfusionReactionType() != null) {
        transfusionReactionTypeCohort.setOption(dto.getTransfusionReactionType().getName());
      }
      dataValue.addCohort(transfusionReactionTypeCohort);

      Cohort transfusionOutcomeCohort = new Cohort();
      transfusionOutcomeCohort.setCategory(CohortConstants.TRANSFUSION_OUTCOME_CATEGORY);
      transfusionOutcomeCohort.setComparator(Comparator.EQUALS);
      transfusionOutcomeCohort.setOption(dto.getTransfusionOutcome());
      dataValue.addCohort(transfusionOutcomeCohort);

      dataValues.add(dataValue);
    }

    return dataValues;
  }
}