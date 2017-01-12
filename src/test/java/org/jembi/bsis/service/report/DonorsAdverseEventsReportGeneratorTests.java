package org.jembi.bsis.service.report;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.jembi.bsis.helpers.builders.AdverseEventTypeBuilder.anAdverseEventType;
import static org.jembi.bsis.helpers.builders.CohortBuilder.aCohort;
import static org.jembi.bsis.helpers.builders.DataValueBuilder.aDataValue;
import static org.jembi.bsis.helpers.builders.DonorsAdverseEventsDTOBuilder.aDonorsAdverseEventsDTO;
import static org.jembi.bsis.helpers.builders.LocationBuilder.aVenue;
import static org.jembi.bsis.helpers.builders.LocationViewModelBuilder.aLocationViewModel;
import static org.jembi.bsis.helpers.builders.ReportBuilder.aReport;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.jembi.bsis.constant.CohortConstants;
import org.jembi.bsis.dto.DonorsAdverseEventsDTO;
import org.jembi.bsis.factory.LocationFactory;
import org.jembi.bsis.model.adverseevent.AdverseEventType;
import org.jembi.bsis.model.location.Location;
import org.jembi.bsis.model.reporting.Cohort;
import org.jembi.bsis.model.reporting.Comparator;
import org.jembi.bsis.model.reporting.DataValue;
import org.jembi.bsis.model.reporting.Report;
import org.jembi.bsis.repository.AdverseEventRepository;
import org.jembi.bsis.suites.UnitTestSuite;
import org.jembi.bsis.viewmodel.LocationViewModel;
import org.joda.time.DateTime;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

public class DonorsAdverseEventsReportGeneratorTests extends UnitTestSuite {

  @InjectMocks
  private DonorsAdverseEventsReportGenerator donorsAdverseEventsReportGenerator;

  @Mock
  private AdverseEventRepository adverseEventRepository;
  
  @Mock
  private LocationFactory locationFactory;
  
  @Test
  public void testGenerateAdverseEventsReportForAllVenues_shouldGenerateReportCorrectly() {

    // Set up data
    Date startDate = new DateTime().minusDays(7).toDate();
    Date endDate = new DateTime().minusDays(0).toDate();

    Location venueB = aVenue().withName("b venue").build();
    LocationViewModel venueBViewModel = aLocationViewModel().withName(venueB.getName()).build();
    Location venueA = aVenue().withName("a venue").build();
    LocationViewModel venueAViewModel = aLocationViewModel().withName(venueA.getName()).build();

    AdverseEventType adverseEventTypeSomething = anAdverseEventType().withName("something").build();
    AdverseEventType adverseEventTypeSomethingReallyBad = anAdverseEventType().withName("something really bad").build();
    
    List<DonorsAdverseEventsDTO> adverseEventsDTOs = Arrays.asList(
        aDonorsAdverseEventsDTO().withAdverseEventType(adverseEventTypeSomething).withVenue(venueA).withCount(4).build(),
        aDonorsAdverseEventsDTO().withAdverseEventType(adverseEventTypeSomething).withVenue(venueB).withCount(1).build(),
        aDonorsAdverseEventsDTO().withAdverseEventType(adverseEventTypeSomethingReallyBad).withVenue(venueA).withCount(14).build(),
        aDonorsAdverseEventsDTO().withAdverseEventType(adverseEventTypeSomethingReallyBad).withVenue(venueB).withCount(3).build()
    );

    Cohort somethingCohort = aCohort()
        .withCategory(CohortConstants.ADVERSE_EVENT_TYPE_CATEGORY)
        .withComparator(Comparator.EQUALS)
        .withOption("something")
        .build();
    Cohort somethingReallyBadCohort = aCohort()
        .withCategory(CohortConstants.ADVERSE_EVENT_TYPE_CATEGORY)
        .withComparator(Comparator.EQUALS)
        .withOption("something really bad")
        .build();

    List<DataValue> expectedDataValues = Arrays.asList(
        aDataValue().withStartDate(startDate).withEndDate(endDate).withValue(4L).withLocation(venueAViewModel)
            .withCohort(somethingCohort)
            .build(),
        aDataValue().withStartDate(startDate).withEndDate(endDate).withValue(14L).withLocation(venueAViewModel)
          .withCohort(somethingReallyBadCohort)
          .build(),
        aDataValue().withStartDate(startDate).withEndDate(endDate).withValue(1L).withLocation(venueBViewModel)
            .withCohort(somethingCohort)
            .build(),
        aDataValue().withStartDate(startDate).withEndDate(endDate).withValue(3L).withLocation(venueBViewModel)
            .withCohort(somethingReallyBadCohort)
            .build()
    );

    Report expectedReport = aReport().withStartDate(startDate).withEndDate(endDate).withDataValues(expectedDataValues).build();

    // Set up mocks
    when(adverseEventRepository.countAdverseEvents(null, startDate, endDate)).thenReturn(adverseEventsDTOs);
    when(locationFactory.createViewModel(venueB)).thenReturn(venueBViewModel);
    when(locationFactory.createViewModel(venueA)).thenReturn(venueAViewModel);

    // Run test
    Report returnedReport = donorsAdverseEventsReportGenerator.generateDonorsAdverseEventsReport(null, startDate, endDate);

    // Verify
    assertThat(returnedReport, is(equalTo(expectedReport)));
  }

