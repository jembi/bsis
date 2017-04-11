package org.jembi.bsis.service.report;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.jembi.bsis.constant.CohortConstants;
import org.jembi.bsis.dto.DonorsAdverseEventsDTO;
import org.jembi.bsis.factory.LocationFactory;
import org.jembi.bsis.model.reporting.Cohort;
import org.jembi.bsis.model.reporting.Comparator;
import org.jembi.bsis.model.reporting.DataValue;
import org.jembi.bsis.model.reporting.Report;
import org.jembi.bsis.repository.AdverseEventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DonorsAdverseEventsReportGenerator {
  
  @Autowired
  private AdverseEventRepository adverseEventRepository;

  @Autowired
  private LocationFactory locationFactory;

  public Report generateDonorsAdverseEventsReport(UUID venueId, Date startDate, Date endDate) {
    Report report = new Report();

    report.setStartDate(startDate);
    report.setEndDate(endDate);
    report.setDataValues(getAdverseEventsDataValues(venueId, startDate, endDate));
    report.sortDataValuesByVenue();

    return report;
  }

  private List<DataValue> getAdverseEventsDataValues(UUID venueId, Date startDate, Date endDate) {
    List<DonorsAdverseEventsDTO> dtos = adverseEventRepository.countAdverseEvents(venueId, startDate, endDate);

    List<DataValue> dataValues = new ArrayList<>(dtos.size());

    for (DonorsAdverseEventsDTO dto : dtos) {

      DataValue dataValue = new DataValue();
      dataValue.setStartDate(startDate);
      dataValue.setEndDate(endDate);
      dataValue.setLocation(locationFactory.createViewModel(dto.getVenue()));
      dataValue.setValue(dto.getCount());

      Cohort adverseEventCohort = new Cohort();
      adverseEventCohort.setCategory(CohortConstants.ADVERSE_EVENT_TYPE_CATEGORY);
      adverseEventCohort.setComparator(Comparator.EQUALS);
      adverseEventCohort.setOption(dto.getAdverseEventType().getName());
      dataValue.addCohort(adverseEventCohort);
      
      dataValues.add(dataValue);
    }

    return dataValues;
  }
}
