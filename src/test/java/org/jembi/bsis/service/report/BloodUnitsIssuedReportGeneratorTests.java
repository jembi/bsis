package org.jembi.bsis.service.report;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.jembi.bsis.helpers.builders.BloodUnitsOrderDTOBuilder.aBloodUnitsOrderDTO;
import static org.jembi.bsis.helpers.builders.CohortBuilder.aCohort;
import static org.jembi.bsis.helpers.builders.DataValueBuilder.aDataValue;
import static org.jembi.bsis.helpers.builders.LocationBuilder.aDistributionSite;
import static org.jembi.bsis.helpers.builders.LocationViewModelBuilder.aLocationViewModel;
import static org.jembi.bsis.helpers.builders.ReportBuilder.aReport;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.jembi.bsis.constant.CohortConstants;
import org.jembi.bsis.dto.BloodUnitsOrderDTO;
import org.jembi.bsis.factory.LocationFactory;
import org.jembi.bsis.helpers.builders.ComponentTypeBuilder;
import org.jembi.bsis.model.componenttype.ComponentType;
import org.jembi.bsis.model.location.Location;
import org.jembi.bsis.model.order.OrderType;
import org.jembi.bsis.model.reporting.Comparator;
import org.jembi.bsis.model.reporting.DataValue;
import org.jembi.bsis.model.reporting.Report;
import org.jembi.bsis.repository.OrderFormRepository;
import org.jembi.bsis.suites.UnitTestSuite;
import org.jembi.bsis.viewmodel.LocationViewModel;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

public class BloodUnitsIssuedReportGeneratorTests extends UnitTestSuite {

  @InjectMocks
  private BloodUnitsIssuedReportGenerator bloodUnitsIssuedReportGenerator;

  @Mock
  private OrderFormRepository orderFormRepository;

  @Mock
  private LocationFactory locationFactory;
  
  @Test
  public void testGenerateUnitsIssuedReport() {

    // Set up data
    UUID locationId = UUID.randomUUID();
    Location distributionSite = aDistributionSite().withId(locationId).withName("Central").build();
    Date startDate = new Date();
    Date endDate = new Date();
    ComponentType componentType1 = ComponentTypeBuilder.aComponentType().withComponentTypeName("comp1").build();
    ComponentType componentType2 = ComponentTypeBuilder.aComponentType().withComponentTypeName("comp2").build();

    List<BloodUnitsOrderDTO> unitsOrdered = Arrays.asList(
        aBloodUnitsOrderDTO()
          .withComponentType(componentType1)
          .withDistributionSite(distributionSite)
          .withOrderType(OrderType.ISSUE)
          .withCount(2)
          .build(),
        aBloodUnitsOrderDTO()
          .withComponentType(componentType2)
          .withDistributionSite(distributionSite)
          .withOrderType(OrderType.ISSUE)
          .withCount(3)
          .build(),
        aBloodUnitsOrderDTO()
          .withComponentType(componentType2)
          .withDistributionSite(distributionSite)
          .withOrderType(OrderType.PATIENT_REQUEST)
          .withCount(3)
        .build()
    );
    
    List<BloodUnitsOrderDTO> unitsIssued = Arrays.asList(
        aBloodUnitsOrderDTO().withComponentType(componentType1).withDistributionSite(distributionSite).withOrderType(OrderType.ISSUE).withCount(1).build(),
        aBloodUnitsOrderDTO().withComponentType(componentType2).withDistributionSite(distributionSite).withOrderType(OrderType.PATIENT_REQUEST).withCount(2).build()
    );

    LocationViewModel expectedLocation = aLocationViewModel().withId(locationId).withName("Central").build();

    List<DataValue> expectedDataValues = Arrays.asList(
        aDataValue()
          .withStartDate(startDate)
          .withEndDate(endDate)
          .withValue(2L)
          .withId("unitsOrdered")
          .withLocation(expectedLocation)
          .withCohort(aCohort()
              .withCategory(CohortConstants.COMPONENT_TYPE_CATEGORY)
              .withComparator(Comparator.EQUALS)
              .withOption("comp1")
              .build())
          .withCohort(aCohort()
              .withCategory(CohortConstants.ORDER_TYPE_CATEGORY)
              .withComparator(Comparator.EQUALS)
              .withOption("ISSUE")
              .build())
          .build(),
        aDataValue()
          .withStartDate(startDate)
          .withEndDate(endDate)
          .withValue(3L)
          .withLocation(expectedLocation)
          .withId("unitsOrdered")
          .withCohort(aCohort()
              .withCategory(CohortConstants.COMPONENT_TYPE_CATEGORY)
              .withComparator(Comparator.EQUALS)
              .withOption("comp2")
              .build())
          .withCohort(aCohort()
              .withCategory(CohortConstants.ORDER_TYPE_CATEGORY)
              .withComparator(Comparator.EQUALS)
              .withOption("ISSUE")
              .build())
          .build(),  
        aDataValue()
          .withStartDate(startDate)
          .withEndDate(endDate)
          .withValue(3L)
          .withId("unitsOrdered")
          .withLocation(expectedLocation)
          .withCohort(aCohort()
              .withCategory(CohortConstants.COMPONENT_TYPE_CATEGORY)
              .withComparator(Comparator.EQUALS)
              .withOption("comp2")
              .build())
          .withCohort(aCohort()
              .withCategory(CohortConstants.ORDER_TYPE_CATEGORY)
              .withComparator(Comparator.EQUALS)
              .withOption("PATIENT_REQUEST")
              .build())
          .build(),
        aDataValue()
          .withStartDate(startDate)
          .withEndDate(endDate)
          .withValue(1L)
          .withId("unitsIssued")
          .withLocation(expectedLocation)
          .withCohort(aCohort()
              .withCategory(CohortConstants.COMPONENT_TYPE_CATEGORY)
              .withComparator(Comparator.EQUALS)
              .withOption("comp1")
              .build())
          .withCohort(aCohort()
              .withCategory(CohortConstants.ORDER_TYPE_CATEGORY)
              .withComparator(Comparator.EQUALS)
              .withOption("ISSUE")
              .build())
          .build(),
        aDataValue()
          .withStartDate(startDate)
          .withEndDate(endDate)
          .withValue(2L)
          .withId("unitsIssued")
          .withLocation(expectedLocation)
          .withCohort(aCohort()
              .withCategory(CohortConstants.COMPONENT_TYPE_CATEGORY)
              .withComparator(Comparator.EQUALS)
              .withOption("comp2")
              .build())
          .withCohort(aCohort()
              .withCategory(CohortConstants.ORDER_TYPE_CATEGORY)
              .withComparator(Comparator.EQUALS)
              .withOption("PATIENT_REQUEST")
              .build())
          .build()
    );

    Report expectedReport = aReport().withStartDate(startDate).withEndDate(endDate).withDataValues(expectedDataValues).build();

    // Set up mocks
    when(orderFormRepository.findBloodUnitsOrdered(startDate, endDate)).thenReturn(unitsOrdered);
    when(orderFormRepository.findBloodUnitsIssued(startDate, endDate)).thenReturn(unitsIssued);
    when(locationFactory.createViewModel(distributionSite)).thenReturn(expectedLocation);

    // Run test
    Report returnedReport = bloodUnitsIssuedReportGenerator.generateUnitsIssuedReport(startDate, endDate);

    // Verify
    assertThat(returnedReport, is(equalTo(expectedReport)));
  }

}
