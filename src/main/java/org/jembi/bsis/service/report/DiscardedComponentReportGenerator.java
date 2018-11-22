package org.jembi.bsis.service.report;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.jembi.bsis.constant.CohortConstants;
import org.jembi.bsis.dto.DiscardedComponentDTO;
import org.jembi.bsis.factory.LocationFactory;
import org.jembi.bsis.model.reporting.Cohort;
import org.jembi.bsis.model.reporting.Comparator;
import org.jembi.bsis.model.reporting.DataValue;
import org.jembi.bsis.model.reporting.Report;
import org.jembi.bsis.repository.ComponentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DiscardedComponentReportGenerator {

  @Autowired
  private ComponentRepository componentRepository;

  @Autowired
  private LocationFactory locationFactory;

  /**
   * Report summary of all discarded components by location, component type and status change reason
   * (discard reason).
   * 
   * @param processingSiteId - The location ID of the processing site
   * @param startDate - The report start date
   * @param endDate - The report end date
   * @return the report
   */
  public Report generateDiscardedComponents(UUID processingSiteId, Date startDate, Date endDate) {
    Report report = new Report();

    report.setStartDate(startDate);
    report.setEndDate(endDate);
    report.setDataValues(getDiscardedComponentsDataValues(processingSiteId, startDate, endDate));
    report.sortDataValuesByVenue();

    return report;
  }

  private List<DataValue> getDiscardedComponentsDataValues(UUID processingSiteId, Date startDate, Date endDate) {
    List<DiscardedComponentDTO> dtos =
        componentRepository.findSummaryOfDiscardedComponentsByProcessingSite(processingSiteId, startDate, endDate);

    List<DataValue> dataValues = new ArrayList<>(dtos.size());

    for (DiscardedComponentDTO dto : dtos) {

      DataValue dataValue = new DataValue();
      dataValue.setStartDate(startDate);
      dataValue.setEndDate(endDate);
      dataValue.setLocation(locationFactory.createViewModel(dto.getVenue()));
      dataValue.setValue(dto.getCount());

      Cohort componentTypeCohort = new Cohort();
      componentTypeCohort.setCategory(CohortConstants.COMPONENT_TYPE_CATEGORY);
      componentTypeCohort.setComparator(Comparator.EQUALS);
      componentTypeCohort.setOption(dto.getComponentType());
      dataValue.addCohort(componentTypeCohort);

      Cohort statusChangeReasonCohort = new Cohort();
      statusChangeReasonCohort.setCategory(CohortConstants.DISCARD_REASON_CATEGORY);
      statusChangeReasonCohort.setComparator(Comparator.EQUALS);
      statusChangeReasonCohort.setOption(dto.getComponentStatusChangeReason());
      dataValue.addCohort(statusChangeReasonCohort);

      dataValues.add(dataValue);
    }

    return dataValues;
  }
}