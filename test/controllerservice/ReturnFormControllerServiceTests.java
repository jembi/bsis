package controllerservice;

import static org.mockito.Mockito.when;
import helpers.builders.ReturnFormBuilder;
import helpers.builders.ReturnFormViewModelBuilder;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import model.returnform.ReturnForm;
import model.returnform.ReturnStatus;

import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import repository.ReturnFormRepository;
import suites.UnitTestSuite;
import viewmodel.ReturnFormViewModel;
import factory.ReturnFormFactory;

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
    Long returnedFromId = 1L;
    Long returnedToId = 2L;
    ReturnStatus status = ReturnStatus.RETURNED;
    
    List<ReturnForm> entities = Arrays.asList(
        ReturnFormBuilder.aReturnForm().withId(1L).build(),
        ReturnFormBuilder.aReturnForm().withId(2L).build()
    );
    
    List<ReturnFormViewModel> viewModels = Arrays.asList(
        ReturnFormViewModelBuilder.aReturnFormViewModel().withId(1L).build(),
        ReturnFormViewModelBuilder.aReturnFormViewModel().withId(2L).build()
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
