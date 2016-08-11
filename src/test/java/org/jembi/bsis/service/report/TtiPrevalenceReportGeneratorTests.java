package org.jembi.bsis.service.report;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.jembi.bsis.helpers.builders.BloodTestBuilder.aBloodTest;
import static org.jembi.bsis.helpers.builders.BloodTestResultDTOBuilder.aBloodTestResultDTO;
import static org.jembi.bsis.helpers.builders.BloodTestTotalDTOBuilder.aBloodTestTotalDTO;
import static org.jembi.bsis.helpers.builders.CohortBuilder.aCohort;
import static org.jembi.bsis.helpers.builders.DataValueBuilder.aDataValue;
import static org.jembi.bsis.helpers.builders.LocationBuilder.aVenue;
import static org.jembi.bsis.helpers.builders.ReportBuilder.aReport;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.jembi.bsis.constant.CohortConstants;
import org.jembi.bsis.dto.BloodTestResultDTO;
import org.jembi.bsis.dto.BloodTestTotalDTO;
import org.jembi.bsis.model.location.Location;
import org.jembi.bsis.model.reporting.Comparator;
import org.jembi.bsis.model.reporting.DataValue;
import org.jembi.bsis.model.reporting.Report;
import org.jembi.bsis.model.util.Gender;
import org.jembi.bsis.repository.bloodtesting.BloodTestingRepository;
import org.jembi.bsis.service.report.TtiPrevalenceReportGenerator;
import org.jembi.bsis.suites.UnitTestSuite;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

public class TtiPrevalenceReportGeneratorTests extends UnitTestSuite {

  @InjectMocks
  private TtiPrevalenceReportGenerator ttiPrevalenceReportGenerator;

  @Mock
  private BloodTestingRepository bloodTestingRepository;
  
  @Test
  public void testGenerateTTIPrevalenceReport() {

    Date irrelevantStartDate = new Date();
    Date irrelevantEndDate = new Date();
    Location venue = aVenue().build();

    List<BloodTestResultDTO> bloodResultDtos = Arrays.asList(aBloodTestResultDTO()
        .withBloodTest(aBloodTest().withTestName("HCV").build()).withResult("POS").withGender(Gender.female)
        .withCount(2).withVenue(venue).build()
    );
    
    List<BloodTestTotalDTO> totalUnitsDtos = Arrays.asList(aBloodTestTotalDTO()
        .withGender(Gender.female).withTotal(2).withVenue(venue).build()
    );
    
    List<BloodTestTotalDTO> totalUnsafeUnitsDtos = Arrays.asList(aBloodTestTotalDTO()
        .withGender(Gender.female).withTotal(2).withVenue(venue).build()
    );

    List<DataValue> expectedDataValues = Arrays.asList(
        aDataValue()
            .withStartDate(irrelevantStartDate)
            .withEndDate(irrelevantEndDate)
            .withValue(2L)
            .withVenue(venue)
            .withCohort(aCohort().withCategory(CohortConstants.BLOOD_TEST_CATEGORY).withComparator(Comparator.EQUALS).withOption("HCV").build())
            .withCohort(aCohort().withCategory(CohortConstants.BLOOD_TEST_RESULT_CATEGORY).withComparator(Comparator.EQUALS).withOption("POS").build())
            .withCohort(aCohort().withCategory(CohortConstants.GENDER_CATEGORY).withComparator(Comparator.EQUALS).withOption(Gender.female).build())
            .build(),
        aDataValue()
            .withStartDate(irrelevantStartDate)
            .withEndDate(irrelevantEndDate)
            .withValue(2L)
            .withVenue(venue)
            .withId("totalUnitsTested")
            .withCohort(aCohort().withCategory(CohortConstants.GENDER_CATEGORY).withComparator(Comparator.EQUALS).withOption(Gender.female).build())
            .build(),
        aDataValue()
            .withStartDate(irrelevantStartDate)
            .withEndDate(irrelevantEndDate)
            .withValue(2L)
            .withVenue(venue)
            .withId("totalUnsafeUnitsTested")
            .withCohort(aCohort().withCategory(CohortConstants.GENDER_CATEGORY).withComparator(Comparator.EQUALS).withOption(Gender.female).build())
            .build()
    );

    Report expectedReport = aReport()
        .withStartDate(irrelevantStartDate)
        .withEndDate(irrelevantEndDate)
        .withDataValues(expectedDataValues)
        .build();

    when(bloodTestingRepository.findTTIPrevalenceReportIndicators(irrelevantStartDate, irrelevantEndDate))
        .thenReturn(bloodResultDtos);
    when(bloodTestingRepository.findTTIPrevalenceReportTotalUnitsTested(irrelevantStartDate, irrelevantEndDate))
        .thenReturn(totalUnitsDtos);
    when(bloodTestingRepository.findTTIPrevalenceReportTotalUnsafeUnitsTested(irrelevantStartDate, irrelevantEndDate))
        .thenReturn(totalUnsafeUnitsDtos);

    Report returnedReport = ttiPrevalenceReportGenerator.generateTTIPrevalenceReport(irrelevantStartDate,
        irrelevantEndDate);

    assertThat(returnedReport, is(equalTo(expectedReport)));
  }



}
