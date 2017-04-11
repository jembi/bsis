package org.jembi.bsis.service.report;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.UUID;

import org.jembi.bsis.constant.CohortConstants;
import org.jembi.bsis.dto.ComponentProductionDTO;
import org.jembi.bsis.factory.LocationFactory;
import org.jembi.bsis.model.reporting.Cohort;
import org.jembi.bsis.model.reporting.Comparator;
import org.jembi.bsis.model.reporting.DataValue;
import org.jembi.bsis.model.reporting.Report;
import org.jembi.bsis.repository.ComponentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ComponentProductionReportGenerator {

  @Autowired
  private ComponentRepository componentRepository;

  @Autowired
  private LocationFactory locationFactory;

  /**
   * Report summary of components produced within a selected date range by collection site,
   * categorized by component Type type, processing site, and blood groups.
   *
   * @return The report.
   *
   * @param processingSiteId
   * The processing site related to the component batch.
   *
   * @param startDate
   * The period start date for date range.
   * Date range is the range of dates you want the report to be generated for.
   *
   * @param endDate
   * The period end date for date range
   */
  public Report generateComponentProductionReport(UUID processingSiteId, Date startDate, Date endDate) {
    Report report = new Report();
    report.setStartDate(startDate);
    report.setEndDate(endDate);
    report.setDataValues(generateReportDataValues(processingSiteId, startDate, endDate));

    return report;
  }

  private List<DataValue> generateReportDataValues(UUID processingSiteId, Date startDate, Date endDate) {
    List<ComponentProductionDTO> dtos = componentRepository.findProducedComponentsByProcessingSite(
            processingSiteId, startDate, endDate);

    List<DataValue> dataValues = new ArrayList<>(dtos.size());

    for (ComponentProductionDTO dto : dtos) {

      DataValue dataValue = new DataValue();
      dataValue.setStartDate(startDate);
      dataValue.setEndDate(endDate);
      dataValue.setLocation(locationFactory.createViewModel(dto.getProcessingSite()));
      dataValue.setValue(dto.getCount());

      Cohort componentTypeCohort = new Cohort();
      componentTypeCohort.setCategory(CohortConstants.COMPONENT_TYPE_CATEGORY);
      componentTypeCohort.setComparator(Comparator.EQUALS);
      componentTypeCohort.setOption(dto.getComponentTypeName());
      dataValue.addCohort(componentTypeCohort);

      Cohort bloodTypeCohort = new Cohort();
      bloodTypeCohort.setCategory(CohortConstants.BLOOD_TYPE_CATEGORY);
      bloodTypeCohort.setComparator(Comparator.EQUALS);
      bloodTypeCohort.setOption(dto.getBloodAbo() + dto.getBloodRh());
      dataValue.addCohort(bloodTypeCohort);

      dataValues.add(dataValue);
    }

    return dataValues;
  }
}