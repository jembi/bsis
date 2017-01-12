package org.jembi.bsis.service.report;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.jembi.bsis.helpers.builders.CohortBuilder.aCohort;
import static org.jembi.bsis.helpers.builders.CollectedDonationDTOBuilder.aCollectedDonationDTO;
import static org.jembi.bsis.helpers.builders.DataValueBuilder.aDataValue;
import static org.jembi.bsis.helpers.builders.DonationTypeBuilder.aDonationType;
import static org.jembi.bsis.helpers.builders.ReportBuilder.aReport;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.jembi.bsis.constant.CohortConstants;
import org.jembi.bsis.dto.CollectedDonationDTO;
import org.jembi.bsis.factory.LocationFactory;
import org.jembi.bsis.model.reporting.Comparator;
import org.jembi.bsis.model.reporting.DataValue;
import org.jembi.bsis.model.reporting.Report;
import org.jembi.bsis.model.util.Gender;
import org.jembi.bsis.repository.DonationRepository;
import org.jembi.bsis.suites.UnitTestSuite;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

public class CollectedDonationsGeneratorTests extends UnitTestSuite {

  @InjectMocks
  private CollectedDonationsReportGenerator collectedDonationsReportGenerator;
  @Mock
  private DonationRepository donationRepository;
  @Mock
  private LocationFactory locationFactory;

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

    Report returnedReport = collectedDonationsReportGenerator.generateCollectedDonationsReport(irrelevantStartDate,
        irrelevantEndDate);

    assertThat(returnedReport, is(equalTo(expectedReport)));
  }

}