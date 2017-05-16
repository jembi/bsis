package org.jembi.bsis.service.report;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.jembi.bsis.helpers.builders.CohortBuilder.aCohort;
import static org.jembi.bsis.helpers.builders.ComponentProductionDTOBuilder.aComponentProductionDTO;
import static org.jembi.bsis.helpers.builders.DataValueBuilder.aDataValue;
import static org.jembi.bsis.helpers.builders.LocationBuilder.aProcessingSite;
import static org.jembi.bsis.helpers.builders.LocationViewModelBuilder.aLocationViewModel;
import static org.jembi.bsis.helpers.builders.ReportBuilder.aReport;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.jembi.bsis.constant.CohortConstants;
import org.jembi.bsis.dto.ComponentProductionDTO;
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

public class ComponentProductionReportGeneratorTests extends UnitTestSuite {

  @InjectMocks
  private ComponentProductionReportGenerator componentProductionReportGenerator;
  @Mock
  private ComponentRepository componentRepository;
  @Mock
  private LocationFactory locationFactory;

  @Test
  public void testGenerateComponentProductionReport() {

    Date irrelevantStartDate = new Date();
    Date irrelevantEndDate = new Date();
    UUID locationId = UUID.randomUUID();
    Location processingSite = aProcessingSite().withId(locationId).build();
    LocationViewModel processingSiteViewModel = aLocationViewModel().withId(locationId).build();

    List<ComponentProductionDTO> dtos = Arrays.asList(
            aComponentProductionDTO()
                    .withComponentTypeName("Apheresis")
                    .withBloodAbo("A")
                    .withBloodRh("+")
                    .withProcessingSite(processingSite)
                    .withCount(2)
                    .build(),
            aComponentProductionDTO()
            .withComponentTypeName("Whole Blood - CPDA")
            .withBloodAbo("B")
            .withBloodRh("+")
            .withProcessingSite(processingSite)
            .withCount(2)
            .build()
    );

    List<DataValue> expectedDataValues = Arrays.asList(
        aDataValue()
            .withStartDate(irrelevantStartDate)
            .withEndDate(irrelevantEndDate)
            .withLocation(processingSiteViewModel)
            .withValue(2L)
            .withCohort(aCohort()
                .withCategory(CohortConstants.COMPONENT_TYPE_CATEGORY)
                .withComparator(Comparator.EQUALS)
                .withOption("Apheresis")
                .build())
            .withCohort(aCohort()
                .withCategory(CohortConstants.BLOOD_TYPE_CATEGORY)
                .withComparator(Comparator.EQUALS)
                .withOption("A+")
                .build())
            .build(),
        aDataValue()
            .withStartDate(irrelevantEndDate)
            .withEndDate(irrelevantEndDate)
            .withLocation(processingSiteViewModel)
            .withValue(2L)
            .withCohort(aCohort()
                .withCategory(CohortConstants.COMPONENT_TYPE_CATEGORY)
                .withComparator(Comparator.EQUALS)
                .withOption("Whole Blood - CPDA")
                .build())
            .withCohort(aCohort()
                .withCategory(CohortConstants.BLOOD_TYPE_CATEGORY)
                .withComparator(Comparator.EQUALS)
                .withOption("B+")
                .build())
            .build()
    );

    Report expectedReport = aReport()
        .withStartDate(irrelevantStartDate)
        .withEndDate(irrelevantEndDate)
        .withDataValues(expectedDataValues)
        .build();

    when(componentRepository.findProducedComponentsByProcessingSite(processingSite.getId(), irrelevantStartDate, irrelevantEndDate))
        .thenReturn(dtos);
    when(locationFactory.createViewModel(processingSite)).thenReturn(processingSiteViewModel);

    Report returnedReport = componentProductionReportGenerator.generateComponentProductionReport(processingSite.getId(), irrelevantStartDate, irrelevantEndDate);

    assertThat(returnedReport, is(equalTo(expectedReport)));
  }

}