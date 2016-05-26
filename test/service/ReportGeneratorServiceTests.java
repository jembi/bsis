package service;

import static helpers.builders.CohortBuilder.aCohort;
import static helpers.builders.CollectedDonationDTOBuilder.aCollectedDonationDTO;
import static helpers.builders.ComponentTypeBuilder.aComponentType;
import static helpers.builders.DataValueBuilder.aDataValue;
import static helpers.builders.DonationTypeBuilder.aDonationType;
import static helpers.builders.BloodTestBuilder.aBloodTest;
import static helpers.builders.ReportBuilder.aReport;
import static helpers.builders.StockLevelDTOBuilder.aStockLevelDTO;
import static helpers.builders.BloodTestResultDTOBuilder.aBloodTestResultDTO;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import constant.CohortConstants;
import dto.BloodTestResultDTO;
import dto.CollectedDonationDTO;
import dto.StockLevelDTO;
import helpers.builders.LocationBuilder;
import model.inventory.InventoryStatus;
import model.location.Location;
import model.reporting.Comparator;
import model.reporting.DataValue;
import model.reporting.Report;
import model.util.Gender;
import repository.DonationRepository;
import repository.InventoryRepository;
import repository.bloodtesting.BloodTestingRepository;
import suites.UnitTestSuite;

public class ReportGeneratorServiceTests extends UnitTestSuite {

  @InjectMocks
  private ReportGeneratorService reportGeneratorService;
  @Mock
  private DonationRepository donationRepository;
  @Mock
  private InventoryRepository inventoryRepository;
  @Mock
  private BloodTestingRepository bloodTestingRepository;

  @Test
  public void testGenerateCollectedDonationsReport() {

    Date irrelevantStartDate = new Date();
    Date irrelevantEndDate = new Date();

    List<CollectedDonationDTO> dtos = Arrays.asList(
        aCollectedDonationDTO()
            .withDonationType(aDonationType().withName("Family").build())
            .withGender(Gender.female)
            .withBloodAbo("A")
            .withBloodRh("+")
            .withCount(2)
            .build()
    );

    List<DataValue> expectedDataValues = Arrays.asList(
        aDataValue()
            .withStartDate(irrelevantStartDate)
            .withEndDate(irrelevantEndDate)
            .withValue(2L)
            .withCohort(aCohort()
                .withCategory(CohortConstants.DONATION_TYPE_CATEGORY)
                .withComparator(Comparator.EQUALS)
                .withOption("Family")
                .build())
            .withCohort(aCohort()
                .withCategory(CohortConstants.GENDER_CATEGORY)
                .withComparator(Comparator.EQUALS)
                .withOption(Gender.female)
                .build())
            .withCohort(aCohort()
                .withCategory(CohortConstants.BLOOD_TYPE_CATEGORY)
                .withComparator(Comparator.EQUALS)
                .withOption("A+")
                .build())
            .build()
    );

    Report expectedReport = aReport()
        .withStartDate(irrelevantStartDate)
        .withEndDate(irrelevantEndDate)
        .withDataValues(expectedDataValues)
        .build();

    when(donationRepository.findCollectedDonationsReportIndicators(irrelevantStartDate, irrelevantEndDate))
        .thenReturn(dtos);

    Report returnedReport = reportGeneratorService.generateCollectedDonationsReport(irrelevantStartDate,
        irrelevantEndDate);

    assertThat(returnedReport, is(equalTo(expectedReport)));
  }
  
  @Test
  public void testStockLevelsForLocationReport() {

    Location location = LocationBuilder.aLocation().withId(1L).build();
    List<StockLevelDTO> dtos = Arrays.asList(aStockLevelDTO().withComponentType(aComponentType().withComponentTypeName("Type1").build())
            .withBloodAbo("A").withBloodRh("+").withCount(2).build()
    );

    List<DataValue> expectedDataValues = Arrays.asList(
        aDataValue().withValue(2L)
            .withCohort(aCohort().withCategory(CohortConstants.COMPONENT_TYPE_CATEGORY)
                .withComparator(Comparator.EQUALS).withOption("Type1").build())
            .withCohort(aCohort().withCategory(CohortConstants.BLOOD_TYPE_CATEGORY).withComparator(Comparator.EQUALS)
                .withOption("A+").build())
            .build()
    );

    Report expectedReport = aReport().withDataValues(expectedDataValues).build();
    when(inventoryRepository.findStockLevelsForLocation(location.getId(), InventoryStatus.IN_STOCK)).thenReturn(dtos);
    Report returnedReport = reportGeneratorService.generateStockLevelsForLocationReport(location.getId(), InventoryStatus.IN_STOCK);

    assertThat(returnedReport, is(equalTo(expectedReport)));
  }
  
  @Test
  public void testGenerateTTIPrevalenceReport() {

    Date irrelevantStartDate = new Date();
    Date irrelevantEndDate = new Date();

    List<BloodTestResultDTO> dtos = Arrays.asList(
        aBloodTestResultDTO()
            .withBloodTest(aBloodTest().withTestName("HCV").build())
            .withResult("POS")
            .withGender(Gender.female)
            .withCount(2)
            .build()
    );

    List<DataValue> expectedDataValues = Arrays.asList(
        aDataValue()
            .withStartDate(irrelevantStartDate)
            .withEndDate(irrelevantEndDate)
            .withValue(2L)
            .withCohort(aCohort()
                .withCategory(CohortConstants.BLOOD_TEST_CATEGORY)
                .withComparator(Comparator.EQUALS)
                .withOption("HCV")
                .build())
            .withCohort(aCohort()
                .withCategory(CohortConstants.GENDER_CATEGORY)
                .withComparator(Comparator.EQUALS)
                .withOption(Gender.female)
                .build())
            .build()
    );

    Report expectedReport = aReport()
        .withStartDate(irrelevantStartDate)
        .withEndDate(irrelevantEndDate)
        .withDataValues(expectedDataValues)
        .build();

    when(bloodTestingRepository.findTTIPrevalenceReportIndicators(irrelevantStartDate, irrelevantEndDate))
        .thenReturn(dtos);

    Report returnedReport = reportGeneratorService.generateTTIPrevalenceReport(irrelevantStartDate,
        irrelevantEndDate);

    assertThat(returnedReport, is(equalTo(expectedReport)));
  }

}
