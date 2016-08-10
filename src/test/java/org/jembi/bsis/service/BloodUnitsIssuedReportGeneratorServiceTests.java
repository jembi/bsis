package org.jembi.bsis.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.jembi.bsis.helpers.builders.BloodUnitsOrderDTOBuilder.aBloodUnitsOrderDTO;
import static org.jembi.bsis.helpers.builders.CohortBuilder.aCohort;
import static org.jembi.bsis.helpers.builders.DataValueBuilder.aDataValue;
import static org.jembi.bsis.helpers.builders.ReportBuilder.aReport;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.jembi.bsis.constant.CohortConstants;
import org.jembi.bsis.dto.BloodUnitsOrderDTO;
import org.jembi.bsis.helpers.builders.ComponentTypeBuilder;
import org.jembi.bsis.model.componenttype.ComponentType;
import org.jembi.bsis.model.reporting.Comparator;
import org.jembi.bsis.model.reporting.DataValue;
import org.jembi.bsis.model.reporting.Report;
import org.jembi.bsis.repository.OrderFormRepository;
import org.jembi.bsis.suites.UnitTestSuite;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

public class BloodUnitsIssuedReportGeneratorServiceTests extends UnitTestSuite {

  @InjectMocks
  private BloodUnitsIssuedReportGeneratorService bloodUnitsIssuedReportGeneratorService;

  @Mock
  private OrderFormRepository orderFormRepository;
  
  @Test
  public void testGenerateUnitsIssuedReport() {

    // Set up data
    Date startDate = new Date();
    Date endDate = new Date();
    ComponentType componentType1 = ComponentTypeBuilder.aComponentType().withComponentTypeName("comp1").build();
    ComponentType componentType2 = ComponentTypeBuilder.aComponentType().withComponentTypeName("comp2").build();

    List<BloodUnitsOrderDTO> unitsOrdered = Arrays.asList(
        aBloodUnitsOrderDTO().withComponentType(componentType1).withCount(2).build(),
        aBloodUnitsOrderDTO().withComponentType(componentType2).withCount(3).build()
    );
    
    List<BloodUnitsOrderDTO> unitsIssued = Arrays.asList(
        aBloodUnitsOrderDTO().withComponentType(componentType1).withCount(1).build(),
        aBloodUnitsOrderDTO().withComponentType(componentType2).withCount(2).build()
    );

    List<DataValue> expectedDataValues = Arrays.asList(
        aDataValue().withStartDate(startDate).withEndDate(endDate).withValue(2L).withId("unitsOrdered")
            .withCohort(aCohort().withCategory(CohortConstants.COMPONENT_TYPE_CATEGORY).withComparator(Comparator.EQUALS).withOption("comp1").build())
            .build(),
        aDataValue().withStartDate(startDate).withEndDate(endDate).withValue(3L).withId("unitsOrdered")
            .withCohort(aCohort().withCategory(CohortConstants.COMPONENT_TYPE_CATEGORY).withComparator(Comparator.EQUALS).withOption("comp2").build())
            .build(),
        aDataValue().withStartDate(startDate).withEndDate(endDate).withValue(1L).withId("unitsIssued")
            .withCohort(aCohort().withCategory(CohortConstants.COMPONENT_TYPE_CATEGORY).withComparator(Comparator.EQUALS).withOption("comp1").build())
            .build(),
        aDataValue().withStartDate(startDate).withEndDate(endDate).withValue(2L).withId("unitsIssued")
            .withCohort(aCohort().withCategory(CohortConstants.COMPONENT_TYPE_CATEGORY).withComparator(Comparator.EQUALS).withOption("comp2").build())
            .build()
    );

    Report expectedReport = aReport().withStartDate(startDate).withEndDate(endDate).withDataValues(expectedDataValues).build();

    // Set up mocks
    when(orderFormRepository.findBloodUnitsOrdered(startDate, endDate)).thenReturn(unitsOrdered);
    when(orderFormRepository.findBloodUnitsIssued(startDate, endDate)).thenReturn(unitsIssued);

    // Run test
    Report returnedReport = bloodUnitsIssuedReportGeneratorService.generateUnitsIssuedReport(startDate, endDate);

    // Verify
    assertThat(returnedReport, is(equalTo(expectedReport)));
  }

}