  @Test
  public void testGenerateAdverseEventsReportForOneVenues_shouldGenerateReportCorrectly() {

    // Set up data
    Date startDate = new DateTime().minusDays(7).toDate();
    Date endDate = new DateTime().minusDays(0).toDate();

    Location venueA = aVenue().withName("a venue").build();
    LocationViewModel venueAViewModel = aLocationViewModel().withName(venueA.getName()).build();

    AdverseEventType adverseEventTypeSomething = anAdverseEventType().withName("something").build();
    AdverseEventType adverseEventTypeSomethingReallyBad = anAdverseEventType().withName("something really bad").build();
    
    List<DonorsAdverseEventsDTO> adverseEventsDTOs = Arrays.asList(
        aDonorsAdverseEventsDTO().withAdverseEventType(adverseEventTypeSomething).withVenue(venueA).withCount(4).build(),
        aDonorsAdverseEventsDTO().withAdverseEventType(adverseEventTypeSomethingReallyBad).withVenue(venueA).withCount(14).build()
    );

    Cohort somethingCohort = aCohort()
        .withCategory(CohortConstants.ADVERSE_EVENT_TYPE_CATEGORY)
        .withComparator(Comparator.EQUALS)
        .withOption("something")
        .build();
    Cohort somethingReallyBadCohort = aCohort()
        .withCategory(CohortConstants.ADVERSE_EVENT_TYPE_CATEGORY)
        .withComparator(Comparator.EQUALS)
        .withOption("something really bad")
        .build();

    List<DataValue> expectedDataValues = Arrays.asList(
        aDataValue().withStartDate(startDate).withEndDate(endDate).withValue(4L).withLocation(venueAViewModel)
            .withCohort(somethingCohort)
            .build(),
        aDataValue().withStartDate(startDate).withEndDate(endDate).withValue(14L).withLocation(venueAViewModel)
            .withCohort(somethingReallyBadCohort)
            .build()
    );

    Report expectedReport = aReport().withStartDate(startDate).withEndDate(endDate).withDataValues(expectedDataValues).build();

    // Set up mocks
    when(adverseEventRepository.countAdverseEvents(venueA.getId(), startDate, endDate)).thenReturn(adverseEventsDTOs);
    when(locationFactory.createViewModel(venueA)).thenReturn(venueAViewModel);

    // Run test
    Report returnedReport = donorsAdverseEventsReportGenerator.generateDonorsAdverseEventsReport(venueA.getId(), startDate, endDate);

    // Verify
    assertThat(returnedReport, is(equalTo(expectedReport)));
  }
}
