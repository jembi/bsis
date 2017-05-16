package org.jembi.bsis.service.report;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.jembi.bsis.helpers.builders.CohortBuilder.aCohort;
import static org.jembi.bsis.helpers.builders.DataValueBuilder.aDataValue;
import static org.jembi.bsis.helpers.builders.DiscardedComponentDTOBuilder.aDiscardedComponentDTO;
import static org.jembi.bsis.helpers.builders.LocationBuilder.aProcessingSite;
import static org.jembi.bsis.helpers.builders.LocationViewModelBuilder.aLocationViewModel;
import static org.jembi.bsis.helpers.builders.ReportBuilder.aReport;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.jembi.bsis.constant.CohortConstants;
import org.jembi.bsis.dto.DiscardedComponentDTO;
import org.jembi.bsis.factory.LocationFactory;
import org.jembi.bsis.model.location.Location;
import org.jembi.bsis.model.reporting.Comparator;
import org.jembi.bsis.model.reporting.DataValue;
import org.jembi.bsis.model.reporting.Report;
import org.jembi.bsis.repository.ComponentRepository;
import org.jembi.bsis.suites.UnitTestSuite;
import org.jembi.bsis.viewmodel.LocationViewModel;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

public class DiscardedComponentReportGeneratorTests extends UnitTestSuite {

  @InjectMocks
  private DiscardedComponentReportGenerator discardedComponentReportGenerator;

  @Mock
  private ComponentRepository componentRepository;

  @Mock
  private LocationFactory locationFactory;

  @Test
  public void testgenerateDiscardedComponents() {
    Date startDate = new Date();
    Date endDate = new Date();
    UUID locationId = UUID.randomUUID();
    Location processingSite = aProcessingSite().withId(locationId).build();
    LocationViewModel processingSiteViewModel = aLocationViewModel().withId(locationId).build();

    List<DiscardedComponentDTO> discardedComponents = Arrays.asList(
        aDiscardedComponentDTO()
          .withComponentStatusChangeReason("Storage Problems")
          .withComponentType("Platelets Concentrate - Apheresis")
          .withCount(2)
          .withVenue(processingSite)
          .build());

    List<DataValue> expectedDataValues = Arrays.asList(
        aDataValue()
          .withStartDate(startDate)
          .withEndDate(endDate)
          .withLocation(processingSiteViewModel)
          .withValue(2L)
          .withCohort(aCohort()
              .withCategory(CohortConstants.COMPONENT_TYPE_CATEGORY)
              .withComparator(Comparator.EQUALS)
              .withOption("Platelets Concentrate - Apheresis")
              .build())
          .withCohort(aCohort()
              .withCategory(CohortConstants.DISCARD_REASON_CATEGORY)
              .withComparator(Comparator.EQUALS)
              .withOption("Storage Problems")
              .build())
          .build());

    Report expectedReport = aReport()
          .withStartDate(startDate)
          .withEndDate(endDate)
          .withDataValues(expectedDataValues)
          .build();

    when(componentRepository.findSummaryOfDiscardedComponentsByProcessingSite(processingSite.getId(), startDate, endDate))
        .thenReturn(discardedComponents);
    when(locationFactory.createViewModel(processingSite)).thenReturn(processingSiteViewModel);

    Report returnedReport =
        discardedComponentReportGenerator.generateDiscardedComponents(processingSite.getId(), startDate, endDate);

    assertThat(returnedReport, is(equalTo(expectedReport)));
  }
  
