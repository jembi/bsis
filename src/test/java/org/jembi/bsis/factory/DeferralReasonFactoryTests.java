package org.jembi.bsis.factory;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.jembi.bsis.helpers.builders.DeferralReasonBuilder.aDeferralReason;
import static org.jembi.bsis.helpers.builders.DeferralReasonViewModelBuilder.aDeferralReasonViewModel;
import static org.jembi.bsis.helpers.matchers.DeferralReasonMatcher.hasSameStateAsDeferralReasonViewModel;

import java.util.Arrays;
import java.util.List;

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
    DeferralReason deferralReason = aDeferralReason()
        .withId(1L)
        .withReason("reason")
        .withDefaultDuration(90)
        .withDurationType(DurationType.PERMANENT)
        .build();
        
    DeferralReasonViewModel expectedViewModel = aDeferralReasonViewModel()
        .withId(1L)
        .withReason("reason")
        .withDefaultDuration(90)
        .withDurationType(DurationType.PERMANENT)
        .build();
    
    DeferralReasonViewModel viewModel = deferralReasonFactory.createViewModel(deferralReason);
    
    assertThat(viewModel, hasSameStateAsDeferralReasonViewModel(expectedViewModel));
  }
  
  @Test
  public void testConvertEntitiesToViewModels_shouldReturnExpectedViewModels() {
    List<DeferralReason> deferralReasons = Arrays.asList(
        aDeferralReason().withId(1L).thatIsNotDeleted().build(),
        aDeferralReason().withId(2L).thatIsNotDeleted().build()
    );
        
    List<DeferralReasonViewModel> expectedViewModels = Arrays.asList(
        aDeferralReasonViewModel().withId(1L).build(),
        aDeferralReasonViewModel().withId(2L).build()
    );
    
    List<DeferralReasonViewModel> viewModels = deferralReasonFactory.createViewModels(deferralReasons);
    
    assertThat("View models are created", viewModels.size() == 2);
    assertThat(viewModels.get(0), hasSameStateAsDeferralReasonViewModel(expectedViewModels.get(0)));
    assertThat(viewModels.get(1), hasSameStateAsDeferralReasonViewModel(expectedViewModels.get(1)));
  }
}
