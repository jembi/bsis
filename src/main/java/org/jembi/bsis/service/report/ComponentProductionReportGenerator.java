package org.jembi.bsis.service.report;

import org.jembi.bsis.constant.CohortConstants;
import org.jembi.bsis.dto.CollectedDonationDTO;
import org.jembi.bsis.dto.ComponentProductionDTO;
import org.jembi.bsis.model.component.Component;
import org.jembi.bsis.model.donationtype.DonationType;
import org.jembi.bsis.model.reporting.Cohort;
import org.jembi.bsis.model.reporting.Comparator;
import org.jembi.bsis.model.reporting.DataValue;
import org.jembi.bsis.model.reporting.Report;
import org.jembi.bsis.repository.ComponentRepository;
import org.jembi.bsis.repository.DonationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ComponentProductionReportGenerator {

  @Autowired
  public ComponentRepository componentRepository;

  /**
   * Report summary of components produced within a selected date range by collection site,
   * categorised by donation donor type, donor gender, and blood groups.
   *
   * @return The report.
   */
  public Report generateCollectedDonationsReport(Date startDate, Date endDate) {
    Report report = new Report();
    report.setStartDate(startDate);
    report.setEndDate(endDate);

    List<ComponentProductionDTO> dtos = componentRepository.findProducedComponentsByDate(
        startDate, endDate);

    List<DataValue> dataValues = new ArrayList<>(dtos.size());

    for (ComponentProductionDTO dto : dtos) {

      DataValue dataValue = new DataValue();
      dataValue.setStartDate(startDate);
      dataValue.setEndDate(endDate);
      dataValue.setVenue(dto.getVenue());
      dataValue.setValue(dto.getCount());

      Cohort genderCohort = new Cohort();
      genderCohort.setCategory(CohortConstants.COMPONENT_TYPE_CATEGORY);
      genderCohort.setComparator(Comparator.EQUALS);
      genderCohort.setOption(dto.getComponentTypeName());
      dataValue.addCohort(genderCohort);

      Cohort bloodTypeCohort = new Cohort();
      bloodTypeCohort.setCategory(CohortConstants.BLOOD_TYPE_CATEGORY);
      bloodTypeCohort.setComparator(Comparator.EQUALS);
      bloodTypeCohort.setOption(dto.getBloodAbo() + dto.getBloodRh());
      dataValue.addCohort(bloodTypeCohort);

      dataValues.add(dataValue);
    }

    report.setDataValues(dataValues);

    return report;
  }
}