  @Test
  public void testgenerateDiscardedComponentsWithListOfDTOs() {
    Date startDate = new Date();
    Date endDate = new Date();
    UUID locationId = UUID.randomUUID();
    Location processingSite = aProcessingSite().withId(locationId).build();
    LocationViewModel processingSiteViewModel = aLocationViewModel().withId(locationId).withName("aProcessingSite").build();

    List<DiscardedComponentDTO> discardedComponents = Arrays.asList(
        aDiscardedComponentDTO()
          .withComponentStatusChangeReason("Storage Problems")
          .withComponentType("Whole Blood Double Pack - CPDA")
          .withCount(2)
          .withVenue(processingSite)
          .build(),
        aDiscardedComponentDTO()
          .withComponentStatusChangeReason("Passed Expiry Dates")
          .withComponentType("Whole Blood Triple Pack - CPDA")
          .withCount(3)
          .withVenue(processingSite)
          .build(),
        aDiscardedComponentDTO()
          .withComponentStatusChangeReason("Incomplete Donation")
          .withComponentType("Whole Blood Poor Platelets - CPDA")
          .withCount(4)
          .withVenue(processingSite)
          .build(),
        aDiscardedComponentDTO()
          .withComponentStatusChangeReason("Processing Problems")
          .withComponentType("Packed Red Cells - CPDA")
          .withCount(5)
          .withVenue(processingSite)
          .build());

    List<DataValue> expectedDataValues = Arrays.asList(
        aDataValue()
          .withStartDate(startDate)
          .withEndDate(endDate)
          .withLocation(processingSiteViewModel)
          .withValue(2L)
            .withCohort(aCohort()
                .withCategory(CohortConstants.COMPONENT_TYPE_CATEGORY)
                .withComparator(Comparator.EQUALS)
                .withOption("Whole Blood Double Pack - CPDA").build())
            .withCohort(aCohort()
                .withCategory(CohortConstants.DISCARD_REASON_CATEGORY)
                .withComparator(Comparator.EQUALS)
                .withOption("Storage Problems")
                .build())
          .build(),
        aDataValue()
          .withStartDate(startDate)
          .withEndDate(endDate)
          .withLocation(processingSiteViewModel)
          .withValue(3L)
          .withCohort(aCohort()
              .withCategory(CohortConstants.COMPONENT_TYPE_CATEGORY)
              .withComparator(Comparator.EQUALS)
              .withOption("Whole Blood Triple Pack - CPDA")
              .build())
          .withCohort(aCohort()
              .withCategory(CohortConstants.DISCARD_REASON_CATEGORY)
              .withComparator(Comparator.EQUALS)
              .withOption("Passed Expiry Dates")
              .build())
          .build(),
        aDataValue()
          .withStartDate(startDate)
          .withEndDate(endDate)
          .withLocation(processingSiteViewModel)
          .withValue(4L)
            .withCohort(aCohort().withCategory(CohortConstants.COMPONENT_TYPE_CATEGORY)
                .withComparator(Comparator.EQUALS).withOption("Whole Blood Poor Platelets - CPDA").build())
            .withCohort(aCohort().withCategory(CohortConstants.DISCARD_REASON_CATEGORY)
                .withComparator(Comparator.EQUALS).withOption("Incomplete Donation").build())
          .build(),
        aDataValue()
          .withStartDate(startDate)
          .withEndDate(endDate)
          .withLocation(processingSiteViewModel)
          .withValue(5L)
            .withCohort(aCohort().withCategory(CohortConstants.COMPONENT_TYPE_CATEGORY)
                .withComparator(Comparator.EQUALS).withOption("Packed Red Cells - CPDA").build())
            .withCohort(aCohort().withCategory(CohortConstants.DISCARD_REASON_CATEGORY)
                .withComparator(Comparator.EQUALS).withOption("Processing Problems").build())
          .build());
    
    Report expectedReport = aReport()
        .withStartDate(startDate)
        .withEndDate(endDate)
        .withDataValues(expectedDataValues)
        .build();

    when(componentRepository.findSummaryOfDiscardedComponentsByProcessingSite(processingSite.getId(), startDate, endDate))
        .thenReturn(discardedComponents);
    when(locationFactory.createViewModel(processingSite)).thenReturn(processingSiteViewModel);

    Report returnedReport =
        discardedComponentReportGenerator.generateDiscardedComponents(processingSite.getId(), startDate, endDate);

    assertThat(returnedReport, is(equalTo(expectedReport)));
  }
}