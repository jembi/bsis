package org.jembi.bsis.factory;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.jembi.bsis.helpers.builders.DeferralReasonBuilder.aDeferralReason;
import static org.jembi.bsis.helpers.builders.DeferralReasonViewModelBuilder.aDeferralReasonViewModel;
import static org.jembi.bsis.helpers.matchers.DeferralReasonMatcher.hasSameStateAsDeferralReasonViewModel;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.jembi.bsis.model.donordeferral.DeferralReason;
import org.jembi.bsis.model.donordeferral.DurationType;
import org.jembi.bsis.suites.UnitTestSuite;
import org.jembi.bsis.viewmodel.DeferralReasonViewModel;
import org.junit.Test;
import org.mockito.InjectMocks;

public class DeferralReasonFactoryTests extends UnitTestSuite {
  
  @InjectMocks
  private DeferralReasonFactory deferralReasonFactory;

  @Test
  public void testConvertEntityToViewModel_shouldReturnExpectedViewModel() {
    UUID deferralReasonId = UUID.randomUUID();
    DeferralReason deferralReason = aDeferralReason()
        .withId(deferralReasonId)
        .withReason("reason")
        .withDefaultDuration(90)
        .withDurationType(DurationType.PERMANENT)
        .build();
        
    DeferralReasonViewModel expectedViewModel = aDeferralReasonViewModel()
        .withId(deferralReasonId)
        .withReason("reason")
        .withDefaultDuration(90)
        .withDurationType(DurationType.PERMANENT)
        .build();
    
    DeferralReasonViewModel viewModel = deferralReasonFactory.createViewModel(deferralReason);
    
    assertThat(viewModel, hasSameStateAsDeferralReasonViewModel(expectedViewModel));
  }
  
  @Test
  public void testConvertEntitiesToViewModels_shouldReturnExpectedViewModels() {
    UUID deferralReasonId1 = UUID.randomUUID();
    UUID deferralReasonId2 = UUID.randomUUID();
    
    List<DeferralReason> deferralReasons = Arrays.asList(
        aDeferralReason().withId(deferralReasonId1).thatIsNotDeleted().build(),
        aDeferralReason().withId(deferralReasonId2).thatIsNotDeleted().build()
    );
        
    List<DeferralReasonViewModel> expectedViewModels = Arrays.asList(
        aDeferralReasonViewModel().withId(deferralReasonId1).build(),
        aDeferralReasonViewModel().withId(deferralReasonId2).build()
    );
    
    List<DeferralReasonViewModel> viewModels = deferralReasonFactory.createViewModels(deferralReasons);
    
    assertThat("View models are created", viewModels.size() == 2);
    assertThat(viewModels.get(0), hasSameStateAsDeferralReasonViewModel(expectedViewModels.get(0)));
    assertThat(viewModels.get(1), hasSameStateAsDeferralReasonViewModel(expectedViewModels.get(1)));
  }
}
