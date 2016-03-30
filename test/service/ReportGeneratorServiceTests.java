package service;

import static helpers.builders.CohortBuilder.aCohort;
import static helpers.builders.CollectedDonationDTOBuilder.aCollectedDonationDTO;
import static helpers.builders.DonationTypeBuilder.aDonationType;
import static helpers.builders.IndicatorBuilder.anIndicator;
import static helpers.builders.ReportBuilder.aReport;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import model.reporting.Comparator;
import model.reporting.Indicator;
import model.reporting.Report;
import model.util.Gender;

import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import constant.CohortConstants;
import dto.CollectedDonationDTO;
import repository.DonationRepository;
import suites.UnitTestSuite;

public class ReportGeneratorServiceTests extends UnitTestSuite {

  @InjectMocks
  private ReportGeneratorService reportGeneratorService;
  @Mock
  private DonationRepository donationRepository;

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

    List<Indicator> expectedIndicators = Arrays.asList(
        anIndicator()
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
        .withIndicators(expectedIndicators)
        .build();

    when(donationRepository.findCollectedDonationsReportIndicators(irrelevantStartDate, irrelevantEndDate))
        .thenReturn(dtos);

    Report returnedReport = reportGeneratorService.generateCollectedDonationsReport(irrelevantStartDate,
        irrelevantEndDate);

    assertThat(returnedReport, is(equalTo(expectedReport)));
  }

}
