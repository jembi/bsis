package service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import constant.CohortConstants;
import dto.CollectedDonationDTO;
import dto.StockLevelDTO;
import model.donationtype.DonationType;
import model.inventory.InventoryStatus;
import model.location.Location;
import model.reporting.Cohort;
import model.reporting.Comparator;
import model.reporting.DataValue;
import model.reporting.Report;
import repository.DonationRepository;
import repository.InventoryRepository;

@Service
public class ReportGeneratorService {

  @Autowired
  private DonationRepository donationRepository;
  
  @Autowired
  private InventoryRepository inventoryRepository;

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

    List<DataValue> dataValues = new ArrayList<>(dtos.size());

    for (CollectedDonationDTO dto : dtos) {

      DataValue dataValue = new DataValue();
      dataValue.setStartDate(startDate);
      dataValue.setEndDate(endDate);
      dataValue.setVenue(dto.getVenue());
      dataValue.setValue(dto.getCount());

      Cohort donationTypeCohort = new Cohort();
      donationTypeCohort.setCategory(CohortConstants.DONATION_TYPE_CATEGORY);
      donationTypeCohort.setComparator(Comparator.EQUALS);
      DonationType donationType = dto.getDonationType();
      donationTypeCohort.setOption(donationType.getDonationType());
      dataValue.addCohort(donationTypeCohort);

      Cohort genderCohort = new Cohort();
      genderCohort.setCategory(CohortConstants.GENDER_CATEGORY);
      genderCohort.setComparator(Comparator.EQUALS);
      genderCohort.setOption(dto.getGender());
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
  
  public Report generateStockLevelsReport(Location location, InventoryStatus inventoryStatus) {
    Report report = new Report();

    List<StockLevelDTO> dtos = inventoryRepository.findStockLevelsForLocation(location, inventoryStatus);

    List<DataValue> dataValues = new ArrayList<>(dtos.size());

    for (StockLevelDTO dto : dtos) {

      DataValue dataValue = new DataValue();
      dataValue.setVenue(dto.getLocation());
      dataValue.setValue(dto.getCount());

      Cohort componentTypeCohort = new Cohort();
      componentTypeCohort.setCategory(CohortConstants.COMPONENT_TYPE_CATEGORY);
      componentTypeCohort.setComparator(Comparator.EQUALS);
      componentTypeCohort.setOption(dto.getComponentType().getComponentTypeName());
      dataValue.addCohort(componentTypeCohort);

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
