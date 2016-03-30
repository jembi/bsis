package service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import constant.CohortConstants;
import dto.CollectedDonationDTO;
import model.donationtype.DonationType;
import model.reporting.Cohort;
import model.reporting.Comparator;
import model.reporting.Indicator;
import model.reporting.Report;
import repository.DonationRepository;

@Service
public class ReportGeneratorService {

  @Autowired
  private DonationRepository donationRepository;

  /**
   * Report listing all donations collected within a selected date range by collection site,
   * categorised by donation donor type, donor gender, and blood groups.
   *
   * @return The report.
   */
  public Report generateCollectedDonationsReport(Date startDate, Date endDate) {
    Report report = new Report();
    report.setStartDate(startDate);
    report.setEndDate(endDate);

    List<CollectedDonationDTO> dtos = donationRepository.findCollectedDonationsReportIndicators(
        startDate, endDate);

    List<Indicator> indicators = new ArrayList<>(dtos.size());

    for (CollectedDonationDTO dto : dtos) {

      Indicator indicator = new Indicator();
      indicator.setStartDate(startDate);
      indicator.setEndDate(endDate);
      indicator.setVenue(dto.getVenue());
      indicator.setValue(dto.getCount());

      Cohort donationTypeCohort = new Cohort();
      donationTypeCohort.setCategory(CohortConstants.DONATION_TYPE_CATEGORY);
      donationTypeCohort.setComparator(Comparator.EQUALS);
      DonationType donationType = dto.getDonationType();
      donationTypeCohort.setOption(donationType.getDonationType());
      indicator.addCohort(donationTypeCohort);

      Cohort genderCohort = new Cohort();
      genderCohort.setCategory(CohortConstants.GENDER_CATEGORY);
      genderCohort.setComparator(Comparator.EQUALS);
      genderCohort.setOption(dto.getGender());
      indicator.addCohort(genderCohort);

      Cohort bloodTypeCohort = new Cohort();
      bloodTypeCohort.setCategory(CohortConstants.BLOOD_TYPE_CATEGORY);
      bloodTypeCohort.setComparator(Comparator.EQUALS);
      bloodTypeCohort.setOption(dto.getBloodAbo() + dto.getBloodRh());
      indicator.addCohort(bloodTypeCohort);

      indicators.add(indicator);
    }

    report.setIndicators(indicators);

    return report;
  }

}
