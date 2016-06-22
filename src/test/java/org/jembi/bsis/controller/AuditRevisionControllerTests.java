package org.jembi.bsis.controller;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.jembi.bsis.helpers.builders.AuditRevisionBuilder.anAuditRevision;
import static org.jembi.bsis.helpers.builders.AuditRevisionViewModelBuilder.anAuditRevisionViewModel;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.jembi.bsis.controller.AuditRevisionController;
import org.jembi.bsis.factory.AuditRevisionViewModelFactory;
import org.jembi.bsis.model.audit.AuditRevision;
import org.jembi.bsis.repository.AuditRevisionRepository;
import org.jembi.bsis.viewmodel.AuditRevisionViewModel;
import org.junit.Test;

public class AuditRevisionControllerTests {


  private static final Date IRRELEVANT_START_DATE = new Date();
  private static final Date IRRELEVANT_END_DATE = new Date();

  private AuditRevisionController auditRevisionController;
  private AuditRevisionRepository auditRevisionRepository;
  private AuditRevisionViewModelFactory auditRevisionViewModelFactory;

  @Test
  public void testGetAuditRevisionsWithNoSearch_shouldCreateAndReturnAuditRevisionViewModels() {
    // Set up fixture
    setUpFixture();

    List<AuditRevision> auditRevisions = Arrays.asList(
        anAuditRevision().withId(1).build(),
        anAuditRevision().withId(2).build()
    );
    List<AuditRevisionViewModel> auditRevisionViewModels = Arrays.asList(
        anAuditRevisionViewModel().withId(1).build(),
        anAuditRevisionViewModel().withId(2).build()
    );

    when(auditRevisionRepository.findAuditRevisions(IRRELEVANT_START_DATE, IRRELEVANT_END_DATE)).thenReturn(auditRevisions);
    when(auditRevisionViewModelFactory.createAuditRevisionViewModels(auditRevisions)).thenReturn(auditRevisionViewModels);

    // Exercise SUT
    List<AuditRevisionViewModel> returnedAuditRevisionViewModels = auditRevisionController.getAuditRevisions(null,
        IRRELEVANT_START_DATE, IRRELEVANT_END_DATE);

    // Verify
    verify(auditRevisionRepository).findAuditRevisions(IRRELEVANT_START_DATE, IRRELEVANT_END_DATE);
    verify(auditRevisionViewModelFactory).createAuditRevisionViewModels(auditRevisions);
    verifyNoMoreInteractions(auditRevisionRepository);
    verifyNoMoreInteractions(auditRevisionViewModelFactory);

    assertThat(returnedAuditRevisionViewModels, is(auditRevisionViewModels));
  }

  @Test
  public void testGetAuditRevisionsWithSearchParam_shouldCreateAndReturnAuditRevisionViewModelsMatchingSearch() {
    // Set up fixture
    setUpFixture();

    List<AuditRevision> auditRevisions = Arrays.asList(
        anAuditRevision().withId(1).build(),
        anAuditRevision().withId(2).build()
    );
    List<AuditRevisionViewModel> auditRevisionViewModels = Arrays.asList(
        anAuditRevisionViewModel().withId(1).build(),
        anAuditRevisionViewModel().withId(2).build()
    );

    String searchParam = "search";

    when(auditRevisionRepository.findAuditRevisionsByUser(searchParam, IRRELEVANT_START_DATE, IRRELEVANT_END_DATE))
        .thenReturn(auditRevisions);
    when(auditRevisionViewModelFactory.createAuditRevisionViewModels(auditRevisions)).thenReturn(auditRevisionViewModels);

    // Exercise SUT
    List<AuditRevisionViewModel> returnedAuditRevisionViewModels = auditRevisionController.getAuditRevisions(
        searchParam, IRRELEVANT_START_DATE, IRRELEVANT_END_DATE);

    // Verify
    verify(auditRevisionRepository).findAuditRevisionsByUser(searchParam, IRRELEVANT_START_DATE, IRRELEVANT_END_DATE);
    verify(auditRevisionViewModelFactory).createAuditRevisionViewModels(auditRevisions);
    verifyNoMoreInteractions(auditRevisionRepository);
    verifyNoMoreInteractions(auditRevisionViewModelFactory);

    assertThat(returnedAuditRevisionViewModels, is(auditRevisionViewModels));
  }

  private void setUpFixture() {
    auditRevisionRepository = mock(AuditRevisionRepository.class);
    auditRevisionViewModelFactory = mock(AuditRevisionViewModelFactory.class);

    auditRevisionController = new AuditRevisionController();
    auditRevisionController.setAuditRevisionRepository(auditRevisionRepository);
    auditRevisionController.setAuditRevisionViewModelFactory(auditRevisionViewModelFactory);
  }

}
