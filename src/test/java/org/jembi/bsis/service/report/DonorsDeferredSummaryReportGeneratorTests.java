package org.jembi.bsis.service.report;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.jembi.bsis.helpers.builders.CohortBuilder.aCohort;
import static org.jembi.bsis.helpers.builders.DataValueBuilder.aDataValue;
import static org.jembi.bsis.helpers.builders.DeferralReasonBuilder.aDeferralReason;
import static org.jembi.bsis.helpers.builders.DeferredDonorsDTOBuilder.aDeferredDonorsDTO;
import static org.jembi.bsis.helpers.builders.LocationBuilder.aVenue;
import static org.jembi.bsis.helpers.builders.LocationViewModelBuilder.aLocationViewModel;
import static org.jembi.bsis.helpers.builders.ReportBuilder.aReport;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.jembi.bsis.constant.CohortConstants;
import org.jembi.bsis.dto.DeferredDonorsDTO;
import org.jembi.bsis.factory.LocationFactory;
import org.jembi.bsis.model.donordeferral.DeferralReason;
import org.jembi.bsis.model.location.Location;
import org.jembi.bsis.model.reporting.Comparator;
import org.jembi.bsis.model.reporting.DataValue;
import org.jembi.bsis.model.reporting.Report;
import org.jembi.bsis.model.util.Gender;
import org.jembi.bsis.repository.DonorDeferralRepository;
import org.jembi.bsis.suites.UnitTestSuite;
import org.jembi.bsis.viewmodel.LocationViewModel;
import org.joda.time.DateTime;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

public class DonorsDeferredSummaryReportGeneratorTests extends UnitTestSuite {

  @InjectMocks
  private DonorsDeferredSummaryReportGenerator donorsDeferredSummaryReportGenerator;

  @Mock
  private DonorDeferralRepository donorDeferralRepository;

  @Mock
  private LocationFactory locationFactory;

  @Test
  public void testGenerateUnitsIssuedReport() {

    // Set up data
    Date startDate = new DateTime().minusDays(7).toDate();
    Date endDate = new DateTime().minusDays(0).toDate();

    Location location1 = aVenue().build();
    LocationViewModel location1ViewModel = aLocationViewModel().build();
    Location location2 = aVenue().build();
    LocationViewModel location2ViewModel = aLocationViewModel().build();

    DeferralReason deferralReason1 = aDeferralReason().withReason("first reason").build();
    DeferralReason deferralReason2 = aDeferralReason().withReason("second reason").build();
    
    List<DeferredDonorsDTO> deferredDonors = Arrays.asList(
        aDeferredDonorsDTO().withDeferralReason(deferralReason1).withGender(Gender.female).withVenue(location1).withCount(4).build(),
        aDeferredDonorsDTO().withDeferralReason(deferralReason2).withGender(Gender.female).withVenue(location2).withCount(3).build(),
        aDeferredDonorsDTO().withDeferralReason(deferralReason2).withGender(Gender.male).withVenue(location1).withCount(1).build(),
        aDeferredDonorsDTO().withDeferralReason(deferralReason1).withGender(Gender.male).withVenue(location2).withCount(5).build()
    );


    List<DataValue> expectedDataValues = Arrays.asList(
        aDataValue().withStartDate(startDate).withEndDate(endDate).withValue(4L).withLocation(location1ViewModel)
            .withCohort(aCohort().withCategory(CohortConstants.GENDER_CATEGORY).withComparator(Comparator.EQUALS).withOption(Gender.female).build())
            .withCohort(aCohort().withCategory(CohortConstants.DEFERRAL_REASON_CATEGORY).withComparator(Comparator.EQUALS).withOption("first reason").build())
            .build(),
        aDataValue().withStartDate(startDate).withEndDate(endDate).withValue(3L).withLocation(location2ViewModel)
            .withCohort(aCohort().withCategory(CohortConstants.GENDER_CATEGORY).withComparator(Comparator.EQUALS).withOption(Gender.female).build())
            .withCohort(aCohort().withCategory(CohortConstants.DEFERRAL_REASON_CATEGORY).withComparator(Comparator.EQUALS).withOption("second reason").build())
            .build(),
        aDataValue().withStartDate(startDate).withEndDate(endDate).withValue(1L).withLocation(location1ViewModel)
            .withCohort(aCohort().withCategory(CohortConstants.GENDER_CATEGORY).withComparator(Comparator.EQUALS).withOption(Gender.male).build())
            .withCohort(aCohort().withCategory(CohortConstants.DEFERRAL_REASON_CATEGORY).withComparator(Comparator.EQUALS).withOption("second reason").build())
            .build(),
        aDataValue().withStartDate(startDate).withEndDate(endDate).withValue(5L).withLocation(location2ViewModel)
            .withCohort(aCohort().withCategory(CohortConstants.GENDER_CATEGORY).withComparator(Comparator.EQUALS).withOption(Gender.male).build())
            .withCohort(aCohort().withCategory(CohortConstants.DEFERRAL_REASON_CATEGORY).withComparator(Comparator.EQUALS).withOption("first reason").build())
            .build()
    );

    Report expectedReport = aReport().withStartDate(startDate).withEndDate(endDate).withDataValues(expectedDataValues).build();

    // Set up mocks
    when(donorDeferralRepository.countDeferredDonors(startDate, endDate)).thenReturn(deferredDonors);
    when(locationFactory.createViewModel(location1)).thenReturn(location1ViewModel);
    when(locationFactory.createViewModel(location2)).thenReturn(location2ViewModel);

    // Run test
    Report returnedReport = donorsDeferredSummaryReportGenerator.generateDonorDeferralSummaryReport(startDate, endDate);

    // Verify
    assertThat(returnedReport, is(equalTo(expectedReport)));
  }

}
