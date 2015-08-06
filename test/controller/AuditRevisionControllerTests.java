package controller;

import static helpers.builders.AuditRevisionBuilder.anAuditRevision;
import static helpers.builders.AuditRevisionViewModelBuilder.anAuditRevisionViewModel;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import model.audit.AuditRevision;

import org.junit.Test;

import repository.AuditRevisionRepository;
import viewmodel.AuditRevisionViewModel;
import factory.AuditRevisionViewModelFactory;

public class AuditRevisionControllerTests {
    
    private AuditRevisionController auditRevisionController;
    private AuditRevisionRepository auditRevisionRepository;
    private AuditRevisionViewModelFactory auditRevisionViewModelFactory;
    
    @Test
    public void testGetAuditRevisions_shouldCreateAndReturnAuditRevisionViewModels() {
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
        
        when(auditRevisionRepository.findRecentAuditRevisions()).thenReturn(auditRevisions);
        when(auditRevisionViewModelFactory.createAuditRevisionViewModels(auditRevisions)).thenReturn(auditRevisionViewModels);
        
        // Exercise SUT
        List<AuditRevisionViewModel> returnedAuditRevisionViewModels = auditRevisionController.getAuditRevisions();
        
        // Verify
        verify(auditRevisionRepository).findRecentAuditRevisions();
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
