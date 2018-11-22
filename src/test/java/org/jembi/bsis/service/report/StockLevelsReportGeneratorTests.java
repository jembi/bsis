package org.jembi.bsis.service.report;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.jembi.bsis.helpers.builders.CohortBuilder.aCohort;
import static org.jembi.bsis.helpers.builders.ComponentTypeBuilder.aComponentType;
import static org.jembi.bsis.helpers.builders.DataValueBuilder.aDataValue;
import static org.jembi.bsis.helpers.builders.LocationViewModelBuilder.aLocationViewModel;
import static org.jembi.bsis.helpers.builders.ReportBuilder.aReport;
import static org.jembi.bsis.helpers.builders.StockLevelDTOBuilder.aStockLevelDTO;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.jembi.bsis.constant.CohortConstants;
import org.jembi.bsis.dto.StockLevelDTO;
import org.jembi.bsis.helpers.builders.LocationBuilder;
import org.jembi.bsis.model.inventory.InventoryStatus;
import org.jembi.bsis.model.location.Location;
import org.jembi.bsis.model.reporting.Comparator;
import org.jembi.bsis.model.reporting.DataValue;
import org.jembi.bsis.model.reporting.Report;
import org.jembi.bsis.repository.InventoryRepository;
import org.jembi.bsis.suites.UnitTestSuite;
import org.jembi.bsis.viewmodel.LocationViewModel;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

public class StockLevelsReportGeneratorTests extends UnitTestSuite {

  @InjectMocks
  private StockLevelsReportGenerator stockLevelsReportGenerator;
  @Mock
  private InventoryRepository inventoryRepository;
  
  @Test
  public void testStockLevelsForLocationReport() {

    UUID locationId = UUID.randomUUID();
    Location location = LocationBuilder.aLocation().withId(locationId).build();
    List<StockLevelDTO> dtos = Arrays.asList(aStockLevelDTO().withComponentType(aComponentType().withComponentTypeName("Type1").build())
            .withBloodAbo("A").withBloodRh("+").withCount(2).build()
    );

    List<DataValue> expectedDataValues = Arrays.asList(
        aDataValue().withValue(2L)
            .withCohort(aCohort().withCategory(CohortConstants.COMPONENT_TYPE_CATEGORY)
                .withComparator(Comparator.EQUALS).withOption("Type1").build())
            .withCohort(aCohort().withCategory(CohortConstants.BLOOD_TYPE_CATEGORY).withComparator(Comparator.EQUALS)
                .withOption("A+").build())
            .build()
    );

    Report expectedReport = aReport().withDataValues(expectedDataValues).build();
    when(inventoryRepository.findStockLevelsForLocation(location.getId(), InventoryStatus.IN_STOCK)).thenReturn(dtos);
    Report returnedReport = stockLevelsReportGenerator.generateStockLevelsForLocationReport(location.getId(), InventoryStatus.IN_STOCK);

    assertThat(returnedReport, is(equalTo(expectedReport)));
  }
  
  @Test
  public void testReportSortDataValuesByVenue_shouldSortCorrectly() {
    LocationViewModel venue1 = aLocationViewModel().withName("venue1").build();
    LocationViewModel venue2 = aLocationViewModel().withName("venue2").build();
    LocationViewModel venue3 = aLocationViewModel().withName("venue3").build();
    
    List<DataValue> dataValues = Arrays.asList(
        aDataValue().withLocation(venue1).build(),
        aDataValue().withLocation(venue2).build(),
        aDataValue().withLocation(venue3).build(),
        aDataValue().withLocation(venue3).build(),
        aDataValue().withLocation(venue2).build(),
        aDataValue().withLocation(venue1).build()
    );
    
    List<DataValue> expectedDataValues = Arrays.asList(
        aDataValue().withLocation(venue1).build(),
        aDataValue().withLocation(venue1).build(),
        aDataValue().withLocation(venue2).build(),
        aDataValue().withLocation(venue2).build(),
        aDataValue().withLocation(venue3).build(),
        aDataValue().withLocation(venue3).build()
    );
    
    Report sortedReport = new Report();
    sortedReport.setDataValues(dataValues);
    sortedReport.sortDataValuesByVenue();
    
    Report expectedReport = new Report();
    expectedReport.setDataValues(expectedDataValues);
    
    assertThat(sortedReport, is(equalTo(expectedReport)));
    
  }

}
