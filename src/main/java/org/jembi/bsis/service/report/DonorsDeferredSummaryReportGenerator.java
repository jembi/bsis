package org.jembi.bsis.service.report;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.jembi.bsis.constant.CohortConstants;
import org.jembi.bsis.dto.DeferredDonorsDTO;
import org.jembi.bsis.factory.LocationFactory;
import org.jembi.bsis.model.reporting.Cohort;
import org.jembi.bsis.model.reporting.Comparator;
import org.jembi.bsis.model.reporting.DataValue;
import org.jembi.bsis.model.reporting.Report;
import org.jembi.bsis.repository.DonorDeferralRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DonorsDeferredSummaryReportGenerator {
  
  @Autowired
  private DonorDeferralRepository donorDeferralRepository;

  @Autowired
  private LocationFactory locationFactory;
  
  public Report generateDonorDeferralSummaryReport(Date startDate, Date endDate) {
    Report report = new Report();

    report.setStartDate(startDate);
    report.setEndDate(endDate);
    report.setDataValues(getDeferredDonorsDataValues(startDate, endDate));

    return report;
  }

  private List<DataValue> getDeferredDonorsDataValues(Date startDate, Date endDate) {
    List<DeferredDonorsDTO> dtos = donorDeferralRepository.countDeferredDonors(startDate, endDate);

    List<DataValue> dataValues = new ArrayList<>(dtos.size());

    for (DeferredDonorsDTO dto : dtos) {

      DataValue dataValue = new DataValue();
      dataValue.setStartDate(startDate);
      dataValue.setEndDate(endDate);
      dataValue.setLocation(locationFactory.createViewModel(dto.getVenue()));
      dataValue.setValue(dto.getCount());

      Cohort genderCohort = new Cohort();
      genderCohort.setCategory(CohortConstants.GENDER_CATEGORY);
      genderCohort.setComparator(Comparator.EQUALS);
      genderCohort.setOption(dto.getGender());
      dataValue.addCohort(genderCohort);

      Cohort deferralReasonCohort = new Cohort();
      deferralReasonCohort.setCategory(CohortConstants.DEFERRAL_REASON_CATEGORY);
      deferralReasonCohort.setComparator(Comparator.EQUALS);
      deferralReasonCohort.setOption(dto.getDeferralReason().getReason());
      dataValue.addCohort(deferralReasonCohort);
      
      dataValues.add(dataValue);
    }

    return dataValues;
  }
}
