package org.jembi.bsis.service.report;
import org.jembi.bsis.constant.CohortConstants;
import org.jembi.bsis.dto.CollectedDonationDTO;
import org.jembi.bsis.dto.ComponentProductionExportDTO;
import org.jembi.bsis.model.componenttype.ComponentType;
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
import java.util.Set;
@Service
public class ComponentProductionReportGenerator {
  @Autowired
  public ComponentRepository componentRepository;
  /**
   * Report listing summary component production within a selected date range by component processing site,
   * categorised by component type, blood group type, date processed, and processing site.
   *
   * @return The report.
   */
  public Report generateComponentProductionReport(Set<Long> venueIds, Date startDate, Date endDate) {
    Report report = new Report();
    report.setStartDate(startDate);
    report.setEndDate(endDate);

    List<ComponentProductionExportDTO> dtos = new ArrayList<>();

    List<DataValue> dataValues = new ArrayList<>(dtos.size());

    for (ComponentProductionExportDTO dto : dtos) {
      DataValue dataValue = new DataValue();
      dataValue.setStartDate(startDate);
      dataValue.setEndDate(endDate);
      dataValue.setVenue(dto.getVenues());
      dataValue.setValue(dto.getCount());

      Cohort componentTypeCohort = new Cohort();
      componentTypeCohort.setCategory(CohortConstants.COMPONENT_TYPE_CATEGORY);
      componentTypeCohort.setComparator(Comparator.EQUALS);
      ComponentType componentType = dto.getComponentType();
      componentTypeCohort.setOption(componentType.getComponentTypeName());
      dataValue.addCohort(componentTypeCohort);

      Cohort bloodTypeCohort = new Cohort();
      bloodTypeCohort.setCategory(CohortConstants.BLOOD_TYPE_CATEGORY);
      bloodTypeCohort.setComparator(Comparator.EQUALS);
      bloodTypeCohort.setOption(dto.getBloodAbo() + dto.getBloodAbo());
      dataValue.addCohort(bloodTypeCohort);

      dataValues.add(dataValue);
    }

    report.setDataValues(dataValues);

    return report;
  }
}