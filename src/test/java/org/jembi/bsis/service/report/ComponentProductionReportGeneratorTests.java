package org.jembi.bsis.service.report;

import org.jembi.bsis.constant.CohortConstants;
import org.jembi.bsis.dto.ComponentProductionDTO;
import org.jembi.bsis.model.location.Location;
import org.jembi.bsis.model.reporting.Comparator;
import org.jembi.bsis.model.reporting.DataValue;
import org.jembi.bsis.model.reporting.Report;
import org.jembi.bsis.model.util.Gender;
import org.jembi.bsis.repository.ComponentRepository;
import org.jembi.bsis.suites.UnitTestSuite;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.jembi.bsis.helpers.builders.CohortBuilder.aCohort;
import static org.jembi.bsis.helpers.builders.ComponentProductionDTOBuilder.aComponentProductionDTO;
import static org.jembi.bsis.helpers.builders.DataValueBuilder.aDataValue;
import static org.jembi.bsis.helpers.builders.LocationBuilder.aProcessingSite;
import static org.jembi.bsis.helpers.builders.ReportBuilder.aReport;
import static org.mockito.Mockito.when;

public class ComponentProductionReportGeneratorTests extends UnitTestSuite {

  @InjectMocks
  private ComponentProductionReportGenerator componentProductionReportGenerator;
  @Mock
  private ComponentRepository componentRepository;

  @Test
  public void testGenerateComponentProductionReport() {

    Date irrelevantStartDate = new Date();
    Date irrelevantEndDate = new Date();
    Location processingSite = aProcessingSite().build();

    List<ComponentProductionDTO> dtos = Arrays.asList(
            aComponentProductionDTO()
                    .withComponentTypeName("Apheresis")
                    .withBloodAbo("A")
                    .withBloodRh("+")
                    .withVenue(processingSite)
                    .withCount(2)
                    .build()
    );

    List<DataValue> expectedDataValues = Arrays.asList(
        aDataValue()
            .withStartDate(irrelevantStartDate)
            .withEndDate(irrelevantEndDate)
            .withVenue(processingSite)
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
            .build()
    );

    Report expectedReport = aReport()
        .withStartDate(irrelevantStartDate)
        .withEndDate(irrelevantEndDate)
        .withDataValues(expectedDataValues)
        .build();

    when(componentRepository.findProducedComponentsByProcessingSite(processingSite.getId(), irrelevantStartDate, irrelevantEndDate))
        .thenReturn(dtos);

    Report returnedReport = componentProductionReportGenerator.generateComponentProductionReport(processingSite.getId(), irrelevantStartDate, irrelevantEndDate);

    assertThat(returnedReport, is(equalTo(expectedReport)));
  }

}