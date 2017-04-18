package org.jembi.bsis.controllerservice;

import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.jembi.bsis.factory.ReturnFormFactory;
import org.jembi.bsis.helpers.builders.ReturnFormBuilder;
import org.jembi.bsis.helpers.builders.ReturnFormViewModelBuilder;
import org.jembi.bsis.model.returnform.ReturnForm;
import org.jembi.bsis.model.returnform.ReturnStatus;
import org.jembi.bsis.repository.ReturnFormRepository;
import org.jembi.bsis.suites.UnitTestSuite;
import org.jembi.bsis.viewmodel.ReturnFormViewModel;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

public class ReturnFormControllerServiceTests extends UnitTestSuite {
  
  @InjectMocks
  private ReturnFormControllerService returnFormControllerService;
  
  @Mock
  private ReturnFormRepository returnFormRepository;
  
  @Mock
  private ReturnFormFactory returnFormFactory;

  @Test
  public void testFindReturnForms_shouldCallRepositoryAndFactory() throws Exception {
    // set up test data
    Date returnDateFrom = new Date();
    Date returnDateTo = new Date();
    UUID returnedFromId = UUID.randomUUID();
    UUID returnedToId = UUID.randomUUID();
    UUID id1 = UUID.randomUUID();
    UUID id2 = UUID.randomUUID();
    ReturnStatus status = ReturnStatus.RETURNED;
    
    List<ReturnForm> entities = Arrays.asList(
        ReturnFormBuilder.aReturnForm().withId(id1).build(),
        ReturnFormBuilder.aReturnForm().withId(id2).build()
    );
    
    List<ReturnFormViewModel> viewModels = Arrays.asList(
        ReturnFormViewModelBuilder.aReturnFormViewModel().withId(id1).build(),
        ReturnFormViewModelBuilder.aReturnFormViewModel().withId(id2).build()
    );
    
    // set up mocks
    when(returnFormRepository.findReturnForms(returnDateFrom, returnDateTo, returnedFromId, returnedToId, status)).thenReturn(entities);
    when(returnFormFactory.createViewModels(entities)).thenReturn(viewModels);
    
    // SUT
    returnFormControllerService.findReturnForms(returnDateFrom, returnDateTo, returnedFromId, returnedToId, status);
    
    // verify behaviour
    Mockito.verify(returnFormRepository).findReturnForms(returnDateFrom, returnDateTo, returnedFromId, returnedToId, status);
    Mockito.verify(returnFormFactory).createViewModels(entities);
  }
}