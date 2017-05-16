package org.jembi.bsis.service.report;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.jembi.bsis.helpers.builders.CohortBuilder.aCohort;
import static org.jembi.bsis.helpers.builders.DataValueBuilder.aDataValue;
import static org.jembi.bsis.helpers.builders.LocationBuilder.aUsageSite;
import static org.jembi.bsis.helpers.builders.LocationViewModelBuilder.aLocationViewModel;
import static org.jembi.bsis.helpers.builders.ReportBuilder.aReport;
import static org.jembi.bsis.helpers.builders.TransfusionReactionTypeBuilder.aTransfusionReactionType;
import static org.jembi.bsis.helpers.builders.TransfusionSummaryDTOBuilder.aTransfusionSummaryDTO;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.jembi.bsis.constant.CohortConstants;
import org.jembi.bsis.dto.TransfusionSummaryDTO;
import org.jembi.bsis.factory.LocationFactory;
import org.jembi.bsis.model.location.Location;
import org.jembi.bsis.model.reporting.Comparator;
import org.jembi.bsis.model.reporting.DataValue;
import org.jembi.bsis.model.reporting.Report;
import org.jembi.bsis.model.transfusion.TransfusionOutcome;
import org.jembi.bsis.repository.TransfusionRepository;
import org.jembi.bsis.suites.UnitTestSuite;
import org.jembi.bsis.viewmodel.LocationViewModel;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

public class TransfusionSummaryReportGeneratorTests extends UnitTestSuite {

  @InjectMocks
  private TransfusionSummaryReportGenerator transfusionSummaryReportGenerator;
  @Mock
  private TransfusionRepository transfusionRepository;
  @Mock
  private LocationFactory locationFactory;

  @Test
  public void testGenerateComponentProductionReport() {

    Date irrelevantStartDate = new Date();
    Date irrelevantEndDate = new Date();
    UUID locationId = UUID.randomUUID();
    Location transfusionSite = aUsageSite().withId(locationId).build();
    LocationViewModel transfusionSiteViewModel = aLocationViewModel().withId(locationId).build();

    List<TransfusionSummaryDTO> dtos = Arrays.asList(
        aTransfusionSummaryDTO()
            .withTransfusionOutcome(TransfusionOutcome.TRANSFUSION_REACTION_OCCURRED)
            .withTransfusionReactionType(aTransfusionReactionType().withName("Reaction Test Name1").build())
            .withTransfusionSite(transfusionSite)
            .withCount(2)
            .build(),
        aTransfusionSummaryDTO()
            .withTransfusionOutcome(TransfusionOutcome.TRANSFUSED_UNEVENTFULLY)
            .withTransfusionSite(transfusionSite)
            .withTransfusionReactionType(null)
            .withTransfusionSite(transfusionSite)
            .withCount(2)
            .build()
    );

    List<DataValue> expectedDataValues = Arrays.asList(
        aDataValue()
            .withStartDate(irrelevantStartDate)
            .withEndDate(irrelevantEndDate)
            .withLocation(transfusionSiteViewModel)
            .withValue(2L)
            .withCohort(aCohort()
                .withCategory(CohortConstants.TRANSFUSION_REACTION_TYPE_CATEGORY)
                .withComparator(Comparator.EQUALS)
                .withOption("Reaction Test Name1")
                .build())
            .withCohort(aCohort()
                .withCategory(CohortConstants.TRANSFUSION_OUTCOME_CATEGORY)
                .withComparator(Comparator.EQUALS)
                .withOption(TransfusionOutcome.TRANSFUSION_REACTION_OCCURRED)
                .build())
            .build(),
        aDataValue()
            .withStartDate(irrelevantEndDate)
            .withEndDate(irrelevantEndDate)
            .withLocation(transfusionSiteViewModel)
            .withValue(2L)
            .withCohort(aCohort()
                .withCategory(CohortConstants.TRANSFUSION_REACTION_TYPE_CATEGORY)
                .withComparator(Comparator.EQUALS)
                .withOption(null)
                .build())
            .withCohort(aCohort()
                .withCategory(CohortConstants.TRANSFUSION_OUTCOME_CATEGORY)
                .withComparator(Comparator.EQUALS)
                .withOption(TransfusionOutcome.TRANSFUSED_UNEVENTFULLY)
                .build())
            .build()
    );

    Report expectedReport = aReport()
        .withStartDate(irrelevantStartDate)
        .withEndDate(irrelevantEndDate)
        .withDataValues(expectedDataValues)
        .build();

    when(transfusionRepository.findTransfusionSummaryRecordedForUsageSiteForPeriod(transfusionSite.getId(), irrelevantStartDate, irrelevantEndDate))
        .thenReturn(dtos);
    when(locationFactory.createViewModel(transfusionSite)).thenReturn(transfusionSiteViewModel);

    Report returnedReport = transfusionSummaryReportGenerator.generateTransfusionSummaryReport(transfusionSite.getId(), irrelevantStartDate, irrelevantEndDate);

    assertThat(returnedReport, is(equalTo(expectedReport)));
  }

}