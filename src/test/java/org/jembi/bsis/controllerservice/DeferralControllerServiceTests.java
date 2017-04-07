package org.jembi.bsis.controllerservice;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.jembi.bsis.helpers.builders.DeferralReasonBuilder.aDeferralReason;
import static org.jembi.bsis.helpers.builders.DeferralReasonViewModelBuilder.aDeferralReasonViewModel;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.jembi.bsis.factory.DeferralReasonFactory;
import org.jembi.bsis.model.donordeferral.DeferralReason;
import org.jembi.bsis.repository.DonorRepository;
import org.jembi.bsis.suites.UnitTestSuite;
import org.jembi.bsis.viewmodel.DeferralReasonViewModel;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

public class DeferralControllerServiceTests extends UnitTestSuite {

  @InjectMocks
  private DeferralControllerService deferralControllerService;
  @Mock
  private DonorRepository donorRepository;
  
  @Mock 
  private DeferralReasonFactory deferralReasonFactory;

  @Test
  public void testGetDeferralReasons_shouldReuturnDeferralReasonViewModels() {
    UUID deferralReasonId = UUID.randomUUID();
    
    List<DeferralReason> deferralReasons = Arrays.asList(
        aDeferralReason()
            .withId(deferralReasonId)
            .withReason("Test out comes")
            .thatIsNotDeleted()
            .build(),
        aDeferralReason()
            .withId(deferralReasonId)
            .withReason("Test outcomes")
            .thatIsNotDeleted()
            .build()
    );
    
    List<DeferralReasonViewModel> deferralReasonViewModels = Arrays.asList(
        aDeferralReasonViewModel()
            .withId(deferralReasonId)
            .withReason("Low weight")
            .build(),
            aDeferralReasonViewModel()
            .withId(deferralReasonId)
            .withReason("Test outcomes")
            .build()
    );
    
    // Mocks
    when(donorRepository.getDeferralReasons()).thenReturn(deferralReasons);
    when(deferralReasonFactory.createViewModels(deferralReasons)).thenReturn(deferralReasonViewModels);
    
    // Test
    List<DeferralReasonViewModel> returnedModels = deferralControllerService.getDeferralReasons();
    
    assertThat(returnedModels, is(deferralReasonViewModels));
    
  }

}

